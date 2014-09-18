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

package org.gradle.integtests.tooling.r21

import org.gradle.integtests.tooling.fixture.TargetGradleVersion
import org.gradle.integtests.tooling.fixture.ToolingApiSpecification
import org.gradle.integtests.tooling.fixture.ToolingApiVersion
import org.gradle.test.fixtures.ConcurrentTestUtil
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.ResultHandler
import spock.lang.Ignore

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ToolingApiVersion(">=2.1")
@TargetGradleVersion(">=1.0-milestone-8")
class PreCancellationCrossVersionSpec extends ToolingApiSpecification {
//    final appender = new TestAppender()
//    @Rule ConfigureLogging logging = new ConfigureLogging(appender)

    def setup() {
        // in-process call does not support cancelling (yet)
        toolingApi.isEmbedded = false
        settingsFile << '''
rootProject.name = 'cancelling'
'''
    }

    @Ignore
    @TargetGradleVersion("<2.1 >=1.0-milestone-8")
    def "cancel with older provider issues warning only"() {
        def marker = file("warning.txt")
        buildFile << """
task t << {
    println "waiting"
    def marker = file('${marker.toURI()}')
    long timeout = System.currentTimeMillis() + 10000
    while (!marker.file && System.currentTimeMillis() < timeout) { Thread.sleep(200) }
    if (!marker.file) { throw new RuntimeException("Timeout waiting for marker file") }
    println "finished"
}
"""
        def cancel = GradleConnector.newCancellationTokenSource()
        def resultHandler = new TestResultHandler(false)
        def output = new TestOutputStream()

        when:
        withConnection { ProjectConnection connection ->
            def build = connection.newBuild()
            build.forTasks('t')
                .withCancellationToken(cancel.token())
                .setStandardOutput(output)
            build.run(resultHandler)
            ConcurrentTestUtil.poll(10) { assert output.toString().contains("waiting") }
            cancel.cancel()
            marker.text = 'go!'
            resultHandler.finished()
        }

        then:
//        appender.toString().contains('Note: Version of Gradle provider does not support cancellation.')
        resultHandler.failure == null
        output.toString().contains("finished")
    }

    class TestResultHandler implements ResultHandler<Object> {
        final latch = new CountDownLatch(1)
        final boolean expectFailure
        def failure

        TestResultHandler() {
            this(true)
        }

        TestResultHandler(boolean expectFailure) {
            this.expectFailure = expectFailure
        }

        void onComplete(Object result) {
            latch.countDown()
        }

        void onFailure(GradleConnectionException failure) {
            this.failure = failure
            latch.countDown()
        }

        def finished() {
            latch.await(10, TimeUnit.SECONDS)
            assert (failure != null) == expectFailure
        }
    }

    class TestOutputStream extends OutputStream {
        final buffer = new ByteArrayOutputStream()

        @Override
        void write(int b) throws IOException {
            synchronized (buffer) {
                buffer.write(b)
            }
        }

        @Override
        String toString() {
            synchronized (buffer) {
                return buffer.toString()
            }
        }
    }
}
