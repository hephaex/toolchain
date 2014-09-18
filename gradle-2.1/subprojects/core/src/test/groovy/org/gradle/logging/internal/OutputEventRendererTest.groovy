/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.logging.internal

import org.gradle.api.Action
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.StandardOutputListener
import org.gradle.util.RedirectStdOutAndErr
import org.junit.Rule
import org.gradle.internal.nativeplatform.console.ConsoleMetaData

class OutputEventRendererTest extends OutputSpecification {
    @Rule public final RedirectStdOutAndErr outputs = new RedirectStdOutAndErr()
    private final ConsoleStub console = new ConsoleStub()
    private final ConsoleMetaData metaData = Mock()
    private final Action<OutputEventRenderer> consoleConfigureAction = Mock()
    private OutputEventRenderer renderer

    def setup() {
        renderer = new OutputEventRenderer(consoleConfigureAction)
        renderer.configure(LogLevel.INFO)
    }

    def rendersLogEventsToStdOut() {
        when:
        renderer.addStandardOutputAndError()
        renderer.onOutput(event('message', LogLevel.INFO))

        then:
        outputs.stdOut.readLines() == ['message']
        outputs.stdErr == ''
    }

    def rendersErrorLogEventsToStdErr() {
        when:
        renderer.addStandardOutputAndError()
        renderer.onOutput(event('message', LogLevel.ERROR))

        then:
        outputs.stdOut == ''
        outputs.stdErr.readLines() == ['message']
    }

    def rendersLogEventsWhenLogLevelIsDebug() {
        def listener = new TestListener()

        when:
        renderer.configure(LogLevel.DEBUG)
        renderer.addStandardOutputListener(listener)
        renderer.onOutput(event(tenAm, 'message', LogLevel.INFO))

        then:
        listener.value.readLines() == ['10:00:00.000 [INFO] [category] message']
    }

    def rendersLogEventsToStdOutandStdErrWhenLogLevelIsDebug() {
        when:
        renderer.configure(LogLevel.DEBUG)
        renderer.addStandardOutputAndError()
        renderer.onOutput(event(tenAm, 'info', LogLevel.INFO))
        renderer.onOutput(event(tenAm, 'error', LogLevel.ERROR))

        then:
        outputs.stdOut.readLines() == ['10:00:00.000 [INFO] [category] info']
        outputs.stdErr.readLines() == ['10:00:00.000 [ERROR] [category] error']
    }

    def rendersLogEventsToStdOutListener() {
        def listener = new TestListener()

        when:
        renderer.addStandardOutputListener(listener)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        listener.value.readLines() == ['info']
    }

    def doesNotRenderLogEventsToRemovedStdOutListener() {
        def listener = new TestListener()

        when:
        renderer.addStandardOutputListener(listener)
        renderer.removeStandardOutputListener(listener)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        listener.value == ''
    }

    def rendersLogEventsToStdOutListenerWhenLogLevelIsDebug() {
        def listener = new TestListener()

        when:
        renderer.configure(LogLevel.DEBUG)
        renderer.addStandardOutputListener(listener)
        renderer.onOutput(event(tenAm, 'message', LogLevel.INFO))

        then:
        listener.value.readLines() == ['10:00:00.000 [INFO] [category] message']
    }

    def rendersErrorLogEventsToStdErrListener() {
        def listener = new TestListener()

        when:
        renderer.addStandardErrorListener(listener)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        listener.value.readLines() == ['error']
    }

    def doesNotRenderLogEventsToRemovedStdErrListener() {
        def listener = new TestListener()

        when:
        renderer.addStandardErrorListener(listener)
        renderer.removeStandardErrorListener(listener)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        listener.value == ''
    }

    def rendersLogEventsToStdErrListenerWhenLogLevelIsDebug() {
        def listener = new TestListener()

        when:
        renderer.configure(LogLevel.DEBUG)
        renderer.addStandardErrorListener(listener)
        renderer.onOutput(event(tenAm, 'message', LogLevel.ERROR))

        then:
        listener.value.readLines() == ['10:00:00.000 [ERROR] [category] message']
    }

