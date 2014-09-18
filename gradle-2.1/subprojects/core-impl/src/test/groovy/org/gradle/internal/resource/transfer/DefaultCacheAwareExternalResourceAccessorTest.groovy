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

package org.gradle.internal.resource.transfer

import org.gradle.api.internal.artifacts.ivyservice.CacheLockingManager
import org.gradle.internal.resource.ExternalResource
import org.gradle.internal.resource.cached.CachedExternalResource
import org.gradle.internal.resource.cached.CachedExternalResourceIndex
import org.gradle.internal.resource.local.LocallyAvailableResourceCandidates
import org.gradle.internal.resource.metadata.ExternalResourceMetaData
import org.gradle.api.internal.file.TemporaryFileProvider
import org.gradle.internal.hash.HashUtil
import org.gradle.internal.resource.local.DefaultLocallyAvailableResource
import org.gradle.internal.resource.local.LocallyAvailableResource
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.util.BuildCommencedTimeProvider
import org.junit.Rule
import spock.lang.Specification

class DefaultCacheAwareExternalResourceAccessorTest extends Specification {
    @Rule TestNameTestDirectoryProvider tempDir = new TestNameTestDirectoryProvider()
    final accessor = Mock(ExternalResourceAccessor)
    final index = Mock(CachedExternalResourceIndex)
    final timeProvider = Mock(BuildCommencedTimeProvider)
    final tempFile = tempDir.file("temp-file")
    final cachedFile = tempDir.file("cached-file")
    final temporaryFileProvider = Stub(TemporaryFileProvider) {
        createTemporaryFile(_, _, _) >> tempFile
    }
    final cacheLockingManager = Mock(CacheLockingManager)
    final cache = new DefaultCacheAwareExternalResourceAccessor(accessor, index, timeProvider, temporaryFileProvider, cacheLockingManager)

    def "returns null when the request resource is not cached and does not exist in the remote repository"() {
        def uri = new URI("scheme:thing")
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def localCandidates = Mock(LocallyAvailableResourceCandidates)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result == null

        and:
        1 * index.lookup("scheme:thing") >> null
        1 * localCandidates.isNone() >> true
        1 * accessor.getResource(uri) >> null
        0 * _._
    }

    def "returns null when the request resource is not cached and there are local candidates but the resource does not exist in the remote repository"() {
        def uri = new URI("scheme:thing")
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def localCandidates = Mock(LocallyAvailableResourceCandidates)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result == null

        and:
        1 * index.lookup("scheme:thing") >> null
        1 * localCandidates.isNone() >> false
        1 * accessor.getMetaData(uri) >> null
        0 * _._
    }

    def "downloads resource and moves it into the cache when it is not cached"() {
        def uri = new URI("scheme:thing")
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def localCandidates = Mock(LocallyAvailableResourceCandidates)
        def remoteResource = Mock(ExternalResource)
        def metaData = Mock(ExternalResourceMetaData)
        def localResource = new DefaultLocallyAvailableResource(cachedFile)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result.localResource.file == cachedFile
        result.metaData == metaData

        and:
        1 * index.lookup("scheme:thing") >> null
        1 * localCandidates.isNone() >> true
        1 * accessor.getResource(uri) >> remoteResource
        _ * remoteResource.name >> "remoteResource"
        1 * remoteResource.writeTo(tempFile)
        1 * remoteResource.close()

        and:
        1 * cacheLockingManager.useCache(_, _) >> { String description, org.gradle.internal.Factory factory ->
            return factory.create()
        }
        1 * fileStore.moveIntoCache(tempFile) >> localResource
        1 * remoteResource.metaData >> metaData
        1 * index.store("scheme:thing", cachedFile, metaData)
        0 * _._
    }

    def "reuses cached resource if it has not expired"() {
        def uri = new URI("scheme:thing")
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def localCandidates = Mock(LocallyAvailableResourceCandidates)
        def metaData = Mock(ExternalResourceMetaData)
        def cachedResource = Stub(CachedExternalResource)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result.localResource.file == cachedFile
        result.metaData == metaData

        and:
        1 * index.lookup("scheme:thing") >> cachedResource
        _ * timeProvider.currentTime >> 24000L
        _ * cachedResource.cachedAt >> 24000L
        _ * cachedResource.cachedFile >> cachedFile
        _ * cachedResource.externalResourceMetaData >> metaData
        0 * _._
    }

