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

package org.gradle.launcher.daemon.testing

import org.gradle.internal.os.OperatingSystem
import org.gradle.launcher.daemon.context.DaemonContext
import org.gradle.launcher.daemon.logging.DaemonMessages
import org.gradle.launcher.daemon.registry.DaemonRegistry
import org.gradle.process.internal.ExecHandleBuilder

import java.util.regex.Matcher
import java.util.regex.Pattern

class TestableDaemon {

    final DaemonContext context
    final String logContent
    private final DaemonRegistry registry

    TestableDaemon(File daemonLog, DaemonRegistry registry) {
        this.logContent = daemonLog.text
        this.context = DaemonContextParser.parseFrom(logContent)
        this.registry = registry
    }

    void waitUntilIdle() {
        def expiry = System.currentTimeMillis() + 20000
        while (expiry > System.currentTimeMillis()) {
            if (registry.idle.find { it.context.pid == context.pid } != null) {
                return
            }
            Thread.sleep(200)
        }
        throw new AssertionError("Timeout waiting for daemon with pid ${context.pid} to become idle.")
    }

    void kill() {
        println "Killing daemon with pid: $context.pid"
        def output = new ByteArrayOutputStream()
        def e = new ExecHandleBuilder()
                .commandLine(killArgs(context.pid))
                .redirectErrorStream()
                .setStandardOutput(output)
                .workingDir(new File(".").absoluteFile) //does not matter
                .build()
        e.start()
        def result = e.waitForFinish()
        result.rethrowFailure()
    }

    private static Object[] killArgs(Long pid) {
        if (pid == null) {
            throw new RuntimeException("Unable to force kill the daemon because provided pid is null!")
        }
        if (OperatingSystem.current().isUnix()) {
            return ["kill", "-9", pid]
        } else if (OperatingSystem.current().isWindows()) {
            return ["taskkill.exe", "/F", "/T", "/PID", pid]
        }
        else {
            throw new RuntimeException("This implementation does not know how to forcefully kill the daemon on os: " + OperatingSystem.current())
        }
    }

    @SuppressWarnings("FieldName")
    enum State {
        busy, idle
    }

    boolean isIdle() {
        getStates()[-1] == State.idle
    }

    boolean isBusy() {
        !isIdle()
    }

    List<State> getStates() {
        def states = new LinkedList<State>()
        logContent.eachLine {
            if (it.contains(DaemonMessages.DAEMON_IDLE)) {
                states << State.idle
            } else if (it.contains(DaemonMessages.DAEMON_BUSY)) {
                states << State.busy
            }
        }
        states
    }

    int getPort() {
        Pattern pattern = Pattern.compile("^.*" + DaemonMessages.ADVERTISING_DAEMON + ".*port:(\\d+).*",
                Pattern.MULTILINE + Pattern.DOTALL);

        Matcher matcher = pattern.matcher(logContent);
        assert matcher.matches(): "Unable to find daemon address in the daemon log. Daemon: $context"

        try {
            return Integer.parseInt(matcher.group(1))
        } catch (NumberFormatException e) {
            throw new RuntimeException("Unexpected format of the port number found in the daemon log. Daemon: $context")
        }
    }
}