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

import org.gradle.internal.Factory
import org.gradle.cache.internal.btree.BTreePersistentIndexedCache
import spock.lang.Specification

class MultiProcessSafePersistentIndexedCacheTest extends Specification {
    final FileAccess fileAccess = Mock()
    final Factory<BTreePersistentIndexedCache<String, String>> factory = Mock()
    final cache = new DefaultMultiProcessSafePersistentIndexedCache<String, String>(factory, fileAccess)
    final BTreePersistentIndexedCache<String, String> backingCache = Mock()
    
    def "opens cache on first access"() {
        when:
        cache.get("value")

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * factory.create() >> backingCache
    }

    def "holds read lock while getting entry from cache"() {
        given:
        cacheOpened()

        when:
        def result = cache.get("value")

        then:
        result == "result"

        and:
        1 * fileAccess.readFile(!null) >> { Factory action -> action.create() }
        1 * backingCache.get("value") >> "result"
        0 * _._
    }

    def "holds write lock while putting entry into cache"() {
        given:
        cacheOpened()

        when:
        cache.put("key", "value")

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * backingCache.put("key", "value")
        0 * _._
    }

    def "holds write lock while removing entry from cache"() {
        given:
        cacheOpened()

        when:
        cache.remove("key")

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * backingCache.remove("key")
        0 * _._
    }

    def "holds write lock while closing cache"() {
        given:
        cacheOpened()

        when:
        cache.close()

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * backingCache.close()
        0 * _._
    }

    def "closes cache when closed"() {
        given:
        cacheOpened()

        when:
        cache.close()

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * backingCache.close()
        0 * _._
    }

    def "does nothing on close when cache is not open"() {
        when:
        cache.close()

        then:
        0 * _._
    }

    def "does nothing on close after cache already closed"() {
        cacheOpened()

        when:
        cache.close()

        then:
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * backingCache.close()
        0 * _._

        when:
        cache.close()

        then:
        0 * _._
    }

    def cacheOpened() {
        1 * fileAccess.writeFile(!null) >> { Runnable action -> action.run() }
        1 * factory.create() >> backingCache
        
        cache.get("something")
    }
}
