/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.model.internal.core;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeResolver;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A type token for the type of a model element.
 * <p>
 * Borrows from Guava's type token.
 * Represent a fully resolved/bound type.
 */
public abstract class ModelType<T> {
    // TODO restrict this to concrete types, validated recursively
    // TODO analyze performance cost of wrapping Guava's type token instead of inlining the code

    private static class Simple<T> extends ModelType<T> {
        private Simple(TypeToken<T> typeToken) {
            super(typeToken);
        }
    }

    public static final ModelType<Object> UNTYPED = ModelType.of(Object.class);

    private final TypeToken<T> typeToken;

    private ModelType(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    protected ModelType() {
        typeToken = new TypeToken<T>(getClass()) {
        };
    }

    protected ModelType(final Class<?> clazz) {
        this(new TypeToken<T>(clazz) {
        });
    }

    public static <T> ModelType<T> of(Class<T> clazz) {
        return new Simple<T>(TypeToken.of(clazz));
    }

    public static ModelType<?> of(Type type) {
        return toModelType(TypeToken.of(type));
    }

    private static <T> ModelType<T> toModelType(TypeToken<T> type) {
        return new Simple<T>(type);
    }

    public Class<? super T> getRawClass() {
        return typeToken.getRawType();
    }

    public boolean isParameterized() {
        return typeToken.getType() instanceof ParameterizedType;
    }

    public List<ModelType<?>> getTypeVariables() {
        if (isParameterized()) {
            List<Type> types = Arrays.asList(((ParameterizedType) typeToken.getType()).getActualTypeArguments());
            return ImmutableList.<ModelType<?>>builder().addAll(Iterables.transform(types, new Function<Type, ModelType<?>>() {
                public ModelType<?> apply(Type input) {
                    return ModelType.of(input);
                }
            })).build();
        } else {
            return Collections.emptyList();
        }
    }

    public boolean isAssignableFrom(ModelType<?> modelType) {
        return typeToken.isAssignableFrom(modelType.typeToken);
    }

    @Override
    public String toString() {
        return typeToken.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelType)) {
            return false;
        }

        ModelType<?> modelType = (ModelType<?>) o;

        return typeToken.equals(modelType.typeToken);
    }

    @Override
    public int hashCode() {
        return typeToken.hashCode();
    }

    abstract public static class Builder<T> {
        private TypeToken<T> typeToken;

        public Builder() {
            typeToken = new TypeToken<T>(getClass()) {
            };
        }

        @SuppressWarnings("unchecked")
        public <I> Builder<T> where(Parameter<I> parameter, ModelType<I> type) {
            TypeResolver resolver = new TypeResolver().where(parameter.typeVariable, type.typeToken.getType());
            typeToken = (TypeToken<T>) TypeToken.of(resolver.resolveType(typeToken.getType()));
            return this;
        }

        public ModelType<T> build() {
            return new Simple<T>(typeToken);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    abstract public static class Parameter<T> {
        private final TypeVariable<?> typeVariable;

        public Parameter() {
            Type type = new TypeToken<T>(getClass()) {
            }.getType();
            if (type instanceof TypeVariable<?>) {
                this.typeVariable = (TypeVariable<?>) type;
            } else {
                throw new IllegalStateException("T for Parameter<T> MUST be a type variable");
            }
        }
    }
}
