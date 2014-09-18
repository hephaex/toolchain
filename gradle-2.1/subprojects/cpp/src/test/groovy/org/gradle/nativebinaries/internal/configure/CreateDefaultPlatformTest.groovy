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

package org.gradle.nativebinaries.internal.configure

import org.gradle.nativebinaries.platform.PlatformContainer
import org.gradle.nativebinaries.plugins.NativeComponentModelPlugin
import spock.lang.Specification

class CreateDefaultPlatformTest extends Specification {
    def platforms = Mock(PlatformContainer)
    def action = new NativeComponentModelPlugin.Rules()

    def "adds a default platform when none configured"() {
        when:
        action.createDefaultPlatforms(platforms)

        then:
        1 * platforms.empty >> true
        1 * platforms.create("current")
        0 * platforms._
    }

    def "does not add default platform when some configured"() {
        when:
        action.createDefaultPlatforms(platforms)

        then:
        1 * platforms.empty >> false
        0 * platforms._
    }
}
