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


package org.gradle.api.plugins.quality

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.hamcrest.CoreMatchers

class FindBugsClasspathValidationIntegrationTest extends AbstractIntegrationSpec {

    def setup() {
        buildFile << """
            apply plugin: "java"
            apply plugin: "findbugs"

            repositories {
                mavenCentral()
            }
        """
        file('src/main/java/org/gradle/BadClass.java') << 'package org.gradle; public class BadClass { public boolean isFoo(Object arg) { System.exit(1); return true; } }'
    }

    @Requires(TestPrecondition.JDK8_OR_LATER)
    def "informs that FindBugs version is too low"() {
        buildFile << """
            dependencies {
                //downgrade version:
                findbugs "com.google.code.findbugs:findbugs:2.0.3"
            }
        """
        expect:
        fails("findbugsMain")
        failure.assertThatCause(CoreMatchers.containsString("too low to work with currently used Java"))
    }

    @Requires(TestPrecondition.JDK6)
    def "informs that FindBugs version is too high"() {
        expect:
        fails("findbugsMain")
        failure.assertThatCause(CoreMatchers.containsString("too high to work with currently used Java"))
    }
}
