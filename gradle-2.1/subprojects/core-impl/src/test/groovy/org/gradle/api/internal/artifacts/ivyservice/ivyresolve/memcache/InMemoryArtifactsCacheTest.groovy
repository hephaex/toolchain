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

package org.gradle.api.internal.artifacts.ivyservice.ivyresolve.memcache
import org.gradle.api.internal.artifacts.component.DefaultModuleComponentIdentifier
import org.gradle.api.internal.artifacts.ivyservice.BuildableArtifactResolveResult
import org.gradle.api.internal.artifacts.ivyservice.DefaultBuildableArtifactResolveResult
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ArtifactResolveException
import org.gradle.api.internal.artifacts.metadata.ModuleVersionArtifactIdentifier
import spock.lang.Specification

class InMemoryArtifactsCacheTest extends Specification {

    def stats = new InMemoryCacheStats()
    def cache = new InMemoryArtifactsCache(stats)

    static componentId(String group, String module, String version) {
        return DefaultModuleComponentIdentifier.newId(group, module, version)
    }

    def "caches and supplies artifacts"() {
        def artifactId = Stub(ModuleVersionArtifactIdentifier)
        def artifactFile = new File("foo")

        given:
        def originalResult = new DefaultBuildableArtifactResolveResult()
        originalResult.resolved(artifactFile)
        cache.newArtifact(artifactId, originalResult)

        def differentIdResult = Mock(BuildableArtifactResolveResult)
        def sameIdResult = Mock(BuildableArtifactResolveResult)

        when:
        def differentId = Stub(ModuleVersionArtifactIdentifier)
        def differentIdFound = cache.supplyArtifact(differentId, differentIdResult)

        then:
        !differentIdFound
        0 * differentIdResult._

        when:
        def sameIdFound = cache.supplyArtifact(artifactId, sameIdResult)

        then:
        sameIdFound
        1 * sameIdResult.resolved(artifactFile)
    }

    def "does not cache failed artifact resolves"() {
        def artifactId = Stub(ModuleVersionArtifactIdentifier)
        def failedResult = Stub(BuildableArtifactResolveResult) { getFailure() >> new ArtifactResolveException("bad") }
        cache.newArtifact(artifactId, failedResult)

        def result = Mock(BuildableArtifactResolveResult)

        when:
        def fromCache = cache.supplyArtifact(artifactId, result)

        then:
        !fromCache
        0 * result._
    }
}
