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

package org.gradle.api.internal.tasks.testing.junit

import org.gradle.api.internal.tasks.testing.DefaultTestClassRunInfo
import org.gradle.api.internal.tasks.testing.TestResultProcessor
import org.gradle.internal.id.LongIdGenerator
import org.gradle.logging.StandardOutputRedirector
import org.gradle.messaging.actor.TestActorFactory
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED

class JUnitTestClassProcessorTest extends Specification {
    
    @Rule TestNameTestDirectoryProvider tmp = new TestNameTestDirectoryProvider()

    def processor = Mock(TestResultProcessor)
    def spec = new JUnitSpec([] as Set, [] as Set, [] as Set)
    
    @Subject classProcessor = withSpec(spec)

    JUnitTestClassProcessor withSpec(spec) {
        new JUnitTestClassProcessor(spec, new LongIdGenerator(), new TestActorFactory(), {} as StandardOutputRedirector)
    }

    void process(Class ... clazz) {
        process(clazz*.name)
    }

    void process(Iterable<String> classNames) {
        classProcessor.startProcessing(processor)
        for (String c : classNames) {
            classProcessor.processTestClass(new DefaultTestClassRunInfo(c))
        }
        classProcessor.stop()
    }

    def executesAJUnit4TestClass() {
        when: process(ATestClass)

        then: 1 * processor.started({ it.id == 1 && it.name == ATestClass.name && it.className == ATestClass.name }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == "ok" && it.className == ATestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null }) //wondering why result type is null? Failures are notified via failure() method
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesAJUnit4TestClassWithIgnoredTest() {
        when: process(ATestClassWithIgnoredMethod)

        then: 1 * processor.started({it.id == 1}, {it.parentId == null})
        then: 1 * processor.started({ it.id == 2 && it.name == "ignored" && it.className == ATestClassWithIgnoredMethod.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == SKIPPED })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesAJUnit4TestClassWithFailedTestAssumption() {
        when: process(ATestClassWithFailedTestAssumption)

        then: 1 * processor.started({it.id == 1}, {it.parentId == null})
        then: 1 * processor.started({ it.id == 2 && it.name == "assumed" && it.className == ATestClassWithFailedTestAssumption.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == SKIPPED })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesAnIgnoredJUnit4TestClass() {
        when: process(AnIgnoredTestClass)

        then: 1 * processor.started({it.id == 1}, {it.parentId == null})
        then: 1 * processor.started({ it.id == 2 && it.name == "ignored2" && it.className == AnIgnoredTestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == SKIPPED })
        then: 1 * processor.started({ it.id == 3 && it.name == "ignored" && it.className == AnIgnoredTestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(3, { it.resultType == SKIPPED })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesAJUnit3TestClass() {
        when: process(AJunit3TestClass)

        then: 1 * processor.started({it.id == 1}, {it.parentId == null})
        then: 1 * processor.started({ it.id == 2 && it.name == "testOk" && it.className == AJunit3TestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesMultipleTestClasses() {
        when: process(ATestClass, AJunit3TestClass)

        then: 1 * processor.started({ it.id == 1 && it.name == ATestClass.name && it.className == ATestClass.name }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == "ok" && it.className == ATestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })

        then: 1 * processor.started({ it.id == 3 && it.name == AJunit3TestClass.name && it.className == AJunit3TestClass.name }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 4 && it.name == "testOk" && it.className == AJunit3TestClass.name }, { it.parentId == 3 })
        then: 1 * processor.completed(4, { it.resultType == null })
        then: 1 * processor.completed(3, { it.resultType == null })
        0 * processor._
    }

    def executesATestClassWithRunWithAnnotation() {
        when: process(ATestClassWithRunner)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == "broken" && it.className == ATestClassWithRunner.name }, { it.parentId == 1 })
        then: 1 * processor.started({ it.id == 3 && it.name == "ok" && it.className == ATestClassWithRunner.name }, { it.parentId == 1 })
        then: 1 * processor.failure(2, CustomRunner.failure)
        then: 1 * processor.completed(3, { it.resultType == null })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesATestClassWithASuiteMethod() {
        when: process(ATestClassWithSuiteMethod)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == "testOk" && it.className == AJunit3TestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.started({ it.id == 3 && it.name == "testOk" && it.className == AJunit3TestClass.name }, { it.parentId == 1 })
        then: 1 * processor.completed(3, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesATestClassWithBrokenBeforeAndAfterMethod() {
        when: process(ATestClassWithBrokenBeforeAndAfterMethod)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == 'test' && it.className == ATestClassWithBrokenBeforeAndAfterMethod.name }, { it.parentId == 1 })
        then: 1 * processor.failure(2, ATestClassWithBrokenBeforeAndAfterMethod.beforeFailure)
        then: 1 * processor.failure(2, ATestClassWithBrokenBeforeAndAfterMethod.afterFailure)
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    @Unroll
    def "#testClass reports failure"() {
        when: process(testClass)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == testMethodName && it.className == testClass.name }, { it.parentId == 1 })
        then: 1 * processor.failure(2, failure)
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._

        where:
        testClass                               |testMethodName         |failure
        ABrokenTestClass                        |'broken'           |ABrokenTestClass.failure
        ABrokenJunit3TestClass                  |'testBroken'           |ABrokenJunit3TestClass.failure
        ATestClassWithBrokenRunner              |'initializationError'  |CustomRunnerWithBrokenRunMethod.failure
        ATestClassWithUnconstructableRunner     |'initializationError'  |CustomRunnerWithBrokenConstructor.failure
        ATestClassWithBrokenBeforeClassMethod   |'classMethod'          |ATestClassWithBrokenBeforeClassMethod.failure
        ATestClassWithBrokenConstructor         |'test'                 |ATestClassWithBrokenConstructor.failure
        ATestClassWithBrokenBeforeMethod        |'test'                 |ATestClassWithBrokenBeforeMethod.failure
        ATestClassWithBrokenSuiteMethod         |'initializationError'  |ATestClassWithBrokenSuiteMethod.failure
        ATestSetUpWithBrokenSetUp               |AJunit3TestClass.name  |ATestSetUpWithBrokenSetUp.failure
    }

    def executesATestClassWithRunnerThatBreaksAfterRunningSomeTests() {
        when: process(ATestClassWithRunnerThatBreaksAfterRuningSomeTests)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })

        then: 1 * processor.started({ it.id == 2 && it.name == 'ok1' && it.className == ATestClassWithRunnerThatBreaksAfterRuningSomeTests.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })

        then: 1 * processor.started({ it.id == 3 && it.name == 'broken' && it.className == ATestClassWithRunnerThatBreaksAfterRuningSomeTests.name }, { it.parentId == 1 })
        then: 1 * processor.failure(3, CustomRunnerWithRunMethodThatBreaksAfterRunningSomeTests.failure)
        then: 1 * processor.completed(3, { it.resultType == null })

        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesATestClassWhichCannotBeLoaded() {
        String testClassName = 'org.gradle.api.internal.tasks.testing.junit.ATestClassWhichCannotBeLoaded'
        when: process([testClassName])

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == 'pass' && it.className == testClassName }, { it.parentId == 1 })
        then: 1 * processor.failure(2, _ as NoClassDefFoundError)
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def executesAJUnit3TestClassThatRenamesItself() {
        when: process(AJunit3TestThatRenamesItself)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == 'testOk' && it.className == AJunit3TestThatRenamesItself.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def "executes specific method"() {
        classProcessor = withSpec(new JUnitSpec([] as Set, [] as Set, [ATestClassWithSeveralMethods.name + ".pass"] as Set))

        when: process(ATestClassWithSeveralMethods)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.started({ it.id == 2 && it.name == "pass" && it.className == ATestClassWithSeveralMethods.name }, { it.parentId == 1 })
        then: 1 * processor.completed(2, { it.resultType == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }

    def "executes multiple specific methods"() {
        classProcessor = withSpec(new JUnitSpec([] as Set, [] as Set, [ATestClassWithSeveralMethods.name + ".pass",
                ATestClassWithSeveralMethods.name + ".pass2"] as Set))

        when: process(ATestClassWithSeveralMethods)

        then:
        1 * processor.started({ it.name == ATestClassWithSeveralMethods.name }, _)
        1 * processor.started({ it.name == "pass" && it.className == ATestClassWithSeveralMethods.name }, _)
        1 * processor.started({ it.name == "pass2" && it.className == ATestClassWithSeveralMethods.name }, _)
        0 * processor.started(_, _)
    }

    def "executes methods from multiple classes by pattern"() {
        classProcessor = withSpec(new JUnitSpec([] as Set, [] as Set, ["*Methods.*Slowly*"] as Set))

        when: process(ATestClassWithSeveralMethods, ATestClassWithSlowMethods, ATestClass)

        then:
        1 * processor.started({ it.name == ATestClassWithSeveralMethods.name }, _)
        1 * processor.started({ it.name == "passSlowly" && it.className == ATestClassWithSeveralMethods.name }, _)
        1 * processor.started({ it.name == "passSlowly2" && it.className == ATestClassWithSeveralMethods.name }, _)
        1 * processor.started({ it.name == ATestClassWithSlowMethods.name }, _)
        1 * processor.started({ it.name == "passSlowly" && it.className == ATestClassWithSlowMethods.name }, _)
        1 * processor.started({ it.name == ATestClass.name }, _)
        0 * processor.started(_, _)
    }

    def "executes no methods when method name does not match"() {
        classProcessor = withSpec(new JUnitSpec([] as Set, [] as Set, ["does not exist"] as Set))

        when: process(ATestClassWithSeveralMethods)

        then: 1 * processor.started({ it.id == 1 }, { it.parentId == null })
        then: 1 * processor.completed(1, { it.resultType == null })
        0 * processor._
    }
}
