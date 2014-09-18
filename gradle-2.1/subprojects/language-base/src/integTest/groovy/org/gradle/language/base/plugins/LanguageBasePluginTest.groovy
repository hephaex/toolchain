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
package org.gradle.language.base.plugins
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.integtests.fixtures.WellBehavedPluginTest
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.language.base.ProjectSourceSet
import org.gradle.runtime.base.BinaryContainer
import org.gradle.runtime.base.internal.BinaryNamingScheme
import org.gradle.runtime.base.internal.DefaultBinaryContainer
import org.gradle.runtime.base.internal.BinarySpecInternal
import org.gradle.util.TestUtil

class LanguageBasePluginTest extends WellBehavedPluginTest {
    Project project = TestUtil.createRootProject()

    def setup() {
        project.plugins.apply(LanguageBasePlugin)
    }

    @Override
    String getPluginId() {
        return 'language-base'
    }

    def "adds a 'binaries' container to the project"() {
        expect:
        project.extensions.findByName("binaries") instanceof BinaryContainer
    }

    def "adds a 'sources' container to the project"() {
        expect:
        project.extensions.findByName("sources") instanceof ProjectSourceSet
    }

    def "creates a lifecycle task for each binary"() {
        def tasks = Mock(TaskContainer)
        def binaries = new DefaultBinaryContainer(new DirectInstantiator())
        def binary = Mock(BinarySpecInternal)
        def namingScheme = Mock(BinaryNamingScheme)
        def task = Mock(Task)

        when:
        binaries.add(binary)
        def rules = new LanguageBasePlugin.Rules()
        rules.createLifecycleTaskForBinary(tasks, binaries)

        then:
        binary.name >> "binaryName"
        binary.toString() >> "binary foo"
        binary.namingScheme >> namingScheme
        namingScheme.lifecycleTaskName >> "lifecycle"

        and:
        1 * tasks.create("lifecycle") >> task
        1 * task.setGroup("build")
        1 * task.setDescription("Assembles binary foo.")
        1 * binary.setBuildTask(task)
    }
}
