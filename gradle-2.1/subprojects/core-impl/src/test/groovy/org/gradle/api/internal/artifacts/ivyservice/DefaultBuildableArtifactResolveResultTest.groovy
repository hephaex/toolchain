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

package org.gradle.api.internal.artifacts.ivyservice
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ArtifactNotFoundException
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ArtifactResolveException
import org.gradle.api.internal.artifacts.metadata.ComponentArtifactIdentifier
import spock.lang.Specification

class DefaultBuildableArtifactResolveResultTest extends Specification {
    final result = new DefaultBuildableArtifactResolveResult()
    final artifactFile = Mock(File)
    final artifactId = Mock(ComponentArtifactIdentifier)

    def "has no result by default"() {
        expect:
        !result.hasResult()
    }

    def "can have artifact result"() {
        when:
        result.resolved(artifactFile)

        then:
        result.file == artifactFile
        result.failure == null
        result.hasResult()
    }

    def "can have missing result"() {
        when:
        result.notFound(artifactId)

        then:
        result.failure instanceof ArtifactNotFoundException
        result.hasResult()

        when:
        result.file

        then:
        def e = thrown(ArtifactNotFoundException)
        result.failure == e
    }

    def "can have failure result"() {
        def failure = new ArtifactResolveException("broken")

        when:
        result.failed(failure)

        then:
        result.failure == failure
        result.hasResult()

        when:
        result.file

        then:
        ArtifactResolveException e = thrown()
        e == failure
    }

    def "cannot get file when no result specified"() {
        when:
        result.file

        then:
        IllegalStateException e = thrown()
        e.message == 'No result has been specified.'
    }

    def "cannot get failure when no result specified"() {
        when:
        result.failure

        then:
        IllegalStateException e = thrown()
        e.message == 'No result has been specified.'
    }

    def "cannot get file when resolve failed"() {
        def failure = new ArtifactResolveException("broken")

        when:
        result.failed(failure)
        result.file

        then:
        ArtifactResolveException e = thrown()
        e == failure
    }
}
