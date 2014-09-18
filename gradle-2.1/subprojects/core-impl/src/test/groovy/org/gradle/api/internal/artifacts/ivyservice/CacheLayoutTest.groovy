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
package org.gradle.api.internal.artifacts.ivyservice

import org.gradle.util.VersionNumber
import spock.lang.Specification

class CacheLayoutTest extends Specification {
    def "use root layout"() {
        when:
        CacheLayout cacheLayout = CacheLayout.ROOT

        then:
        cacheLayout.key == 'modules-2'
        cacheLayout.version == VersionNumber.parse("2.0.0")
        cacheLayout.formattedVersion == '2'
        cacheLayout.getPath(new File('some/dir')) == new File('some/dir/modules-2')
    }

    def "use file store layout"() {
        when:
        CacheLayout cacheLayout = CacheLayout.FILE_STORE

        then:
        cacheLayout.key == 'files-2.1'
        cacheLayout.version == VersionNumber.parse("2.1.0")
        cacheLayout.formattedVersion == '2.1'
        cacheLayout.getPath(new File('some/dir')) == new File('some/dir/files-2.1')
    }

    def "use metadata store layout"() {
        when:
        CacheLayout cacheLayout = CacheLayout.META_DATA

        then:
        cacheLayout.key == 'metadata-2.13'
        cacheLayout.version == VersionNumber.parse("2.13.0")
        cacheLayout.formattedVersion == '2.13'
        cacheLayout.getPath(new File('some/dir')) == new File('some/dir/metadata-2.13')
    }
}
