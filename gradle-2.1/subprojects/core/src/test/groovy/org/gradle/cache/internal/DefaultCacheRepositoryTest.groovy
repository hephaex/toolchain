/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.cache.internal

import org.gradle.api.Action
import org.gradle.cache.CacheBuilder
import org.gradle.cache.CacheValidator
import org.gradle.cache.PersistentCache
import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.util.GradleVersion
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.cache.internal.FileLockManager.LockMode.None
import static org.gradle.cache.internal.FileLockManager.LockMode.Shared
import static org.gradle.cache.internal.filelock.LockOptionsBuilder.mode

class DefaultCacheRepositoryTest extends Specification {
    @Rule
    public final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()
    private final TestFile homeDir = tmpDir.createDir("home")
    private final TestFile sharedCacheDir = homeDir.file("caches")
    private final String version = GradleVersion.current().version
    private final Map<String, ?> properties = [a: "value", b: "value2"]
    private final CacheFactory cacheFactory = Mock()
    private final PersistentCache cache = Mock()
    private final CacheScopeMapping scopeMapping = Mock()
    private final DefaultCacheRepository repository = new DefaultCacheRepository(scopeMapping, cacheFactory)

    public void createsGlobalDirectoryBackedStore() {
        when:
        def result = repository.store("a/b/c").open()

        then:
        result == cache
        1 * scopeMapping.getBaseDirectory(null, "a/b/c", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.openStore(sharedCacheDir, null, mode(Shared), null) >> cache
        0 * cacheFactory._
    }

    public void createsGlobalDirectoryBackedCache() {
        when:
        def result = repository.cache("a/b/c").open()

        then:
        result == cache
        1 * scopeMapping.getBaseDirectory(null, "a/b/c", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(Shared), null) >> cache
        0 * cacheFactory._
    }

    public void createsGlobalCacheWithProperties() {
        when:
        repository.cache("a/b/c").withProperties(properties).open()

        then:
        1 * scopeMapping.getBaseDirectory(null, "a/b/c", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, properties, mode(Shared), null) >> cache
    }

    public void createsScopedCache() {
        when:
        repository.cache("scope", "a/b/c").open()

        then:
        1 * scopeMapping.getBaseDirectory("scope", "a/b/c", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(Shared), null) >> cache
    }

    public void createsCacheWithBaseDirectory() {
        when:
        repository.cache(sharedCacheDir).open()

        then:
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(Shared), null) >> cache
    }

    public void createsCrossVersionCache() {
        when:
        repository.cache("scope", "a/b/c").withCrossVersionCache().open()

        then:
        1 * scopeMapping.getBaseDirectory("scope", "a/b/c", CacheBuilder.VersionStrategy.SharedCache) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(Shared), null) >> cache
    }

    public void canSpecifyInitializerActionForDirectoryCache() {
        Action<?> action = Mock()

        when:
        repository.cache("a").withInitializer(action).open()

        then:
        1 * scopeMapping.getBaseDirectory(null, "a", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(Shared), action) >> cache
    }

    public void canSpecifyLockModeForDirectoryCache() {
        when:
        repository.cache("a").withLockOptions(mode(None)).open()

        then:
        1 * scopeMapping.getBaseDirectory(null, "a", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, null, [:], mode(None), null) >> cache
    }

    public void canSpecifyDisplayNameForDirectoryCache() {
        when:
        repository.cache("a").withDisplayName("<cache>").open()

        then:
        1 * scopeMapping.getBaseDirectory(null, "a", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, "<cache>", null, [:], mode(Shared), null) >> cache
    }

    public void canSpecifyCacheValidatorForDirectoryCache() {
        CacheValidator validator = Mock();
        when:
        repository.cache("a").withValidator(validator).open()

        then:
        1 * scopeMapping.getBaseDirectory(null, "a", CacheBuilder.VersionStrategy.CachePerVersion) >> sharedCacheDir
        1 * cacheFactory.open(sharedCacheDir, null, validator, [:], mode(Shared), null) >> cache
    }
}
