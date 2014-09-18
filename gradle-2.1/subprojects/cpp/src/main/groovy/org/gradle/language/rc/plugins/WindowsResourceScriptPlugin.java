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
package org.gradle.language.rc.plugins;

import com.google.common.collect.Maps;
import org.gradle.api.Incubating;
import org.gradle.api.Plugin;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.language.base.internal.LanguageRegistry;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.language.internal.NativeLanguageRegistration;
import org.gradle.language.rc.WindowsResourceSet;
import org.gradle.language.rc.internal.DefaultWindowsResourceSet;
import org.gradle.nativebinaries.NativeBinarySpec;
import org.gradle.nativebinaries.language.internal.DefaultPreprocessingTool;
import org.gradle.nativebinaries.language.internal.WindowsResourcesCompileTaskConfig;
import org.gradle.runtime.base.BinarySpec;

import java.util.Map;

/**
 * Adds core language support for Windows resource script files.
 */
@Incubating
public class WindowsResourceScriptPlugin implements Plugin<ProjectInternal> {

    public void apply(final ProjectInternal project) {
        project.getPlugins().apply(ComponentModelBasePlugin.class);
        project.getExtensions().getByType(LanguageRegistry.class).add(new WindowsResources());
    }

    private static class WindowsResources extends NativeLanguageRegistration<WindowsResourceSet> {
        public String getName() {
            return "rc";
        }

        public Class<WindowsResourceSet> getSourceSetType() {
            return WindowsResourceSet.class;
        }

        public Class<? extends WindowsResourceSet> getSourceSetImplementation() {
            return DefaultWindowsResourceSet.class;
        }

        public Map<String, Class<?>> getBinaryTools() {
            Map<String, Class<?>> tools = Maps.newLinkedHashMap();
            tools.put("rcCompiler", DefaultPreprocessingTool.class);
            return tools;
        }

        public SourceTransformTaskConfig getTransformTask() {
            return new WindowsResourcesCompileTaskConfig();
        }

        @Override
        public boolean applyToBinary(BinarySpec binary) {
            return binary instanceof NativeBinarySpec && shouldProcessResources((NativeBinarySpec) binary);
        }

        private boolean shouldProcessResources(NativeBinarySpec binary) {
            return binary.getTargetPlatform().getOperatingSystem().isWindows();
        }
    }
}
