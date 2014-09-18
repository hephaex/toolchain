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

package org.gradle.api.tasks

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.test.fixtures.file.TestFile
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition

import static org.junit.Assert.assertTrue

class CopyPermissionsIntegrationTest extends AbstractIntegrationSpec {

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "file permissions are preserved in copy action"() {
        given:
        def testSourceFile = file(testFileName)
        testSourceFile << "test file content"
        testSourceFile.mode = mode
        and:
        buildFile << """
        task copy(type: Copy) {
            from "${testSourceFile.absolutePath}"
            into ("build/tmp")
        }
        """

        when:
        run "copy"
        then:
        file("build/tmp/${testFileName}").mode == mode
        where:
        mode << [0746, 0746]
        testFileName << ["reference.txt", "\u0627\u0644\u0627\u0655\u062F\u0627\u0631\u0629.txt"]
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "file permissions can be modfied with eachFile closure"() {
        given:
        def testSourceFile = file("reference.txt") << 'test file"'
        testSourceFile.mode = 0746
        and:
        buildFile << """
            task copy(type: Copy) {
                from "reference.txt"
                eachFile {
		            it.setMode(0755)
	            }
                into ("build/tmp")
            }
            """
        when:
        run "copy"
        then:
        file("build/tmp/reference.txt").mode == 0755
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "directory permissions are preserved in copy action"() {
        given:
        TestFile parent = getTestDirectory().createDir("testparent")
        TestFile child = parent.createDir("testchild")
        child.file("reference.txt") << "test file"

        child.mode = mode
        and:
        buildFile << """
            task copy(type: Copy) {
                from "testparent"
                into ("build/tmp")
            }
            """
        when:
        run "copy"
        then:
        file("build/tmp/testchild").mode == mode
        where:
        mode << [0755, 0776]
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "fileMode can be modified in copy task"() {
        given:

        file("reference.txt") << 'test file"'
        file("reference.txt").mode = 0777
        and:
        buildFile << """
             task copy(type: Copy) {
                 from "reference.txt"
                 into ("build/tmp")
                 fileMode = $mode
             }
            """
        when:
        run "copy"

        then:
        file("build/tmp/reference.txt").mode == mode

        where:
        mode << [0755, 0776]
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "fileMode can be modified in copy action"() {
        given:
        file("reference.txt") << 'test file"'

        and:
        buildFile << """
            task copy << {
                copy {
                    from 'reference.txt'
                    into 'build/tmp'
                    fileMode = $mode
                }
            }
            """

        when:
        run "copy"

        then:
        file("build/tmp/reference.txt").mode == mode
        where:
        mode << [0755, 0776]

    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "dirMode can be modified in copy task"() {
        given:
        TestFile parent = getTestDirectory().createDir("testparent")
        TestFile child = parent.createDir("testchild")
        child.file("reference.txt") << "test file"

        child.mode = 0777
        and:
        buildFile << """
            task copy(type: Copy) {
                from "testparent"
                into ("build/tmp")
                dirMode = $mode
            }
            """
        when:
        run "copy"
        then:
        file("build/tmp/testchild").mode == mode
        where:
        mode << [0755, 0776]
    }

    @Requires(TestPrecondition.WINDOWS)
    def "file permissions are not preserved on OS without permission support"() {
        given:
        def testSourceFile = file("reference.txt") << 'test file"'
        assertTrue testSourceFile.setReadOnly()
        and:
        buildFile << """
        task copy(type: Copy) {
            from "reference.txt"
            into ("build/tmp")
        }
        """
        when:
        withDebugLogging()
        run "copy"
        then:
        def testTargetFile = file("build/tmp/reference.txt")
        testTargetFile.exists()
        testTargetFile.canWrite()
    }


}