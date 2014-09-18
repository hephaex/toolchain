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

package org.gradle.testing

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.DefaultTestExecutionResult
import org.gradle.integtests.fixtures.TestResources
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.junit.Rule

class TestEnvironmentIntegrationTest extends AbstractIntegrationSpec {
    @Rule public final TestResources resources = new TestResources(temporaryFolder)

    @Requires(TestPrecondition.NOT_JDK_IBM)
    def canRunTestsWithCustomSystemClassLoader() {
        when:
        run 'test'

        then:
        def result = new DefaultTestExecutionResult(testDirectory)
        result.assertTestClassesExecuted('org.gradle.JUnitTest')
        result.testClass('org.gradle.JUnitTest').assertTestPassed('mySystemClassLoaderIsUsed')
    }

    @Requires(TestPrecondition.NOT_JDK_IBM)
    def canRunTestsWithCustomSystemClassLoaderAndJavaAgent() {
        when:
        run 'test'

        then:
        def result = new DefaultTestExecutionResult(testDirectory)
        result.assertTestClassesExecuted('org.gradle.JUnitTest')
        result.testClass('org.gradle.JUnitTest').assertTestPassed('mySystemClassLoaderIsUsed')
    }

    def canRunTestsWithCustomSecurityManager() {
        when:
        run 'test'

        then:
        def result = new DefaultTestExecutionResult(testDirectory)
        result.assertTestClassesExecuted('org.gradle.JUnitTest')
        result.testClass('org.gradle.JUnitTest').assertTestPassed('mySecurityManagerIsUsed')
    }

    @Requires(TestPrecondition.JDK7_OR_EARLIER)
    def canRunTestsWithJMockitLoadedWithJavaAgent() {
        when:
        run 'test'

        then:
        def result = new DefaultTestExecutionResult(testDirectory)
        result.assertTestClassesExecuted('org.gradle.JMockitTest')
        result.testClass('org.gradle.JMockitTest').assertTestPassed('testOk')
    }
}
