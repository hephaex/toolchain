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

import org.gradle.initialization.BuildLayoutParameters
import org.gradle.internal.service.ServiceRegistry
import org.gradle.launcher.daemon.client.DaemonClientServices
import org.gradle.launcher.daemon.configuration.DaemonParameters
import org.gradle.launcher.daemon.registry.DaemonRegistry
import org.gradle.logging.LoggingServiceRegistry

class DaemonLogsAnalyzer {

    private List<File> daemonLogs
    private File daemonBaseDir
    private ServiceRegistry services

    DaemonLogsAnalyzer(File daemonBaseDir) {
        this.daemonBaseDir = daemonBaseDir
        assert daemonBaseDir.listFiles().length == 1
        def daemonFiles = daemonBaseDir.listFiles()[0].listFiles()
        daemonLogs = daemonFiles.findAll { it.name.endsWith('.log') }
        DaemonParameters daemonParameters = new DaemonParameters(new BuildLayoutParameters())
        daemonParameters.setBaseDir(daemonBaseDir)
        services = new DaemonClientServices(LoggingServiceRegistry.newEmbeddableLogging(), daemonParameters, new ByteArrayInputStream(new byte[0]))
    }

    List<TestableDaemon> getDaemons() {
        return daemonLogs.collect { new TestableDaemon(it, registry) }
    }

    TestableDaemon getDaemon() {
        def daemons = getDaemons()
        assert daemons.size() == 1: "Expected only a single daemon."
        daemons[0]
    }

    DaemonRegistry getRegistry() {
        services.get(DaemonRegistry)
    }
}