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
package org.gradle.internal.concurrent

import org.gradle.test.fixtures.concurrent.ConcurrentSpec

import java.util.concurrent.TimeUnit

class DefaultExecutorFactoryTest extends ConcurrentSpec {

    def factory = new DefaultExecutorFactory()

    def cleanup() {
        factory.stop()
    }

    def stopBlocksUntilAllJobsAreComplete() {
        given:
        def action1 = {
            thread.block()
            instant.completed1
        }
        def action2 = {
            thread.block()
            instant.completed2
        }

        when:
        async {
            def executor = factory.create('test')
            executor.execute(action1)
            executor.execute(action2)
            executor.stop()
            instant.stopped
        }

        then:
        instant.stopped > instant.completed1
        instant.stopped > instant.completed2
    }

    def factoryStopBlocksUntilAllJobsAreComplete() {
        given:
        def action1 = {
            thread.block()
            instant.completed1
        }
        def action2 = {
            thread.block()
            instant.completed2
        }

        when:
        async {
            factory.create("1").execute(action1)
            factory.create("2").execute(action2)
            factory.stop()
            instant.stopped
        }

        then:
        instant.stopped > instant.completed1
        instant.stopped > instant.completed2
    }

    public void cannotStopExecutorFromAnExecutorThread() {
        when:
        def executor = factory.create('<display-name>')
        def action = {
            executor.stop()
        }
        executor.execute(action)
        executor.stop()

        then:
        IllegalStateException e = thrown()
        e.message == 'Cannot stop this executor from an executor thread.'
    }

    def stopThrowsExceptionOnTimeout() {
        def action = {
            thread.block()
        }

        when:
        def executor = factory.create('<display-name>')
        executor.execute(action)
        operation.stop {
            executor.stop(200, TimeUnit.MILLISECONDS)
        }

        then:
        IllegalStateException e = thrown()
        e.message == 'Timeout waiting for concurrent jobs to complete.'

        and:
        operation.stop.duration in approx(200)
    }

    def stopRethrowsFirstExecutionException() {
        given:
        def failure1 = new RuntimeException()
        def runnable1 = {
            instant.broken1
            throw failure1
        }

        def failure2 = new RuntimeException()
        def runnable2 = {
            instant.broken2
            throw failure2
        }

        when:
        def executor = factory.create('test')
        executor.execute(runnable1)
        thread.blockUntil.broken1
        executor.execute(runnable2)
        thread.blockUntil.broken2

        then:
        noExceptionThrown()

        when:
        executor.stop()

        then:
        def ex = thrown(RuntimeException)
        ex.is(failure1)
    }
}
