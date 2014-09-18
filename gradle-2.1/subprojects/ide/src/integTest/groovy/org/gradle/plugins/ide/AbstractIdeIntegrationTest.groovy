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


package org.gradle.plugins.ide

import org.gradle.integtests.fixtures.AbstractIntegrationTest
import org.gradle.integtests.fixtures.executer.ExecutionResult
import org.gradle.plugins.ide.idea.IdeaModuleFixture
import org.gradle.test.fixtures.file.TestFile

abstract class AbstractIdeIntegrationTest extends AbstractIntegrationTest {
    protected ExecutionResult runTask(taskName, settingsScript = "rootProject.name = 'root'", buildScript) {
        def settingsFile = file("settings.gradle")
        settingsFile << settingsScript

        def buildFile = file("build.gradle")
        buildFile << buildScript

        return executer.usingSettingsFile(settingsFile).usingBuildScript(buildFile).withTasks(taskName).run()
    }

    protected File getFile(Map options, String filename) {
        def file = options?.project ? file(options.project, filename) : file(filename)
        if (options?.print) { println file.text }
        file
    }

    protected parseFile(Map options, String filename) {
        def file = getFile(options, filename)
        new XmlSlurper().parse(file)
    }

    protected void createJavaSourceDirs(TestFile buildFile) {
        buildFile.parentFile.file("src/main/java").createDir()
        buildFile.parentFile.file("src/main/resources").createDir()
    }

    protected ExecutionResult runIdeaTask(buildScript) {
        return runTask("idea", buildScript)
    }

    protected IdeaModuleFixture parseIml(Map options = [:], String moduleFile) {
        return new IdeaModuleFixture(parseFile(options, moduleFile))
    }

    protected parseImlFile(Map options = [:], String projectName) {
        parseFile(options, "${projectName}.iml")
    }
}
