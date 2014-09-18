/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.configuration

import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.initialization.ClassLoaderScope
import org.gradle.api.internal.initialization.ScriptHandlerFactory
import org.gradle.groovy.scripts.ScriptSource
import org.gradle.initialization.InitScript
import spock.lang.Specification

class DefaultInitScriptProcessorTest extends Specification {

    void "can execute init script"() {
        when:
        def scriptPluginFactory = Mock(ScriptPluginFactory)
        def scriptHandlerFactory = Mock(ScriptHandlerFactory)
        def gradleScope = Mock(ClassLoaderScope)
        def initScriptMock = Mock(ScriptSource)
        def gradleMock = Mock(GradleInternal)
        def siblingScope = Mock(ClassLoaderScope)
        def scriptHandler = Mock(ScriptHandler)
        def scriptPlugin = Mock(ScriptPlugin)

        1 * gradleMock.getClassLoaderScope() >> gradleScope
        1 * gradleScope.createSibling() >> siblingScope

        1 * scriptHandlerFactory.create(initScriptMock, siblingScope) >> scriptHandler
        1 * scriptPluginFactory.create(initScriptMock, scriptHandler, siblingScope, gradleScope, "initscript", InitScript, false) >> scriptPlugin
        1 * scriptPlugin.apply(gradleMock)

        DefaultInitScriptProcessor processor = new DefaultInitScriptProcessor(scriptPluginFactory, scriptHandlerFactory)

        then:
        processor.process(initScriptMock, gradleMock)
    }
}
