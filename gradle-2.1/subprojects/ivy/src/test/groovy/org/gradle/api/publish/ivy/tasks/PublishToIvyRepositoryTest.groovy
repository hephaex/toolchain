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

package org.gradle.api.publish.ivy.tasks
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.publish.ivy.IvyPublication
import org.gradle.api.publish.ivy.internal.publisher.IvyNormalizedPublication
import org.gradle.api.publish.ivy.internal.publication.IvyPublicationInternal
import org.gradle.util.TestUtil
import spock.lang.Specification

class PublishToIvyRepositoryTest extends Specification {

    Project project
    PublishToIvyRepository publish

    def normalizedPublication = Mock(IvyNormalizedPublication)

    def publication = Mock(IvyPublicationInternal) {
        asNormalisedPublication() >> normalizedPublication
    }

    def repository = Mock(IvyArtifactRepository) {}

    def setup() {
        project = TestUtil.createRootProject()
        publish = createPublish("publish")
    }

    def "publication must implement the internal interface"() {
        when:
        publish.publication = [:] as IvyPublication

        then:
        thrown(InvalidUserDataException)

        when:
        publish.publication = [:] as IvyPublicationInternal

        then:
        notThrown(Exception)
    }

    def "the publishableFiles of the publication are inputs of the task"() {
        given:
        def publishableFiles = project.files("a", "b", "c")

        publication.getPublishableFiles() >> publishableFiles

        when:
        publish.publication = publication

        then:
        publish.inputs.files.files == publishableFiles.files
    }

    PublishToIvyRepository createPublish(String name) {
        project.tasks.create(name, PublishToIvyRepository)
    }

}
