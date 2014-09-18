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
package org.gradle.integtests.tooling

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.IntegrationTestHint
import org.gradle.integtests.fixtures.Sample
import org.gradle.integtests.fixtures.UsesSample
import org.gradle.integtests.fixtures.executer.ExecutionResult
import org.gradle.integtests.fixtures.executer.GradleContextualExecuter
import org.gradle.integtests.fixtures.executer.IntegrationTestBuildContext
import org.gradle.util.TextUtil
import org.junit.Rule

class SamplesToolingApiIntegrationTest extends AbstractIntegrationSpec {

    @Rule public final Sample sample = new Sample(temporaryFolder)

    private IntegrationTestBuildContext buildContext

    @UsesSample('toolingApi/eclipse')
    def "can use tooling API to build Eclipse model"() {
        tweakProject()

        when:
        def result = run()

        then:
        result.output.contains("gradle-tooling-api-")
        result.output.contains("src/main/java")
    }

    @UsesSample('toolingApi/runBuild')
    def "can use tooling API to run tasks"() {
        tweakProject()

        when:
        def result = run()

        then:
        result.output.contains("Welcome to Gradle")
    }

    @UsesSample('toolingApi/idea')
    def "can use tooling API to build IDEA model"() {
        tweakProject()

        when:
        run()

        then:
        noExceptionThrown()
    }

    @UsesSample('toolingApi/model')
    def "can use tooling API to build general model"() {
        tweakProject()

        when:
        def result = run()

        then:
        result.output.contains("Project: model")
        result.output.contains("    build")
    }

    @UsesSample('toolingApi/customModel')
    def "can use tooling API to register custom model"() {
        tweakPluginProject(sample.dir.file('plugin'))
        tweakProject(sample.dir.file('tooling'))

        when:
        run('publish', sample.dir.file('plugin'))
        def result = run('run', sample.dir.file('tooling'))

        then:
        result.output.contains("   :a")
        result.output.contains("   :b")
        result.output.contains("   :c")
        noExceptionThrown()
    }

    private void tweakProject(File projectDir = sample.dir) {
        // Inject some additional configuration into the sample build script
        def buildFile = projectDir.file('build.gradle')
        def buildScript = buildFile.text
        def index = buildScript.indexOf('repositories {')
        assert index >= 0
        buildContext = new IntegrationTestBuildContext()
        buildScript = buildScript.substring(0, index) + """
repositories {
    maven { url "${buildContext.libsRepo.toURI()}" }
}
run {
    args = ["${TextUtil.escapeString(buildContext.gradleHomeDir.absolutePath)}", "${TextUtil.escapeString(executer.gradleUserHomeDir.absolutePath)}"]
    systemProperty 'org.gradle.daemon.idletimeout', 10000
    systemProperty 'org.gradle.daemon.registry.base', "${TextUtil.escapeString(projectDir.file("daemon").absolutePath)}"
}
""" + buildScript.substring(index)

        buildFile.text = buildScript

        // Add in an empty settings file to avoid searching up
        projectDir.file('settings.gradle').text = '// to stop search upwards'
    }

    private void tweakPluginProject(File projectDir) {
        // Inject some additional configuration into the sample build script
        def buildFile = projectDir.file('build.gradle')
        def buildScript = buildFile.text
        def index = buildScript.indexOf('publishing {')
        assert index >= 0
        buildContext = new IntegrationTestBuildContext()
        buildScript = buildScript.substring(0, index) + """
repositories {
    maven { url "${buildContext.libsRepo.toURI()}" }
}
""" + buildScript.substring(index)

        buildFile.text = buildScript

        // Add in an empty settings file to avoid searching up
        projectDir.file('settings.gradle').text = '// to stop search upwards'
    }

    private ExecutionResult run(String task = 'run', File dir = sample.dir) {
        try {
            return new GradleContextualExecuter(distribution, temporaryFolder)
                    .requireGradleHome()
                    .inDirectory(dir)
                    .withTasks(task)
                    .run()
        } catch (Exception e) {
            throw new IntegrationTestHint(e);
        }
    }
}