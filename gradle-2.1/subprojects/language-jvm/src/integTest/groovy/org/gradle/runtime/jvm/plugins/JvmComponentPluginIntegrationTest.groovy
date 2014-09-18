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

package org.gradle.runtime.jvm.plugins
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.test.fixtures.archive.JarTestFixture

class JvmComponentPluginIntegrationTest extends AbstractIntegrationSpec {
    def "does not create library or binaries when not configured"() {
        when:
        buildFile << """
        apply plugin: 'jvm-component'
        task check << {
            assert jvm.libraries.empty
            assert binaries.empty
        }
"""
        then:
        succeeds "check"

        and:
        !file("build").exists()
    }

    def "defines jvm library and binary model objects and lifecycle task"() {
        when:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myLib
        }
    }

    task check << {
        assert jvm.libraries.size() == 1
        def myLib = jvm.libraries.myLib
        assert myLib.name == 'myLib'
        assert myLib == jvm.libraries['myLib']
        assert myLib instanceof JvmLibrarySpec

        assert sources.size() == 1
        assert sources.myLib instanceof FunctionalSourceSet

        assert binaries.size() == 1
        assert myLib.binaries as Set == binaries as Set

        def myLibJar = (binaries as List)[0]
        assert myLibJar instanceof JarBinarySpec
        assert myLibJar.name == 'myLibJar'
        assert myLibJar.displayName == "jar 'myLib:jar'"

        def binaryTask = tasks['myLibJar']
        assert binaryTask.group == 'build'
        assert binaryTask.description == "Assembles jar 'myLib:jar'."
        assert myLibJar.buildTask == binaryTask

        def jarTask = tasks['createMyLibJar']
        assert jarTask instanceof Jar
        assert jarTask.group == null
        assert jarTask.description == "Creates the binary file for jar 'myLib:jar'."
    }
"""
        then:
        succeeds "check"
    }

    def "creates empty jar when no language sources available"() {
        given:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myJvmLib
        }
    }
"""
        when:
        succeeds "myJvmLibJar"

        then:
        executed ":createMyJvmLibJar", ":myJvmLibJar"

        and:
        def jar = new JarTestFixture(file("build/jars/myJvmLibJar/myJvmLib.jar"))
        jar.hasDescendants()
    }

    def "can configure jvm binary"() {
        given:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myJvmLib
        }
    }
    binaries.withType(JarBinarySpec) { jar ->
        jar.jarFile = file("\${project.buildDir}/bin/\${jar.name}.bin")
    }
"""
        when:
        succeeds "myJvmLibJar"

        then:
        file("build/bin/myJvmLibJar.bin").assertExists()
    }

    def "can configure jvm binary for component"() {
        given:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myJvmLib {
                binaries.all { jar ->
                    jar.jarFile = file("\${project.buildDir}/bin/\${jar.name}.bin")
                }
            }
        }
    }
    binaries.withType(JarBinarySpec) { jar ->
    }
"""
        when:
        succeeds "myJvmLibJar"

        then:
        file("build/bin/myJvmLibJar.bin").assertExists()
    }

    def "can specify additional builder tasks for binary"() {
        given:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myJvmLib
        }
    }
    binaries.all { binary ->
        def logTask = project.tasks.create(binary.namingScheme.getTaskName("log")) {
            println "Constructing binary: \${binary.displayName}"
        }
        binary.builtBy(logTask)
    }
"""
        when:
        succeeds "myJvmLibJar"

        then:
        executed ":createMyJvmLibJar", ":logMyJvmLibJar", ":myJvmLibJar"

        and:
        output.contains("Constructing binary: jar 'myJvmLib:jar'")
    }

    def "can define multiple jvm libraries in single project"() {
        when:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myLibOne
            myLibTwo
        }
    }

    task check << {
        assert jvm.libraries.size() == 2
        assert jvm.libraries.myLibOne instanceof JvmLibrarySpec
        assert jvm.libraries.myLibTwo instanceof JvmLibrarySpec

        assert binaries.size() == 2
        assert binaries.myLibOneJar.library == jvm.libraries.myLibOne
        assert binaries.myLibTwoJar.library == jvm.libraries.myLibTwo
    }
"""
        then:
        succeeds "check"
    }

    def "can build multiple jvm libraries in single project"() {
        given:
        buildFile << """
    apply plugin: 'jvm-component'

    jvm {
        libraries {
            myLibOne
            myLibTwo
        }
    }
"""
        when:
        succeeds "myLibOneJar"

        then:
        executed ":createMyLibOneJar", ":myLibOneJar"
        notExecuted ":myLibTwoJar"

        when:
        succeeds "assemble"

        then:
        executed ":createMyLibOneJar", ":myLibOneJar", ":createMyLibTwoJar", ":myLibTwoJar"
    }
}