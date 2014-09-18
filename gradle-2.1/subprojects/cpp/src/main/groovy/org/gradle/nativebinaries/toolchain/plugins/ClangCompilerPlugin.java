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

package org.gradle.nativebinaries.toolchain.plugins;

import org.gradle.api.Incubating;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.nativebinaries.plugins.NativeComponentModelPlugin;
import org.gradle.nativebinaries.toolchain.Clang;
import org.gradle.nativebinaries.toolchain.internal.ToolChainRegistryInternal;
import org.gradle.nativebinaries.toolchain.internal.clang.ClangToolChain;
import org.gradle.process.internal.ExecActionFactory;

/**
 * A {@link Plugin} which makes the <a href="http://clang.llvm.org">Clang</a> compiler available for compiling C/C++ code.
 */
@Incubating
public class ClangCompilerPlugin implements Plugin<Project> {

    public void apply(Project project) {
        project.getPlugins().apply(NativeComponentModelPlugin.class);
    }

    /**
     * Model rules.
     */
    @RuleSource
    public static class Rules {
        @Mutate
        public static void addToolChain(ToolChainRegistryInternal toolChainRegistry, ServiceRegistry serviceRegistry) {
            final FileResolver fileResolver = serviceRegistry.get(FileResolver.class);
            final ExecActionFactory execActionFactory = serviceRegistry.get(ExecActionFactory.class);
            final Instantiator instantiator = serviceRegistry.get(Instantiator.class);

            toolChainRegistry.registerFactory(Clang.class, new NamedDomainObjectFactory<Clang>() {
                public Clang create(String name) {
                    return instantiator.newInstance(ClangToolChain.class, name, OperatingSystem.current(), fileResolver, execActionFactory, instantiator);
                }
            });
            toolChainRegistry.registerDefaultToolChain(ClangToolChain.DEFAULT_NAME, Clang.class);
        }

    }
}
