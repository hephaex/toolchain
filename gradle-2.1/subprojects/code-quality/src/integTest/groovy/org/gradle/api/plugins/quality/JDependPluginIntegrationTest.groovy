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
package org.gradle.api.plugins.quality

import org.gradle.integtests.fixtures.WellBehavedPluginTest
import static org.hamcrest.Matchers.containsString

class JDependPluginIntegrationTest extends WellBehavedPluginTest {
    def setup() {
        writeBuildFile()
    }

    @Override
    String getMainTask() {
        return "check"
    }

    def "analyze code"() {
        goodCode()

        expect:
        succeeds("check")
        file("build/reports/jdepend/main.xml").assertContents(containsString("org.gradle.Class1"))
        file("build/reports/jdepend/test.xml").assertContents(containsString("org.gradle.Class1Test"))
    }

    def "is incremental"() {
        given:
        goodCode()

        expect:
        succeeds("jdependMain") && ":jdependMain" in nonSkippedTasks
        succeeds(":jdependMain") && ":jdependMain" in skippedTasks

        when:
        file("build/reports/jdepend/main.xml").delete()

        then:
        succeeds("jdependMain") && ":jdependMain" in nonSkippedTasks
    }

    def "cannot generate multiple reports"() {
        given:
        buildFile << """
            jdependMain.reports {
                xml.enabled true
                text.enabled true
            }
        """

        and:
        goodCode()

        expect:
        fails "jdependMain"

        failure.assertHasCause "JDepend tasks can only have one report enabled"
    }

    def "can generate text reports"() {
        given:
        buildFile << """
            jdependMain.reports {
                xml.enabled false
                text.enabled true
            }
        """

        and:
        goodCode()

        when:
        run "jdependMain"

        then:
        file("build/reports/jdepend/main.txt").exists()
    }

    def "can't generate no reports"() {
        given:
        buildFile << """
            jdependMain.reports {
                xml.enabled false
                text.enabled false
            }
        """

        and:
        goodCode()

        expect:
        fails "jdependMain"

        and:
        failure.assertHasCause "JDepend tasks must have one report enabled"
    }

    private goodCode() {
        file("src/main/java/org/gradle/Class1.java") <<
                "package org.gradle; class Class1 { public boolean is() { return true; } }"
        file("src/test/java/org/gradle/Class1Test.java") <<
                "package org.gradle; class Class1Test { public boolean is() { return true; } }"
    }

    private void writeBuildFile() {
        file("build.gradle") << """
apply plugin: "java"
apply plugin: "jdepend"

repositories {
    mavenCentral()
}
        """
    }
}
