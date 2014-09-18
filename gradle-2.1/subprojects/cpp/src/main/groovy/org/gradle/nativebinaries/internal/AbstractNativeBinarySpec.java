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

package org.gradle.nativebinaries.internal;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.language.DependentSourceSet;
import org.gradle.language.base.LanguageSourceSet;
import org.gradle.language.base.internal.LanguageSourceSetContainer;
import org.gradle.api.internal.AbstractBuildableModelElement;
import org.gradle.runtime.base.internal.BinaryNamingScheme;
import org.gradle.nativebinaries.*;
import org.gradle.nativebinaries.internal.resolve.NativeBinaryResolveResult;
import org.gradle.nativebinaries.internal.resolve.NativeDependencyResolver;
import org.gradle.nativebinaries.platform.Platform;
import org.gradle.nativebinaries.toolchain.internal.ToolChainInternal;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractNativeBinarySpec extends AbstractBuildableModelElement implements NativeBinarySpecInternal {
    private final NativeComponentSpec component;
    private final LanguageSourceSetContainer sourceSets = new LanguageSourceSetContainer();
    private final Set<? super Object> libs = new LinkedHashSet<Object>();
    private final DefaultTool linker = new DefaultTool();
    private final DefaultTool staticLibArchiver = new DefaultTool();
    private final NativeBinaryTasks tasks = new DefaultNativeBinaryTasks(this);
    private final BinaryNamingScheme namingScheme;
    private final Flavor flavor;
    private final ToolChainInternal toolChain;
    private final Platform targetPlatform;
    private final BuildType buildType;
    private final NativeDependencyResolver resolver;
    private boolean buildable;

    protected AbstractNativeBinarySpec(NativeComponentSpec owner, Flavor flavor, ToolChainInternal toolChain, Platform targetPlatform, BuildType buildType,
                                       BinaryNamingScheme namingScheme, NativeDependencyResolver resolver) {
        this.component = owner;
        this.namingScheme = namingScheme;
        this.flavor = flavor;
        this.toolChain = toolChain;
        this.targetPlatform = targetPlatform;
        this.buildType = buildType;
        this.buildable = true;
        this.resolver = resolver;
        component.getSource().all(new Action<LanguageSourceSet>() {
            public void execute(LanguageSourceSet sourceSet) {
                sourceSets.add(sourceSet);
            }
        });

    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public String getDisplayName() {
        return namingScheme.getDescription();
    }

    public String getName() {
        return namingScheme.getLifecycleTaskName();
    }

    public NativeComponentSpec getComponent() {
        return component;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public ToolChainInternal getToolChain() {
        return toolChain;
    }

    public Platform getTargetPlatform() {
        return targetPlatform;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public DomainObjectSet<LanguageSourceSet> getSource() {
        return sourceSets;
    }

    public void source(Object sources) {
        sourceSets.source(sources);
    }

    public Tool getLinker() {
        return linker;
    }

    public Tool getStaticLibArchiver() {
        return staticLibArchiver;
    }

    public NativeBinaryTasks getTasks() {
        return tasks;
    }

    public BinaryNamingScheme getNamingScheme() {
        return namingScheme;
    }

    public Collection<NativeDependencySet> getLibs() {
        return resolve(sourceSets.withType(DependentSourceSet.class)).getAllResults();
    }

    public Collection<NativeDependencySet> getLibs(DependentSourceSet sourceSet) {
        return resolve(Collections.singleton(sourceSet)).getAllResults();
    }

    public void lib(Object notation) {
        libs.add(notation);
    }

    public Collection<NativeLibraryBinary> getDependentBinaries() {
        return resolve(sourceSets.withType(DependentSourceSet.class)).getAllLibraryBinaries();
    }

    private NativeBinaryResolveResult resolve(Collection<? extends DependentSourceSet> sourceSets) {
        Set<? super Object> allLibs = new LinkedHashSet<Object>(libs);
        for (DependentSourceSet dependentSourceSet : sourceSets) {
            allLibs.addAll(dependentSourceSet.getLibs());
        }
        NativeBinaryResolveResult resolution = new NativeBinaryResolveResult(this, allLibs);
        resolver.resolve(resolution);
        return resolution;
    }

    public boolean isBuildable() {
        return buildable;
    }

    public void setBuildable(boolean buildable) {
        this.buildable = buildable;
    }

    public boolean isLegacyBinary() {
        return false;
    }
}
