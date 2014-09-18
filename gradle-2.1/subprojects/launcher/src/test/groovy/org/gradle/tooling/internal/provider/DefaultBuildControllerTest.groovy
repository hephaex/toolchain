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

package org.gradle.tooling.internal.provider

import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.internal.service.ServiceRegistry
import org.gradle.tooling.internal.gradle.GradleProjectIdentity
import org.gradle.tooling.internal.protocol.InternalUnsupportedModelException
import org.gradle.tooling.internal.protocol.ModelIdentifier
import org.gradle.tooling.model.internal.ProjectSensitiveToolingModelBuilder
import org.gradle.tooling.provider.model.ToolingModelBuilder
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import org.gradle.tooling.provider.model.UnknownModelException
import spock.lang.Specification

class DefaultBuildControllerTest extends Specification {
    def gradle = Stub(GradleInternal)
    def registry = Stub(ToolingModelBuilderRegistry)
    def project = Stub(ProjectInternal) {
        getServices() >> Stub(ServiceRegistry) {
            get(ToolingModelBuilderRegistry) >> registry
        }
    }
    def modelId = Stub(ModelIdentifier) {
        getName() >> 'some.model'
    }
    def modelBuilder = Stub(ToolingModelBuilder)
    def controller = new DefaultBuildController(gradle)

    def "adapts model not found exception to protocol exception"() {
        def failure = new UnknownModelException("not found")

        given:
        _ * gradle.defaultProject >> project
        _ * registry.getBuilder('some.model') >> { throw failure }

        when:
        controller.getModel(null, modelId)

        then:
        InternalUnsupportedModelException e = thrown()
        e.cause == failure
    }

    def "uses builder for specified project"() {
        def target = Stub(GradleProjectIdentity)
        def rootProject = Stub(ProjectInternal)
        def model = new Object()

        given:
        _ * target.path >> ":some:path"
        _ * gradle.rootProject >> rootProject
        _ * rootProject.project(":some:path") >> project
        _ * registry.getBuilder("some.model") >> modelBuilder
        _ * modelBuilder.buildAll("some.model", project) >> model

        when:
        def result = controller.getModel(target, modelId)

        then:
        result.getModel() == model
    }

    def "uses builder for default project when none specified"() {
        def model = new Object()

        given:
        _ * gradle.defaultProject >> project
        _ * registry.getBuilder("some.model") >> modelBuilder
        _ * modelBuilder.buildAll("some.model", project) >> model

        when:
        def result = controller.getModel(null, modelId)

        then:
        result.getModel() == model
    }

    def "passes information about default project when context sensitive builder is used"() {
        def contextModelBuilder = Stub(ProjectSensitiveToolingModelBuilder)
        def model = new Object()

        given:
        _ * gradle.defaultProject >> project
        _ * registry.getBuilder("some.model") >> contextModelBuilder
        _ * contextModelBuilder.buildAll("some.model", project, true) >> model

        when:
        def result = controller.getModel(null, modelId)

        then:
        result.getModel() == model
    }

    def "passes information about specified project when context sensitive builder is used"() {
        def contextModelBuilder = Stub(ProjectSensitiveToolingModelBuilder)
        def model = new Object()
        def target = Stub(GradleProjectIdentity)
        def rootProject = Stub(ProjectInternal)

        given:
        _ * target.path >> ":some:path"
        _ * gradle.rootProject >> rootProject
        _ * rootProject.project(":some:path") >> project
        _ * registry.getBuilder("some.model") >> contextModelBuilder
        _ * contextModelBuilder.buildAll("some.model", project, false) >> model

        when:
        def result = controller.getModel(target, modelId)

        then:
        result.getModel() == model
    }
}
