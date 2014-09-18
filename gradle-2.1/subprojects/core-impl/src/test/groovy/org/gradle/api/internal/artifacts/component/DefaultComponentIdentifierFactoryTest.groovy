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

package org.gradle.api.internal.artifacts.component

import org.gradle.api.Project
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.internal.artifacts.DefaultModule
import org.gradle.api.internal.artifacts.ModuleInternal
import org.gradle.api.internal.artifacts.ProjectBackedModule
import org.gradle.api.internal.project.ProjectInternal
import spock.lang.Specification

class DefaultComponentIdentifierFactoryTest extends Specification {
    ComponentIdentifierFactory componentIdentifierFactory = new DefaultComponentIdentifierFactory()

    def "can create project component identifier"() {
        given:
        Project project = Mock(ProjectInternal)
        ModuleInternal module = new ProjectBackedModule(project)

        when:
        ComponentIdentifier componentIdentifier = componentIdentifierFactory.createComponentIdentifier(module)

        then:
        project.path >> ':a'
        componentIdentifier == new DefaultProjectComponentIdentifier(':a')
    }

    def "can create module component identifier"() {
        given:
        ModuleInternal module = new DefaultModule('some-group', 'some-name', '1.0')

        when:
        ComponentIdentifier componentIdentifier = componentIdentifierFactory.createComponentIdentifier(module)

        then:
        componentIdentifier == new DefaultModuleComponentIdentifier('some-group', 'some-name', '1.0')
    }
}
