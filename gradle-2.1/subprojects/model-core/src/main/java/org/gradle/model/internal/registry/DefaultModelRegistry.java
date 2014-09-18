/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.model.internal.registry;


import com.google.common.base.Function;
import com.google.common.collect.*;
import org.gradle.api.Action;
import org.gradle.api.Nullable;
import org.gradle.api.Transformer;
import org.gradle.internal.Actions;
import org.gradle.internal.Transformers;
import org.gradle.model.internal.core.*;
import org.gradle.model.internal.core.rule.describe.ModelRuleDescriptor;

import java.util.*;

public class DefaultModelRegistry implements ModelRegistry {

    /*
                Things we aren't doing and should:

                - Detecting cycles between config rules
                - Detecting dangling, unbound, rules
                - Detecting model elements with no object at their parent path
                - Detecting mutation rules registering parent model
                - Detecting a rule binding the same input twice (maybe that's ok)
                - Detecting a rule trying to bind the same element to mutate and to read

             */
    private final Map<ModelPath, ModelElement> store = new HashMap<ModelPath, ModelElement>();

    private final Map<ModelPath, BoundModelCreator> creations = new HashMap<ModelPath, BoundModelCreator>();
    private final Multimap<ModelPath, BoundModelMutator<?>> mutators = ArrayListMultimap.create();
    private final Multimap<ModelPath, List<ModelPath>> usedMutators = ArrayListMultimap.create();
    private final Multimap<ModelPath, BoundModelMutator<?>> finalizers = ArrayListMultimap.create();
    private final Multimap<ModelPath, List<ModelPath>> usedFinalizers = ArrayListMultimap.create();

    // TODO rework this listener mechanism to be more targeted, in that interested parties nominate what they are interested in (e.g. path or type) and use this to only notify relavant listeners
    private final List<ModelCreationListener> modelCreationListeners = new LinkedList<ModelCreationListener>();

    private final List<RuleBinder<?>> binders = Lists.newLinkedList();

    private static String toString(ModelRuleDescriptor descriptor) {
        StringBuilder stringBuilder = new StringBuilder();
        descriptor.describeTo(stringBuilder);
        return stringBuilder.toString();
    }

    public void create(ModelCreator creator) {
        ModelPath path = creator.getPath();
        BoundModelCreator existingCreation = creations.get(path);

        if (existingCreation != null) {
            throw new DuplicateModelException(
                    String.format(
                            "Cannot register model creation rule '%s' for path '%s' as the rule '%s' is already registered to create a model element at this path",
                            toString(creator.getDescriptor()),
                            path,
                            toString(existingCreation.getCreator().getDescriptor())
                    )
            );
        }

        ModelElement existing = store.get(path);
        if (existing != null) {
            throw new DuplicateModelException(
                    String.format(
                            "Cannot register model creation rule '%s' for path '%s' as the rule '%s' is already registered (and the model element has been created)",
                            toString(creator.getDescriptor()),
                            path,
                            toString(existing.getCreatorDescriptor())
                    )
            );
        }

        notifyCreationListeners(creator);
        bind(creator);
    }

    public <T> void mutate(ModelMutator<T> mutator) {
        bind(mutator, mutators);
    }

    public <T> void finalize(ModelMutator<T> mutator) {
        bind(mutator, finalizers);
    }

    private void bind(final ModelCreator creator) {
        final RuleBinder<Void> binder = bind(null, creator.getInputs(), creator.getDescriptor(), new Action<RuleBinder<Void>>() {
            public void execute(RuleBinder<Void> ruleBinding) {
                BoundModelCreator boundCreator = new BoundModelCreator(creator, ruleBinding.getInputBindings());
                creations.put(creator.getPath(), boundCreator);
            }
        });

        bindInputs(binder);
    }

