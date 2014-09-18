/*
 * Copyright 2009 the original author or authors.
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



package org.gradle.tooling.internal.provider

import org.gradle.TaskExecutionRequest
import org.gradle.launcher.cli.converter.PropertiesToStartParameterConverter
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.tooling.internal.protocol.InternalLaunchable
import org.gradle.tooling.internal.provider.connection.ProviderOperationParameters
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.util.Matchers.isSerializable
import static org.hamcrest.MatcherAssert.assertThat

class ConfiguringBuildActionTest extends Specification {
    @Rule TestNameTestDirectoryProvider temp

    def "allows configuring the start parameter with build arguments"() {
        when:
        def action = new ConfiguringBuildAction(arguments: ['-PextraProperty=foo', '-m'])
        def start = action.configureStartParameter()

        then:
        start.projectProperties['extraProperty'] == 'foo'
        start.dryRun
    }

    def "can overwrite project dir via build arguments"() {
        given:
        def projectDir = temp.createDir('projectDir')

        when:
        def action = new ConfiguringBuildAction(projectDirectory: projectDir, arguments: ['-p', 'otherDir'])
        def start = action.configureStartParameter()

        then:
        start.projectDir == new File(projectDir, "otherDir")
    }

    def "can overwrite gradle user home via build arguments"() {
        given:
        def dotGradle = temp.createDir('.gradle')
        def projectDir = temp.createDir('projectDir')

        when:
        def action = new ConfiguringBuildAction(gradleUserHomeDir: dotGradle, projectDirectory: projectDir, 
                arguments: ['-g', 'otherDir'])
        def start = action.configureStartParameter()

        then:
        start.gradleUserHomeDir == new File(projectDir, "otherDir")
    }

    def "can overwrite searchUpwards via build arguments"() {
        when:
        def action = new ConfiguringBuildAction(arguments: ['-u'])
        def start = action.configureStartParameter()

        then:
        !start.searchUpwards
    }

    def "searchUpwards configured directly on the action wins over the command line setting"() {
        when:
        def action = new ConfiguringBuildAction(arguments: ['-u'], searchUpwards: true)
        def start = action.configureStartParameter()

        then:
        start.searchUpwards
    }

    def "the start parameter is configured from properties"() {
        given:
        def converter = Mock(PropertiesToStartParameterConverter)
        def action = new ConfiguringBuildAction(properties: [foo: 'bar'])

        when:
        action.configureStartParameter(converter)

        then:
        1 * converter.convert([foo: 'bar'], _)
    }

    def "is serializable"() {
        expect:
        assertThat(new ConfiguringBuildAction({} as ProviderOperationParameters, null, [foo: 'bar']), isSerializable())
    }

    abstract class LaunchableExecutionRequest implements InternalLaunchable, TaskExecutionRequest {}

    def "accepts launchables from consumer"() {
        given:
        def selector = Mock(LaunchableExecutionRequest)
        _ * selector.args >> ['myTask']
        _ * selector.projectPath >> ':child'

        ProviderOperationParameters providerParameters = Mock(ProviderOperationParameters)
        _ * providerParameters.launchables >> [selector]
        _ * providerParameters.tasks >> []
        def action = new ConfiguringBuildAction(providerParameters, null, [:])

        when:
        def start = action.configureStartParameter()

        then:
        start.taskRequests.size() == 1
    }
}
