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

import org.gradle.api.Incubating
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.reporting.components.ComponentReport
import org.gradle.api.tasks.diagnostics.*
import org.gradle.configuration.Help

import static org.gradle.configuration.ImplicitTasksConfigurer.*

@Incubating
class HelpTasksPlugin implements Plugin<ProjectInternal> {

    void apply(ProjectInternal project) {
        project.tasks.addPlaceholderAction(HELP_TASK) {
            project.tasks.create(name: HELP_TASK, type: Help) {
                description = "Displays a help message"
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(PROJECTS_TASK) {
            project.tasks.create(name: PROJECTS_TASK, type: ProjectReportTask) {
                description = "Displays the sub-projects of $project."
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(TASKS_TASK) {
            project.tasks.create(name: TASKS_TASK, type: TaskReportTask) {
                if (project.childProjects.isEmpty()) {
                    description = "Displays the tasks runnable from $project."
                } else {
                    description = "Displays the tasks runnable from $project (some of the displayed tasks may belong to subprojects)."
                }
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(PROPERTIES_TASK) {
            project.tasks.create(name: PROPERTIES_TASK, type: PropertyReportTask) {
                description = "Displays the properties of $project."
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(DEPENDENCY_INSIGHT_TASK) {
            project.tasks.create(name: DEPENDENCY_INSIGHT_TASK, type: DependencyInsightReportTask) { task ->
                description = "Displays the insight into a specific dependency in $project."
                group = HELP_GROUP
                project.plugins.withType(JavaPlugin) {
                    task.configuration = project.configurations.compile
                }
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(DEPENDENCIES_TASK) {
            project.tasks.create(name: DEPENDENCIES_TASK, type: DependencyReportTask) {
                description = "Displays all dependencies declared in $project."
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }

        project.tasks.addPlaceholderAction(COMPONENTS_TASK) {
            project.tasks.create(name: COMPONENTS_TASK, type: ComponentReport) {
                description = "Displays the components produced by $project."
                group = HELP_GROUP
                impliesSubProjects = true
            }
        }
    }
}