    @SuppressWarnings("unchecked")
    private <T> RuleBinder<T> bind(ModelReference<T> subject, List<ModelReference<?>> inputs, ModelRuleDescriptor descriptor, Action<? super RuleBinder<T>> onBind) {
        RuleBinder<T> binder = new RuleBinder<T>(subject, inputs, descriptor, Actions.composite(new Action<RuleBinder<T>>() {
            public void execute(RuleBinder<T> binder) {
                // TODO this is going to run even if we never added the binder to the bindings (inefficient)
                binders.remove(binder);
            }
        }, onBind));

        if (!binder.isBound()) {
            binders.add(binder);
        }

        return binder;
    }

    private <T> void bind(final ModelMutator<T> mutator, final Multimap<ModelPath, BoundModelMutator<?>> mutators) {
        final RuleBinder<T> binder = bind(mutator.getSubject(), mutator.getInputs(), mutator.getDescriptor(), new Action<RuleBinder<T>>() {
            public void execute(RuleBinder<T> ruleBinder) {
                BoundModelMutator<T> boundMutator = new BoundModelMutator<T>(mutator, ruleBinder.getSubjectBinding(), ruleBinder.getInputBindings());
                ModelPath path = boundMutator.getSubject().getPath();
                assertNotFinalized(path);
                mutators.put(path, boundMutator);
            }
        });

        final ModelReference<T> subjectReference = binder.getSubjectReference();

        registerListener(new ModelCreationListener() {
            public boolean onCreate(ModelPath path, ModelPromise promise) {
                if (subjectReference.getPath() == null) {
                    boolean typeCompatible = promise.asWritable(subjectReference.getType());
                    if (typeCompatible && subjectReference.getPath() == null) {
                        binder.bindSubject(path);
                        return true;
                    }
                } else if (path.equals(subjectReference.getPath())) {
                    boolean typeCompatible = promise.asWritable(subjectReference.getType());
                    if (typeCompatible) {
                        binder.bindSubject(path);
                        return true;
                    } else {
                        // TODO proper exception
                        throw new IllegalStateException("incompatible type");
                    }
                }

                return false;
            }
        });

        bindInputs(binder);
    }

    private void bindInputs(final RuleBinder<?> binder) {
        List<ModelReference<?>> inputReferences = binder.getInputReferences();

        for (int i = 0; i < inputReferences.size(); i++) {
            final ModelReference<?> inputReference = inputReferences.get(i);
            final int finalI = i;
            registerListener(new ModelCreationListener() {
                public boolean onCreate(ModelPath path, ModelPromise promise) {
                    if (inputReference.getPath() == null) {
                        if (promise.asReadOnly(inputReference.getType())) {
                            binder.bindInput(finalI, path);
                            return true;
                        }
                    } else if (path.equals(inputReference.getPath())) {
                        boolean typeCompatible = promise.asReadOnly(inputReference.getType());
                        if (typeCompatible) {
                            binder.bindInput(finalI, path);
                            return true;
                        } else {
                            // TODO proper exception
                            throw new IllegalStateException("incompatible type");
                        }
                    }

                    return false;
                }
            });
        }
    }

    private void assertNotFinalized(ModelPath path) {
        if (store.containsKey(path)) {
            throw new IllegalStateException("model '" + path + "' is finalized");
        }
    }

    public <T> T get(ModelPath path, ModelType<T> type) {
        ModelElement element = get(path);
        ModelView<? extends T> typed = assertView(element, type, "get(ModelPath, ModelType)");
        return typed.getInstance();
    }

    public ModelElement element(ModelPath path) {
        return get(path);
    }

    public ModelState state(ModelPath path) {
        ModelElement closed = store.get(path);
        if (closed != null) {
            return toState(closed.getPath(), ModelState.Status.FINALIZED);
        }

        BoundModelCreator creator = creations.get(path);
        if (creator != null) {
            return toState(creator.getCreator().getPath(), ModelState.Status.PENDING);
        }

        return null;
    }

    private ModelState toState(ModelPath path, ModelState.Status status) {
        return new ModelState(path, status);
    }

