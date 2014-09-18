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

package org.gradle.performance

import spock.lang.Unroll

import static org.gradle.performance.measure.Duration.millis

class IdeIntegrationPerformanceTest extends AbstractPerformanceTest {
    @Unroll("Project '#testProject' eclipse")
    def "eclipse"() {
        given:
        runner.testId = "eclipse $testProject"
        runner.testProject = testProject
        runner.tasksToRun = ['eclipse']
        runner.maxExecutionTimeRegression = maxExecutionTimeRegression
        runner.targetVersions = ['1.0', '1.4', '1.8', '1.12', 'last']

        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()

        where:
        testProject       | maxExecutionTimeRegression
        "small"           | millis(800)
        "multi"           | millis(500)
        "lotDependencies" | millis(500)
    }

    @Unroll("Project '#testProject' idea")
    def "idea"() {
        given:
        runner.testId = "idea $testProject"
        runner.testProject = testProject
        runner.tasksToRun = ['idea']
        runner.maxExecutionTimeRegression = maxExecutionTimeRegression
        runner.targetVersions = ['1.0', '1.8', '1.10', '1.12', 'last']

        when:
        def result = runner.run()

        then:
        result.assertCurrentVersionHasNotRegressed()

        where:
        testProject       | maxExecutionTimeRegression
        "small"           | millis(800)
        "multi"           | millis(500)
        "lotDependencies" | millis(500)
    }
}