    def "will use sha1 from metadata for finding candidates if available"() {
        given:
        def localCandidates = Mock(LocallyAvailableResourceCandidates)
        def cached = Mock(CachedExternalResource)
        def candidate = tempDir.createFile("candidate-file")
        def sha1 = HashUtil.createHash(candidate, "sha1")
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def cachedMetaData = Mock(ExternalResourceMetaData)
        def remoteMetaData = Mock(ExternalResourceMetaData)
        def localCandidate = Mock(LocallyAvailableResource)
        def uri = new URI("scheme:thing")
        def localResource = new DefaultLocallyAvailableResource(cachedFile)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result.localResource.file == cachedFile
        result.metaData == remoteMetaData

        and:
        1 * index.lookup("scheme:thing") >> cached
        timeProvider.currentTime >> 24000L
        cached.cachedAt >> 23999L
        cached.externalResourceMetaData >> cachedMetaData
        1 * accessor.getMetaData(uri) >> remoteMetaData
        localCandidates.none >> false
        remoteMetaData.sha1 >> sha1
        remoteMetaData.etag >> null
        remoteMetaData.lastModified >> null
        cachedMetaData.etag >> null
        cachedMetaData.lastModified >> null
        1 * localCandidates.findByHashValue(sha1) >> localCandidate
        localCandidate.file >> candidate
        cached.cachedFile >> cachedFile
        0 * _._

        and:
        1 * cacheLockingManager.useCache(_, _) >> { String description, org.gradle.internal.Factory factory ->
            return factory.create()
        }
        1 * fileStore.moveIntoCache(tempFile) >> localResource
        1 * index.store("scheme:thing", cachedFile, remoteMetaData)
        0 * _._
    }

    def "downloads resource directly when local candidate cannot be copied"() {
        given:
        def localCandidates = Mock(LocallyAvailableResourceCandidates)
        def cached = Mock(CachedExternalResource)
        def candidate = tempDir.createFile("candidate-file")
        def sha1 = HashUtil.createHash(candidate, "sha1")
        candidate << "some extra stuff"
        def fileStore = Mock(CacheAwareExternalResourceAccessor.ResourceFileStore)
        def cachedMetaData = Mock(ExternalResourceMetaData)
        def remoteMetaData = Mock(ExternalResourceMetaData)
        def localCandidate = Mock(LocallyAvailableResource)
        def uri = new URI("scheme:thing")
        def remoteResource = Mock(ExternalResource)
        def localResource = new DefaultLocallyAvailableResource(cachedFile)

        when:
        def result = cache.getResource(uri, fileStore, localCandidates)

        then:
        result.localResource.file == cachedFile
        result.metaData == remoteMetaData

        and:
        1 * index.lookup("scheme:thing") >> cached
        timeProvider.currentTime >> 24000L
        cached.cachedAt >> 23999L
        cached.externalResourceMetaData >> cachedMetaData
        1 * accessor.getMetaData(uri) >> remoteMetaData
        localCandidates.none >> false
        remoteMetaData.sha1 >> sha1
        remoteMetaData.etag >> null
        remoteMetaData.lastModified >> null
        cachedMetaData.etag >> null
        cachedMetaData.lastModified >> null
        1 * localCandidates.findByHashValue(sha1) >> localCandidate
        localCandidate.file >> candidate
        cached.cachedFile >> cachedFile
        1 * accessor.getResource(uri) >> remoteResource
        1 * remoteResource.writeTo(tempFile)
        0 * _._

        and:
        1 * cacheLockingManager.useCache(_, _) >> { String description, org.gradle.internal.Factory factory ->
            return factory.create()
        }
        1 * fileStore.moveIntoCache(tempFile) >> localResource
        1 * index.store("scheme:thing", cachedFile, remoteMetaData)
        0 * _._
    }
}
