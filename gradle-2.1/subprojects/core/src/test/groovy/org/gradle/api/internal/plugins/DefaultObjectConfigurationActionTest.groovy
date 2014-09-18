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
package org.gradle.api.internal.plugins

import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.initialization.ClassLoaderScope
import org.gradle.api.internal.initialization.ScriptHandlerFactory

import org.gradle.configuration.ScriptPlugin
import org.gradle.configuration.ScriptPluginFactory
import org.gradle.groovy.scripts.DefaultScript
import org.junit.Test
import spock.lang.Specification

class DefaultObjectConfigurationActionTest extends Specification {
    Object target = new Object()
    URI file = new URI('script:something')

    def resolver = Mock(FileResolver)
    def scriptPluginFactory = Mock(ScriptPluginFactory)
    def scriptHandlerFactory = Mock(ScriptHandlerFactory)
    def scriptHandler = Mock(ScriptHandler)
    def scriptCompileScope = Mock(ClassLoaderScope)
    def parentCompileScope = Mock(ClassLoaderScope)
    def configurer = Mock(ScriptPlugin)

    DefaultObjectConfigurationAction action = new DefaultObjectConfigurationAction(resolver, scriptPluginFactory, scriptHandlerFactory, parentCompileScope, target)

    void doesNothingWhenNothingSpecified() {
        expect:
        action.execute()
    }

    @Test
    public void appliesScriptsToDefaultTargetObject() {
        given:
        1 * resolver.resolveUri('script') >> file
        1 * parentCompileScope.createChild() >> scriptCompileScope
        1 * scriptHandlerFactory.create(_, scriptCompileScope) >> scriptHandler
        1 * scriptPluginFactory.create(_, scriptHandler, scriptCompileScope, parentCompileScope, "buildscript", DefaultScript, false) >> configurer

        when:
        action.from('script')

        then:
        action.execute()
    }

    @Test
    public void appliesScriptsToTargetObjects() {
        when:
        Object target1 = new Object()
        Object target2 = new Object()
        1 * resolver.resolveUri('script') >> file
        1 * scriptHandlerFactory.create(_, scriptCompileScope) >> scriptHandler
        1 * scriptPluginFactory.create(_, scriptHandler, scriptCompileScope, parentCompileScope,  "buildscript", DefaultScript, false) >> configurer
        1 * configurer.apply(target1)
        1 * configurer.apply(target2)
        1 * parentCompileScope.createChild() >> scriptCompileScope


        then:
        action.from('script')
        action.to(target1)
        action.to(target2)
        action.execute()
    }

    @Test
    public void flattensCollections() {
        when:
        Object target1 = new Object()
        Object target2 = new Object()
        1 * resolver.resolveUri('script') >> file
        1 * scriptHandlerFactory.create(_, scriptCompileScope) >> scriptHandler
        1 * scriptPluginFactory.create(_, scriptHandler, scriptCompileScope, parentCompileScope, "buildscript", DefaultScript, false) >> configurer
        1 * configurer.apply(target1)
        1 * configurer.apply(target2)
        1 * parentCompileScope.createChild() >> scriptCompileScope


        then:
        action.from('script')
        action.to([[target1], target2])
        action.execute()
    }

}

