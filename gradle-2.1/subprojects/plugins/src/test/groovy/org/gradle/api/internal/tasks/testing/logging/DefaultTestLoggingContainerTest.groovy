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
import org.gradle.api.tasks.testing.logging.TestStackTraceFilter
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.internal.reflect.DirectInstantiator

import spock.lang.Specification
import spock.lang.Unroll
import org.gradle.api.Action
import org.gradle.api.tasks.testing.logging.TestLogging

class DefaultTestLoggingContainerTest extends Specification {
    DefaultTestLoggingContainer container = new DefaultTestLoggingContainer(new DirectInstantiator())

    def "sets defaults for level ERROR"() {
        def logging = container.get(LogLevel.ERROR)

        expect:
        hasUnchangedDefaults(logging)
    }

    def "sets defaults for level QUIET"() {
        def logging = container.get(LogLevel.QUIET)

        expect:
        hasUnchangedDefaults(logging)
    }

    def "sets defaults for level WARN"() {
        def logging = container.get(LogLevel.WARN)

        expect:
        hasUnchangedDefaults(logging)
    }

    def "sets defaults for level LIFECYCLE"() {
        def logging = container.get(LogLevel.LIFECYCLE)

        expect:
        logging.events == [TestLogEvent.FAILED] as Set
        logging.minGranularity == -1
        logging.maxGranularity == -1
        logging.exceptionFormat == TestExceptionFormat.SHORT
        logging.showExceptions
        logging.showCauses
        logging.stackTraceFilters == [TestStackTraceFilter.TRUNCATE] as Set
    }

    def "sets defaults for level INFO"() {
        def logging = container.get(LogLevel.INFO)

        expect:
        logging.events == [TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR] as Set
        logging.minGranularity == -1
        logging.maxGranularity == -1
        logging.exceptionFormat == TestExceptionFormat.FULL
        logging.showExceptions
        logging.showCauses
        logging.stackTraceFilters == [TestStackTraceFilter.TRUNCATE] as Set
    }

    def "sets defaults for level DEBUG"() {
        def logging = container.get(LogLevel.DEBUG)

        expect:
        logging.events == EnumSet.allOf(TestLogEvent)
        logging.minGranularity == 0
        logging.maxGranularity == -1
        logging.exceptionFormat == TestExceptionFormat.FULL
        logging.showExceptions
        logging.showCauses
        logging.stackTraceFilters == [] as Set
    }

    def "implicitly configures level LIFECYCLE"() {
        def logging = container.get(LogLevel.LIFECYCLE)
        assert logging.showExceptions

        when:
        container.showExceptions = false

        then:
        !logging.showExceptions
    }

    @Unroll
    def "allows to explicitly configure level #level"(LogLevel level) {
        def logging = container.get(level)
        def configMethod = level.toString().toLowerCase()

        when:
        container."$configMethod"(new Action<TestLogging>() {
            void execute(TestLogging l) {
                l.showExceptions = false
            }
        })

        then:
        !logging.showExceptions

        where:
        level << LogLevel.values()
    }

    private void hasUnchangedDefaults(logging) {
        assert logging.events == [] as Set
        assert logging.minGranularity == -1
        assert logging.maxGranularity == -1
        logging.exceptionFormat == TestExceptionFormat.FULL
        logging.showExceptions
        logging.showCauses
        assert logging.stackTraceFilters == [TestStackTraceFilter.TRUNCATE] as Set
    }
}
