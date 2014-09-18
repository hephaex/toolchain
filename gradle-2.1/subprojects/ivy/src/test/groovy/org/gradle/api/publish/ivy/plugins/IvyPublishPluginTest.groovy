/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.api.publish.ivy.plugins

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.internal.tasks.TaskContainerInternal
import org.gradle.api.internal.xml.XmlTransformer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.ivy.IvyPublication
import org.gradle.api.publish.ivy.internal.publication.DefaultIvyPublication
import org.gradle.api.publish.ivy.internal.publication.IvyPublicationInternal
import org.gradle.model.internal.core.ModelType
import org.gradle.util.TestUtil
import spock.lang.Specification

class IvyPublishPluginTest extends Specification {

    ProjectInternal project = TestUtil.createRootProject()
    PublishingExtension publishing

    def setup() {
        project.plugins.apply(IvyPublishPlugin)
        publishing = project.extensions.getByType(PublishingExtension)
    }

    def "no publication without component"() {
        expect:
        publishing.publications.empty
    }

    def "publication can be added"() {
        when:
        publishing.publications.create("test", IvyPublication)

        then:
        publishing.publications.size() == 1
        publishing.publications.test instanceof DefaultIvyPublication
    }

    def "creates publish task for publication and repository"() {
        when:
        publishing.publications.create("test", IvyPublication)
        publishing.repositories { ivy { url = "http://foo.com" } }
        project.modelRegistry.get(TaskContainerInternal.MODEL_PATH, ModelType.UNTYPED)
        def publishTask = project.tasks["publishTestPublicationToIvyRepository"]

        then:
        publishTask != null
        project.tasks["publish"].dependsOn.contains publishTask
    }

    def "ivy publication coordinates are a snapshot of project identity"() {
        when:
        project.group = "foo"
        project.version = 1.0
        project.status = "another"

        and:
        publishing.publications.create("test", IvyPublication)

        then:
        with(publishing.publications.test) {
            identity.module == project.name
            identity.organisation == "foo"
            identity.revision == "1.0"
            descriptor.status == "integration"
        }

        when:
        project.group = "changed-group"
        project.version = "changed-version"

        then:
        with(publishing.publications.test) {
            identity.organisation == "foo"
            identity.revision == "1.0"
        }
    }

    def "can configure descriptor"() {
        given:
        publishing.publications.create("ivy", IvyPublication)
        IvyPublicationInternal publication = publishing.publications.ivy

        when:
        publication.descriptor {
            withXml {
                it.asNode().@foo = "bar"
            }
        }

        then:
        def transformer = new XmlTransformer()
        transformer.addAction(publication.descriptor.xmlAction)
        transformer.transform("<things/>").contains('things foo="bar"')
    }
}
