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
package org.gradle.testing.testng

import org.gradle.integtests.fixtures.*
import org.junit.Rule
import org.junit.Test

public class SampleTestNGIntegrationTest extends AbstractIntegrationTest {

    @Rule public final Sample sample = new Sample(testDirectoryProvider)

    @Test @UsesSample('testing/testng/suitexmlbuilder')
    public void suiteXmlBuilder() {
        executer.inDirectory(sample.dir).withTasks('clean', 'test').run()

        def result = new DefaultTestExecutionResult(sample.dir)
        result.assertTestClassesExecuted('org.gradle.testng.UserImplTest')
        result.testClass('org.gradle.testng.UserImplTest').assertTestsExecuted('testOkFirstName')
        result.testClass('org.gradle.testng.UserImplTest').assertTestPassed('testOkFirstName')
    }

    @Test @UsesSample('testing/testng/java-jdk14-passing')
    public void javaJdk14Passing() {
        executer.inDirectory(sample.dir).withTasks('clean', 'test').run()

        def result = new DefaultTestExecutionResult(sample.dir)
        result.assertTestClassesExecuted('org.gradle.OkTest')
        result.testClass('org.gradle.OkTest').assertTestPassed('passingTest')
    }
    
    @Test @UsesSample('testing/testng/java-jdk15-passing')
    public void javaJdk15Passing() {
        executer.inDirectory(sample.dir).withTasks('clean', 'test').run()

        def result = new TestNGExecutionResult(sample.dir)
        result.assertTestClassesExecuted('org.gradle.OkTest', 'org.gradle.ConcreteTest', 'org.gradle.SuiteSetup', 'org.gradle.SuiteCleanup', 'org.gradle.TestSetup', 'org.gradle.TestCleanup')
        result.testClass('org.gradle.OkTest').assertTestsExecuted('passingTest', 'expectedFailTest')
        result.testClass('org.gradle.OkTest').assertTestPassed('passingTest')
        result.testClass('org.gradle.OkTest').assertTestPassed('expectedFailTest')
        result.testClass('org.gradle.ConcreteTest').assertTestsExecuted('ok', 'alsoOk')
        result.testClass('org.gradle.ConcreteTest').assertTestPassed('ok')
        result.testClass('org.gradle.ConcreteTest').assertTestPassed('alsoOk')
        result.testClass('org.gradle.SuiteSetup').assertConfigMethodPassed('setupSuite')
        result.testClass('org.gradle.SuiteCleanup').assertConfigMethodPassed('cleanupSuite')
        result.testClass('org.gradle.TestSetup').assertConfigMethodPassed('setupTest')
        result.testClass('org.gradle.TestCleanup').assertConfigMethodPassed('cleanupTest')
    }
}