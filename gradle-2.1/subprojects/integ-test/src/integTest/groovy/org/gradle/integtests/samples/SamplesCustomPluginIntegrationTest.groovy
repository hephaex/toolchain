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
package org.gradle.integtests.samples

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.DefaultTestExecutionResult
import org.gradle.integtests.fixtures.Sample
import org.junit.Rule

class SamplesCustomPluginIntegrationTest extends AbstractIntegrationSpec {
    @Rule public final Sample sample = new Sample(temporaryFolder, 'customPlugin')

    def getProducerDir() {
        return sample.dir.file('plugin')
    }

    def getConsumerDir() {
        return sample.dir.file('consumer')
    }

    public void canTestPluginAndTaskImplementation() {
        when:
        executer.inDirectory(producerDir).withTasks('check').run()

        then:
        def result = new DefaultTestExecutionResult(producerDir)
        result.assertTestClassesExecuted('org.gradle.GreetingTaskTest', 'org.gradle.GreetingPluginTest')
    }

    public void canPublishAndUsePluginAndTestImplementations() {
        given:
        executer.inDirectory(producerDir).withTasks('uploadArchives').run()

        when:
        def result = executer.inDirectory(consumerDir).withTasks('greeting').run()

        then:
        result.output.contains('howdy!')

        when:
        result = executer.inDirectory(consumerDir).withTasks('hello').run()

        then:
        result.output.contains('hello from GreetingTask')
    }
}