    def forwardsOutputEventsToListener() {
        OutputEventListener listener = Mock()
        LogEvent ignored = event('ignored', LogLevel.DEBUG)
        LogEvent event = event('message', LogLevel.INFO)

        when:
        renderer.configure(LogLevel.INFO)
        renderer.addOutputEventListener(listener)
        renderer.onOutput(ignored)
        renderer.onOutput(event)

        then:
        1 * listener.onOutput(event)
        0 * listener._
    }

    def doesNotForwardOutputEventsToRemovedListener() {
        OutputEventListener listener = Mock()
        LogEvent event = event('message', LogLevel.INFO)

        when:
        renderer.configure(LogLevel.INFO)
        renderer.addOutputEventListener(listener)
        renderer.removeOutputEventListener(listener)
        renderer.onOutput(event)

        then:
        0 * listener._
    }

    def rendersProgressEvents() {
        when:
        renderer.addStandardOutputAndError()
        renderer.onOutput(start(loggingHeader: 'description'))
        renderer.onOutput(complete('status'))

        then:
        outputs.stdOut.readLines() == ['description status']
        outputs.stdErr == ''
    }

    def doesNotRendersProgressEventsForLogLevelQuiet() {
        when:
        renderer.addStandardOutputAndError()
        renderer.configure(LogLevel.QUIET)
        renderer.onOutput(start('description'))
        renderer.onOutput(complete('status'))

        then:
        outputs.stdOut == ''
        outputs.stdErr == ''
    }

    def rendersLogEventsWhenStdOutAndStdErrAreConsole() {
        renderer.addConsole(console, true, true, metaData)

        when:
        renderer.onOutput(start(loggingHeader: 'description'))
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))
        renderer.onOutput(complete('status'))

        then:
        console.value.readLines() == ['description', 'info', '{error}error', '{normal}description {progressstatus}status{normal}']
    }

    def rendersLogEventsWhenOnlyStdOutIsConsole() {
        renderer.addConsole(console, true, false, metaData)

        when:
        renderer.onOutput(start(loggingHeader: 'description'))
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))
        renderer.onOutput(complete('status'))

        then:
        console.value.readLines() == ['description', 'info', 'description {progressstatus}status{normal}']
    }

    def rendersLogEventsWhenOnlyStdErrIsConsole() {
        renderer.addConsole(console, false, true, metaData)

        when:
        renderer.onOutput(start('description'))
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))
        renderer.onOutput(complete('status'))

        then:
        console.value.readLines() == ['{error}error', '{normal}']
    }

    def rendersLogEventsInConsoleWhenLogLevelIsDebug() {
        renderer.configure(LogLevel.DEBUG)
        renderer.addConsole(console, true, true, metaData)

        when:
        renderer.onOutput(event(tenAm, 'info', LogLevel.INFO))
        renderer.onOutput(event(tenAm, 'error', LogLevel.ERROR))

        then:
        console.value.readLines() == ['10:00:00.000 [INFO] [category] info', '{error}10:00:00.000 [ERROR] [category] error', '{normal}']
    }

    def attachesConsoleWhenStdOutAndStdErrAreAttachedToConsole() {
        when:
        renderer.addStandardOutputAndError()
        renderer.addConsole(console, true, true, metaData)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        console.value.readLines() == ['info', '{error}error', '{normal}']
        outputs.stdOut == ''
        outputs.stdErr == ''
    }

    def attachesConsoleWhenOnlyStdOutIsAttachedToConsole() {
        when:
        renderer.addStandardOutputAndError()
        renderer.addConsole(console, true, false, metaData)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        console.value.readLines() == ['info']
        outputs.stdOut == ''
        outputs.stdErr.readLines() == ['error']
    }

    def attachesConsoleWhenOnlyStdErrIsAttachedToConsole() {
        when:
        renderer.addStandardOutputAndError()
        renderer.addConsole(console, false, true, metaData)
        renderer.onOutput(event('info', LogLevel.INFO))
        renderer.onOutput(event('error', LogLevel.ERROR))

        then:
        console.value.readLines() == ['{error}error', '{normal}']
        outputs.stdOut.readLines() == ['info']
        outputs.stdErr == ''
    }
}

class TestListener implements StandardOutputListener {
    private final StringWriter writer = new StringWriter();

    def getValue() {
        return writer.toString()
    }

    public void onOutput(CharSequence output) {
        writer.append(output);
    }
}

