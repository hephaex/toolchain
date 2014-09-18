/*
 * Copyright 2014 the original author or authors.
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
package org.gradle.scala

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import spock.lang.Issue

class ScalaPluginIntegrationTest extends AbstractIntegrationSpec {
    @Requires(TestPrecondition.JDK8_OR_LATER)
    @Issue("https://issues.gradle.org/browse/GRADLE-3094")
    def "can apply scala plugin when running under java 8"() {
        file("build.gradle") << """
apply plugin: "scala"

task someTask
"""

        expect:
        succeeds("someTask")
    }
}