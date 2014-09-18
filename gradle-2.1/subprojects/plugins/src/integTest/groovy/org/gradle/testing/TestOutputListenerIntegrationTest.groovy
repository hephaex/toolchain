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
package org.gradle.testing

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.TestResources
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

@Issue("GRADLE-1009")
public class TestOutputListenerIntegrationTest extends AbstractIntegrationSpec {
    @Rule public final TestResources resources = new TestResources(temporaryFolder)

    @Before
    public void before() {
        executer.noExtraLogging()
    }

    @Test
    def "can use standard output listener for tests"() {
        given:
        def test = file("src/test/java/SomeTest.java")
        test << """
import org.junit.*;

public class SomeTest {
    @Test public void showsOutputWhenPassing() {
        System.out.println("out passing");
        System.err.println("err passing");
        Assert.assertTrue(true);
    }

    @Test public void showsOutputWhenFailing() {
        System.out.println("out failing");
        System.err.println("err failing");
        Assert.assertTrue(false);
    }
}
"""
        def buildFile = file('build.gradle')
        buildFile << """
apply plugin: 'java'
repositories { mavenCentral() }
dependencies { testCompile "junit:junit:4.11" }

test.addTestOutputListener(new VerboseOutputListener(logger: project.logger))

def removeMe = new RemoveMeListener()
test.addTestOutputListener(removeMe)
test.removeTestOutputListener(removeMe)

class VerboseOutputListener implements TestOutputListener {

    def logger

    public void onOutput(TestDescriptor descriptor, TestOutputEvent event) {
        logger.lifecycle(descriptor.toString() + " " + event.destination + " " + event.message);
    }
}

class RemoveMeListener implements TestOutputListener {
    public void onOutput(TestDescriptor descriptor, TestOutputEvent event) {
        println "remove me!"
    }
}
"""

        when:
        def failure = executer.withTasks('test').runWithFailure()

        then:
        failure.output.contains('test showsOutputWhenPassing(SomeTest) StdOut out passing')
        failure.output.contains('test showsOutputWhenFailing(SomeTest) StdOut out failing')
        failure.output.contains('test showsOutputWhenPassing(SomeTest) StdErr err passing')
        failure.output.contains('test showsOutputWhenFailing(SomeTest) StdErr err failing')

        !failure.output.contains("remove me!")
    }

    @Test
    def "can register output listener at gradle level and using onOutput method"() {
        given:
        def test = file("src/test/java/SomeTest.java")
        test << """
import org.junit.*;

public class SomeTest {
    @Test public void foo() {
        System.out.println("message from foo");
    }
}
"""
        def buildFile = file('build.gradle')
        buildFile << """
apply plugin: 'java'
repositories { mavenCentral() }
dependencies { testCompile "junit:junit:4.11" }

test.onOutput { descriptor, event ->
    logger.lifecycle("first: " + event.message)
}

gradle.addListener(new VerboseOutputListener(logger: project.logger))

class VerboseOutputListener implements TestOutputListener {

    def logger

    public void onOutput(TestDescriptor descriptor, TestOutputEvent event) {
        logger.lifecycle("second: " + event.message);
    }
}
"""

        when:
        def result = executer.withTasks('test').run()

        then:
        result.output.contains('first: message from foo')
        result.output.contains('second: message from foo')
    }

    @Test
    def "shows standard streams configured via closure"() {
        given:
        def test = file("src/test/java/SomeTest.java")
        test << """
import org.junit.*;

public class SomeTest {
    @Test public void foo() {
        System.out.println("message from foo");
    }
}
"""
        def buildFile = file('build.gradle')
        buildFile << """
apply plugin: 'java'
repositories { mavenCentral() }
dependencies { testCompile "junit:junit:4.11" }

test.testLogging {
    showStandardStreams = true
}
"""

        when:
        def result = executer.withTasks('test').withArguments('-i').run()

        then:
        result.output.contains('message from foo')
    }

    @Test
    def "shows standard stream also for testNG"() {
        given:
        def test = file("src/test/java/SomeTest.java")
        test << """
import org.testng.*;
import org.testng.annotations.*;

public class SomeTest {
    @Test public void foo() {
        System.out.println("output from foo");
        System.err.println("error from foo");
    }
}
"""

        def buildFile = file('build.gradle')
        buildFile << """
apply plugin: 'java'
repositories { mavenCentral() }
dependencies { testCompile 'org.testng:testng:6.3.1' }

test {
    useTestNG()
    testLogging.showStandardStreams = true
}
"""
        when: "run with quiet"
        def result = executer.withArguments("-q").withTasks('test'). run()
        then:
        !result.output.contains('output from foo')

        when: "run with lifecycle"
        result = executer.noExtraLogging().withTasks('cleanTest', 'test').run()

        then:
        result.output.contains('output from foo')
        result.output.contains('error from foo')
    }
}
