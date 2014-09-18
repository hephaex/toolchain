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

package org.gradle.api.plugins

import org.gradle.api.reporting.components.ComponentReport
import org.gradle.api.tasks.diagnostics.*
import org.gradle.configuration.Help
import org.gradle.util.TestUtil
import spock.lang.Specification

import static org.gradle.configuration.ImplicitTasksConfigurer.*

class HelpTasksPluginSpec extends Specification {
    final project = TestUtil.createRootProject()

    def "adds help tasks"() {
        when:
        project.apply(plugin: 'help-tasks')

        then:
        hasHelpTask(HELP_TASK, Help)
        hasHelpTask(DEPENDENCY_INSIGHT_TASK, DependencyInsightReportTask)
        hasHelpTask(DEPENDENCIES_TASK, DependencyReportTask)
        hasHelpTask(PROJECTS_TASK, ProjectReportTask)
        hasHelpTask(TASKS_TASK, TaskReportTask)
        hasHelpTask(PROPERTIES_TASK, PropertyReportTask)
        hasHelpTask(COMPONENTS_TASK, ComponentReport)
    }

    def "tasks description reflects whether project has sub-projects or not"() {
        given:
        def child = TestUtil.createChildProject(project, "child")

        when:
        project.apply(plugin: 'help-tasks')
        child.apply(plugin: 'help-tasks')

        then:
        project.tasks[TASKS_TASK].description == "Displays the tasks runnable from root project 'test' (some of the displayed tasks may belong to subprojects)."
        child.tasks[TASKS_TASK].description == "Displays the tasks runnable from project ':child'."
    }

    def "configures tasks for java plugin"() {
        when:
        project.apply(plugin: 'help-tasks')

        then:
        !project.tasks[DEPENDENCY_INSIGHT_TASK].configuration

        when:
        project.plugins.apply(JavaPlugin)

        then:
        project.tasks[DEPENDENCY_INSIGHT_TASK].configuration == project.configurations.compile
    }

    private hasHelpTask(String name, Class type) {
        def task = project.tasks.getByName(name)
        assert type.isInstance(task)
        assert task.group == HELP_GROUP
        assert task.impliesSubProjects
        if (type != Help.class) {
            assert task.description.contains(project.name)
        }
        return task
    }
}