    public void registerListener(ModelCreationListener listener) {
        boolean remove;

        for (Map.Entry<ModelPath, BoundModelCreator> entry : creations.entrySet()) {
            BoundModelCreator boundCreator = entry.getValue();
            ModelCreator creator = boundCreator.getCreator();
            remove = listener.onCreate(creator.getPath(), creator.getPromise());
            if (remove) {
                return;
            }
        }

        for (ModelElement element : store.values()) {
            remove = listener.onCreate(element.getPath(), element.getPromise());
            if (remove) {
                return;
            }
        }

        modelCreationListeners.add(listener);
    }

    public void remove(ModelPath path) {
        if (creations.remove(path) == null) {
            if (store.remove(path) == null) {
                throw new RuntimeException("Tried to remove model " + path + " but it is not registered");
            } else {
                if (isDependedOn(path)) {
                    throw new RuntimeException("Tried to remove model " + path + " but it is depended on by other model elements");
                }
            }
        }
    }

    public void validate() throws UnboundModelRulesException {
        if (!binders.isEmpty()) {
            throw new UnboundModelRulesException(binders);
        }
    }

    // TODO - this needs to consider partially bound rules
    private boolean isDependedOn(ModelPath candidate) {
        Transformer<Iterable<ModelPath>, BoundModelMutator<?>> extractInputPaths = new Transformer<Iterable<ModelPath>, BoundModelMutator<?>>() {
            public Iterable<ModelPath> transform(BoundModelMutator<?> original) {
                return Iterables.transform(original.getInputs(), new Function<ModelBinding<?>, ModelPath>() {
                    @Nullable
                    public ModelPath apply(ModelBinding<?> input) {
                        return input.getPath();
                    }
                });
            }
        };

        Transformer<List<ModelPath>, List<ModelPath>> passThrough = Transformers.noOpTransformer();

        return hasModelPath(candidate, mutators.values(), extractInputPaths)
                || hasModelPath(candidate, usedMutators.values(), passThrough)
                || hasModelPath(candidate, finalizers.values(), extractInputPaths)
                || hasModelPath(candidate, usedFinalizers.values(), passThrough);
    }

