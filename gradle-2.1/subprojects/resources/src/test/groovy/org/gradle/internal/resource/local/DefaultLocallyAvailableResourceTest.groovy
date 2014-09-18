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
package org.gradle.internal.resource.local

import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.internal.hash.HashUtil
import org.junit.Rule
import spock.lang.Specification

public class DefaultLocallyAvailableResourceTest extends Specification {
    @Rule final TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    def "uses value from origin file"() {
        given:
        def origin = tmpDir.file("origin")
        origin << "some text"

        when:
        def DefaultLocallyAvailableResource resource = new DefaultLocallyAvailableResource(origin)

        then:
        resource.sha1 == HashUtil.createHash(origin, 'SHA1')
        resource.contentLength == origin.length()
        resource.lastModified == origin.lastModified()
    }

    def "sha1 content length and last modified do not change when file is subsequently modified"() {
        given:
        def origin = tmpDir.file("origin")
        origin << "some text"


        when:
        def DefaultLocallyAvailableResource resource = new DefaultLocallyAvailableResource(origin)
        def originalSha1 = resource.sha1
        def originalContentLength = resource.contentLength
        def originalLastModified = resource.lastModified

        and:
        origin << "some more text"
        origin.setLastModified(11)

        then:
        resource.sha1 != HashUtil.createHash(origin, 'SHA1')
        resource.contentLength != origin.length()
        resource.lastModified != origin.lastModified()

        and:
        resource.sha1 == originalSha1
        resource.contentLength == originalContentLength
        resource.lastModified == originalLastModified
    }
}
