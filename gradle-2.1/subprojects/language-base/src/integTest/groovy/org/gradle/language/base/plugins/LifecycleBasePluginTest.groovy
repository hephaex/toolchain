/*
 * Copyright 2014 the original author or authors.
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
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Delete
import org.gradle.util.TestUtil
import spock.lang.Specification

import static org.gradle.api.tasks.TaskDependencyMatchers.dependsOn
import static org.hamcrest.Matchers.instanceOf

class LifecycleBasePluginTest extends Specification {
    private final Project project = TestUtil.createRootProject()

    public void createsTasksAndAppliesMappings() {
        when:
        project.plugins.apply(LifecycleBasePlugin)

        then:
        def clean = project.tasks[LifecycleBasePlugin.CLEAN_TASK_NAME]
        clean instanceOf(Delete)
        clean dependsOn()
        clean.targetFiles.files == [project.buildDir] as Set

        and:
        def assemble = project.tasks[LifecycleBasePlugin.ASSEMBLE_TASK_NAME]
        assemble instanceOf(DefaultTask)
    }

    public void addsACleanRule() {
        given:
        Task test = project.task('test')
        test.outputs.files(project.buildDir)

        when:
        project.plugins.apply(LifecycleBasePlugin)

        then:
        Task cleanTest = project.tasks['cleanTest']
        cleanTest instanceOf(Delete)
        cleanTest.delete == [test.outputs.files] as Set
    }

    public void cleanRuleIsCaseSensitive() {
        given:
        project.task('testTask')
        project.task('12')

        when:
        project.plugins.apply(LifecycleBasePlugin)

        then:
        project.tasks.findByName('cleantestTask') == null
        project.tasks.findByName('cleanTesttask') == null
        project.tasks.findByName('cleanTestTask') instanceof Delete
        project.tasks.findByName('clean12') instanceof Delete
    }
}