    private <T> boolean hasModelPath(ModelPath candidate, Iterable<T> things, Transformer<? extends Iterable<ModelPath>, T> transformer) {
        for (T thing : things) {
            for (ModelPath path : transformer.transform(thing)) {
                if (path.equals(candidate)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Set<ModelPath> getPromisedPaths() {
        return ImmutableSet.<ModelPath>builder().addAll(creations.keySet()).build();
    }

    private ModelElement get(ModelPath path) {
        if (store.containsKey(path)) {
            return store.get(path);
        }

        BoundModelCreator creation = removeCreator(path);
        return createAndClose(creation);
    }

    private ModelElement createAndClose(BoundModelCreator creation) {
        ModelElement element = doCreate(creation);
        close(element);
        return element;
    }

    private void close(ModelElement model) {
        ModelPath path = model.getPath();
        fireMutations(model, mutators.removeAll(path), usedMutators);
        fireMutations(model, finalizers.removeAll(path), usedFinalizers);

        // close all the child objects
        Set<ModelPath> promisedPaths = getPromisedPaths();
        for (ModelPath modelPath : promisedPaths) {
            if (path.isDirectChild(modelPath)) {
                get(modelPath);
            }
        }
    }

    private void fireMutations(ModelElement element, Iterable<BoundModelMutator<?>> mutators, Multimap<ModelPath, List<ModelPath>> used) {
        for (BoundModelMutator<?> mutator : mutators) {
            fireMutation(element, mutator);
            List<ModelPath> inputPaths = Lists.transform(mutator.getInputs(), new Function<ModelBinding<?>, ModelPath>() {
                @Nullable
                public ModelPath apply(ModelBinding<?> input) {
                    return input.getPath();
                }
            });
            used.put(element.getPath(), inputPaths);
        }
    }

    private <T> ModelView<? extends T> assertView(ModelElement element, ModelType<T> targetType, String msg, Object... msgArgs) {
        ModelAdapter adapter = element.getAdapter();
        ModelView<? extends T> view = adapter.asReadOnly(targetType);
        if (view == null) {
            // TODO better error reporting here
            throw new IllegalArgumentException("Model element " + element.getPath() + " is not compatible with requested " + targetType + " (operation: " + String.format(msg, msgArgs) + ")");
        } else {
            return view;
        }
    }

    private <T> ModelView<? extends T> assertView(ModelElement element, ModelBinding<T> binding, ModelRuleDescriptor sourceDescriptor, Inputs inputs) {
        ModelAdapter adapter = element.getAdapter();
        ModelView<? extends T> view = adapter.asWritable(binding, sourceDescriptor, inputs, this);
        if (view == null) {
            // TODO better error reporting here
            throw new IllegalArgumentException("Cannot project model element " + binding.getPath() + " to writable type '" + binding.getReference().getType() + "' for rule " + sourceDescriptor);
        } else {
            return view;
        }
    }

    private BoundModelCreator removeCreator(ModelPath path) {
        BoundModelCreator creator = creations.remove(path);
        if (creator == null) {
            throw new IllegalStateException("No creator for '" + path + "'");
        } else {
            return creator;
        }
    }

    private ModelElement doCreate(BoundModelCreator boundCreator) {
        ModelCreator creator = boundCreator.getCreator();
        ModelPath path = creator.getPath();
        Inputs inputs = toInputs(boundCreator.getInputs());

        ModelAdapter adapter;
        try {
            adapter = creator.create(inputs);
        } catch (Exception e) {
            // TODO some representation of state of the inputs
            throw new ModelRuleExecutionException(creator.getDescriptor(), e);
        }

        ModelElement element = toElement(adapter, creator);
        store.put(path, element);
        return element;
    }

    private ModelElement toElement(ModelAdapter adapter, ModelCreator creator) {
        return new ModelElement(creator.getPath(), creator.getPromise(), adapter, creator.getDescriptor());
    }

    private <T> void fireMutation(ModelElement element, BoundModelMutator<T> boundMutator) {
        Inputs inputs = toInputs(boundMutator.getInputs());
        ModelMutator<T> mutator = boundMutator.getMutator();
        ModelRuleDescriptor descriptor = mutator.getDescriptor();

        ModelView<? extends T> view = assertView(element, boundMutator.getSubject(), descriptor, inputs);
        try {
            mutator.mutate(view.getInstance(), inputs);
        } catch (Exception e) {
            throw new ModelRuleExecutionException(descriptor, e);
        } finally {
            view.close();
        }
    }

    private Inputs toInputs(Iterable<? extends ModelBinding<?>> bindings) {
        ImmutableList.Builder<ModelRuleInput<?>> builder = ImmutableList.builder();
        for (ModelBinding<?> binding : bindings) {
            ModelRuleInput<?> input = toInput(binding);
            builder.add(input);
        }
        return new DefaultInputs(builder.build());
    }

    private <T> ModelRuleInput<T> toInput(ModelBinding<T> binding) {
        ModelPath path = binding.getPath();
        ModelElement element = element(path);
        ModelView<? extends T> view = assertView(element, binding.getReference().getType(), "toInputs");
        return ModelRuleInput.of(binding, view);
    }

    private void notifyCreationListeners(ModelCreator creator) {
        ListIterator<ModelCreationListener> modelCreationListenerListIterator = modelCreationListeners.listIterator();
        while (modelCreationListenerListIterator.hasNext()) {
            ModelCreationListener next = modelCreationListenerListIterator.next();
            boolean remove = next.onCreate(creator.getPath(), creator.getPromise());
            if (remove) {
                modelCreationListenerListIterator.remove();
            }
        }
    }

}
