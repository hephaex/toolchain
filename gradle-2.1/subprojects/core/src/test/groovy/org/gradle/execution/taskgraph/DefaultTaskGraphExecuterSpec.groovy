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

package org.gradle.execution.taskgraph

import org.gradle.api.Task
import org.gradle.api.internal.TaskInternal
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.internal.tasks.TaskStateInternal
import org.gradle.api.tasks.TaskDependency
import org.gradle.initialization.BuildCancellationToken
import org.gradle.listener.ListenerBroadcast
import org.gradle.listener.ListenerManager
import spock.lang.Specification

class DefaultTaskGraphExecuterSpec extends Specification {
    def cancellationToken = Mock(BuildCancellationToken)
    def project = Stub(ProjectInternal)
    def listenerManager = Stub(ListenerManager) {
        _ * createAnonymousBroadcaster(_) >> { Class cl -> new ListenerBroadcast(cl) }
    }
    def taskExecuter = new DefaultTaskGraphExecuter(listenerManager, new DefaultTaskPlanExecutor(), cancellationToken)

    def testStopsExecutionWhenCancelled() {
        def a = task("a")
        def b = task("b")

        given:
        cancellationToken.cancellationRequested >>> [false, true]

        when:
        taskExecuter.addTasks([a, b])
        taskExecuter.execute()

        then:
        RuntimeException e = thrown()
        e.message == 'Build cancelled.'

        and:
        1 * a.executeWithoutThrowingTaskFailure()
        0 * b.executeWithoutThrowingTaskFailure()
    }

    def task(String name) {
        def mock = Mock(TaskInternal)
        _ * mock.name >> name
        _ * mock.project >> project
        _ * mock.state >> Stub(TaskStateInternal) {
            getFailure() >> null
        }
        _ * mock.taskDependencies >> Stub(TaskDependency)
        _ * mock.finalizedBy >> Stub(TaskDependency)
        _ * mock.mustRunAfter >> Stub(TaskDependency)
        _ * mock.shouldRunAfter >> Stub(TaskDependency)
        _ * mock.compareTo(_) >> { Task t -> name.compareTo(t.name) }
        return mock
    }
}
