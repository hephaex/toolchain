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

package org.gradle.api.plugins.buildcomparison.gradle.internal

import org.gradle.api.internal.file.FileResolver
import org.gradle.util.TestUtil
import spock.lang.Specification

class DefaultGradleBuildInvocationSpecTest extends Specification {

    FileResolver fileResolver = TestUtil.createRootProject().fileResolver

    def "equals and hashCode"() {
        given:
        def left = spec()
        def right = spec()

        expect:
        left == right
        left.hashCode() == right.hashCode()

        when:
        left.tasks = ["a"]

        then:
        left != right
    }

    protected DefaultGradleBuildInvocationSpec spec() {
        new DefaultGradleBuildInvocationSpec(fileResolver, ".")
    }

    def "gradle version is validated"() {
        when:
        spec().gradleVersion = "1.1"

        then:
        notThrown Exception

        when:
        spec().gradleVersion = "sadfasd"

        then:
        thrown IllegalArgumentException
    }

}
