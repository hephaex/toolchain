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
package org.gradle.initialization.layout

import org.gradle.StartParameter
import spock.lang.Specification

class BuildLayoutConfigurationTest extends Specification {
    def "uses specified settings script"() {
        def startParameter = new StartParameter()
        def settingsFile = new File("settings.gradle")
        startParameter.settingsFile = settingsFile
        def config = new BuildLayoutConfiguration(startParameter)

        expect:
        config.settingsFile == settingsFile.canonicalFile
    }

    def "uses default settings file when none specified"() {
        def startParameter = new StartParameter()
        def config = new BuildLayoutConfiguration(startParameter)

        expect:
        config.settingsFile == null
    }
}
