/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.configuration.project

import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.internal.initialization.ClassLoaderScope
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.internal.project.ProjectScript
import org.gradle.configuration.ScriptPlugin
import org.gradle.configuration.ScriptPluginFactory
import org.gradle.groovy.scripts.ScriptSource
import spock.lang.Specification

public class BuildScriptProcessorTest extends Specification {
    def project = Mock(ProjectInternal)
    def scriptSource = Mock(ScriptSource)
    def configurerFactory = Mock(ScriptPluginFactory)
    def scriptPlugin = Mock(ScriptPlugin)
    def targetScope = Mock(ClassLoaderScope)
    def baseScope = Mock(ClassLoaderScope)
    def BuildScriptProcessor buildScriptProcessor = new BuildScriptProcessor(configurerFactory)
    private ScriptHandler scriptHandler;

    def "setup"() {
        _ * project.buildScriptSource >> scriptSource
        scriptHandler = Mock(ScriptHandler)
        project.getBuildscript() >> scriptHandler
        project.getClassLoaderScope() >> targetScope
        project.getBaseClassLoaderScope() >> baseScope
    }

    def configuresProjectUsingBuildScript() {
        when:
        buildScriptProcessor.execute(project)

        then:
        1 * configurerFactory.create(scriptSource, scriptHandler, targetScope, baseScope, "buildscript", ProjectScript, true) >> scriptPlugin
        1 * scriptPlugin.apply(project)
    }
}