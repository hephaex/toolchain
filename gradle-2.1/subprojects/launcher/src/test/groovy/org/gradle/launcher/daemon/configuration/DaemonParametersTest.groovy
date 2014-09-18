/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.launcher.daemon.configuration

import org.gradle.initialization.BuildLayoutParameters
import spock.lang.Specification

import static java.lang.Boolean.parseBoolean

class DaemonParametersTest extends Specification {
    final DaemonParameters parameters = new DaemonParameters(new BuildLayoutParameters())

    def "has reasonable default values"() {
        expect:
        assertDefaultValues()
    }

    def "uses default values when no specific gradle properties provided"() {
        expect:
        assertDefaultValues()
    }

    void assertDefaultValues() {
        assert !parameters.enabled
        assert parameters.idleTimeout == DaemonParameters.DEFAULT_IDLE_TIMEOUT
        def baseDir = new File(new BuildLayoutParameters().getGradleUserHomeDir(), "daemon")
        assert parameters.baseDir == baseDir
        assert parameters.systemProperties.isEmpty()
        assert parameters.effectiveJvmArgs.containsAll(parameters.DEFAULT_JVM_ARGS)
        assert parameters.effectiveJvmArgs.size() == parameters.DEFAULT_JVM_ARGS.size() + 1 + 3 // + 1 because effective JVM args contains -Dfile.encoding, +3 for locale props
        assert parameters.idleTimeout == DaemonParameters.DEFAULT_IDLE_TIMEOUT
    }

    def "configuring jvmargs replaces the defaults"() {
        when:
        parameters.setJvmArgs(["-Xmx17m"])

        then:
        parameters.effectiveJvmArgs.each { assert !parameters.DEFAULT_JVM_ARGS.contains(it) }
    }

    def "can configure debug mode"() {
        when:
        parameters.setDebug(parseBoolean(flag))

        then:
        parameters.effectiveJvmArgs.contains("-Xdebug") == parseBoolean(flag)
        parameters.effectiveJvmArgs.contains("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005") == parseBoolean(flag)

        where:
        flag << ["true", "false"]
    }
}
