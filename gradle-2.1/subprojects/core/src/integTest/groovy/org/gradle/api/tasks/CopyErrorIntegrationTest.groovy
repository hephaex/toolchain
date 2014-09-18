/*
 * Copyright 2010 the original author or authors.
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

import org.gradle.integtests.fixtures.AbstractIntegrationTest
import org.gradle.integtests.fixtures.executer.ExecutionFailure
import org.gradle.test.fixtures.file.TestFile
import org.gradle.util.PreconditionVerifier
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class CopyErrorIntegrationTest extends AbstractIntegrationTest {
    @Rule public PreconditionVerifier verifier = new PreconditionVerifier()

    @Test
    public void givesReasonableErrorMessageWhenPathCannotBeConverted() {
        file('src/thing.txt').createFile()

        testFile('build.gradle') << '''
            task copy(type: Copy) {
                from('src') {
                    into project.repositories
                }
                into 'dest'
            }
'''

        ExecutionFailure failure = inTestDirectory().withTasks('copy').runWithFailure()
        failure.assertHasCause("""Cannot convert the provided notation to a String: [].
The following types/formats are supported:
  - String or CharSequence instances e.g. 'some/path'
  - Boolean values e.g. true, Boolean.TRUE
  - Number values e.g. 42, 3.14
  - A File instance
  - A Closure that returns any supported value.
  - A Callable that returns any supported value.""")
    }

    @Test
    @Requires(TestPrecondition.SYMLINKS)
    public void reportsSymLinkWhichPointsToNothing() {
        TestFile link = testFile('src/file')
        link.createLink(testFile('missing'))

        Assert.assertFalse(link.isDirectory())
        Assert.assertFalse(link.isFile())
        Assert.assertFalse(link.exists())

        testFile('build.gradle') << '''
            task copy(type: Copy) {
                from 'src'
                into 'dest'
            }
'''

        ExecutionFailure failure = inTestDirectory().withTasks('copy').runWithFailure()
        failure.assertHasDescription("Could not list contents of '${link}'.")
    }

    @Test
    @Requires(TestPrecondition.FILE_PERMISSIONS)
    public void reportsUnreadableSourceDir() {
        TestFile dir = testFile('src').createDir()
        def oldPermissions = dir.permissions
        dir.permissions = '-w-r--r--'

        try {
            Assert.assertTrue(dir.isDirectory())
            Assert.assertTrue(dir.exists())
            Assert.assertFalse(dir.canRead())
            Assert.assertTrue(dir.canWrite())

            testFile('build.gradle') << '''
                task copy(type: Copy) {
                    from 'src'
                    into 'dest'
                }
    '''

            ExecutionFailure failure = inTestDirectory().withTasks('copy').runWithFailure()
            failure.assertHasDescription("Could not list contents of directory '${dir}' as it is not readable.")
        } finally {
            dir.permissions = oldPermissions
        }
    }
}
