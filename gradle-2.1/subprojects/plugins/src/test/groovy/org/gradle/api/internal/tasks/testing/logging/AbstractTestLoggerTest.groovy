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

package org.gradle.api.internal.tasks.testing.logging

import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.TestStyledTextOutputFactory
import org.gradle.util.TextUtil

import spock.lang.Specification

class AbstractTestLoggerTest extends Specification {
    static String sep = TextUtil.platformLineSeparator

    StyledTextOutputFactory textOutputFactory = new TestStyledTextOutputFactory()
    AbstractTestLogger logger

    def rootDescriptor = new SimpleTestDescriptor(name: "Test Run", composite: true)
    def workerDescriptor = new SimpleTestDescriptor(name: "Gradle Worker 2", composite: true, parent: rootDescriptor)
    def outerSuiteDescriptor = new SimpleTestDescriptor(name: "com.OuterSuiteClass", composite: true, parent: workerDescriptor)
    def innerSuiteDescriptor = new SimpleTestDescriptor(name: "com.InnerSuiteClass", composite: true, parent: outerSuiteDescriptor)
    def classDescriptor = new SimpleTestDescriptor(name: "foo.bar.TestClass", composite: true, parent: innerSuiteDescriptor)
    def methodDescriptor = new SimpleTestDescriptor(name: "testMethod", className: "foo.bar.TestClass", parent: classDescriptor)

    def "log test run event"() {
        createLogger(LogLevel.INFO)

        when:
        logger.logEvent(rootDescriptor, TestLogEvent.STARTED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}Test Run STARTED${sep}"
    }

    def "log Gradle worker event"() {
        createLogger(LogLevel.INFO)

        when:
        logger.logEvent(workerDescriptor, TestLogEvent.STARTED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}Gradle Worker 2 STARTED${sep}"
    }

    def "log outer suite event"() {
        createLogger(LogLevel.ERROR)

        when:
        logger.logEvent(outerSuiteDescriptor, TestLogEvent.STARTED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{ERROR}${sep}com.OuterSuiteClass STARTED${sep}"
    }

    def "log inner suite event"() {
        createLogger(LogLevel.QUIET)

        when:
        logger.logEvent(innerSuiteDescriptor, TestLogEvent.PASSED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{QUIET}${sep}com.OuterSuiteClass > com.InnerSuiteClass {identifier}PASSED{normal}$sep"

    }

    def "log test class event"() {
        createLogger(LogLevel.WARN)

        when:
        logger.logEvent(classDescriptor, TestLogEvent.SKIPPED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{WARN}${sep}com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass {info}SKIPPED{normal}${sep}"
    }

    def "log test method event"() {
        createLogger(LogLevel.LIFECYCLE)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.FAILED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{LIFECYCLE}${sep}com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass > testMethod {failure}FAILED{normal}${sep}"
    }

    def "log standard out event"() {
        createLogger(LogLevel.INFO)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.STANDARD_OUT, "this is a${sep}standard out${sep}event")

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass > testMethod STANDARD_OUT${sep}this is a${sep}standard out${sep}event"
    }

    def "log standard error event"() {
        createLogger(LogLevel.DEBUG)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.STANDARD_ERROR, "this is a${sep}standard error${sep}event")

        then:
        textOutputFactory.toString() == "{TestEventLogger}{DEBUG}${sep}com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass > testMethod STANDARD_ERROR${sep}this is a${sep}standard error${sep}event"
    }

    def "log test method event with lowest display granularity"() {
        createLogger(LogLevel.INFO, 0)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.FAILED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}Test Run > Gradle Worker 2 > com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass > testMethod {failure}FAILED{normal}${sep}"
    }

    def "log test method event with highest display granularity"() {
        createLogger(LogLevel.INFO, -1)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.FAILED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}testMethod {failure}FAILED{normal}${sep}"
    }

    def "logging of atomic test whose parent isn't the test class includes test class name"() {
        createLogger(LogLevel.INFO)
        methodDescriptor.parent = innerSuiteDescriptor

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.STARTED)

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}com.OuterSuiteClass > com.InnerSuiteClass > foo.bar.TestClass.testMethod STARTED${sep}"
    }

    def "logs header just once per batch of events with same type and for same test"() {
        createLogger(LogLevel.INFO)

        when:
        logger.logEvent(methodDescriptor, TestLogEvent.STANDARD_OUT, "event 1")
        logger.logEvent(methodDescriptor, TestLogEvent.STANDARD_OUT, "event 2")

        then:
        textOutputFactory.toString() == "{TestEventLogger}{INFO}${sep}com.OuterSuiteClass > com.InnerSuiteClass\
 > foo.bar.TestClass > testMethod STANDARD_OUT${sep}event 1{TestEventLogger}{INFO}event 2"
    }

    void createLogger(LogLevel level, int displayGranularity = 2) {
        logger = new AbstractTestLogger(textOutputFactory, level, displayGranularity) {}
    }
}
