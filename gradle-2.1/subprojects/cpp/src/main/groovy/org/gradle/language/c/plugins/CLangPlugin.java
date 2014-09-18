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
package org.gradle.language.c.plugins;

import com.google.common.collect.Maps;
import org.gradle.api.Incubating;
import org.gradle.api.Plugin;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.language.base.internal.LanguageRegistry;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.language.c.CSourceSet;
import org.gradle.language.c.internal.DefaultCSourceSet;
import org.gradle.language.internal.NativeLanguageRegistration;
import org.gradle.nativebinaries.language.c.tasks.CCompile;
import org.gradle.nativebinaries.language.internal.CompileTaskConfig;
import org.gradle.nativebinaries.language.internal.DefaultPreprocessingTool;

import java.util.Map;

/**
 * Adds core C language support.
 */
@Incubating
public class CLangPlugin implements Plugin<ProjectInternal> {

    public void apply(final ProjectInternal project) {

        project.getPlugins().apply(ComponentModelBasePlugin.class);
        project.getExtensions().getByType(LanguageRegistry.class).add(new C());
    }

    private static class C extends NativeLanguageRegistration<CSourceSet> {
        public String getName() {
            return "c";
        }

        public Class<CSourceSet> getSourceSetType() {
            return CSourceSet.class;
        }

        public Class<? extends CSourceSet> getSourceSetImplementation() {
            return DefaultCSourceSet.class;
        }

        public Map<String, Class<?>> getBinaryTools() {
            Map<String, Class<?>> tools = Maps.newLinkedHashMap();
            tools.put("cCompiler", DefaultPreprocessingTool.class);
            return tools;
        }


        public SourceTransformTaskConfig getTransformTask() {
            return new CompileTaskConfig(this, CCompile.class);
        }

    }
}
