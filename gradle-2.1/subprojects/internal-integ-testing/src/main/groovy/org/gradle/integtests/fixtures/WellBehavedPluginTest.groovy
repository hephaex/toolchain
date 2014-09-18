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

package org.gradle.integtests.fixtures

import java.util.regex.Pattern

abstract class WellBehavedPluginTest extends AbstractIntegrationSpec {

    String getPluginId() {
        def matcher = Pattern.compile("(\\w+)Plugin(GoodBehaviour)?(Integ(ration)?)?Test").matcher(getClass().simpleName)
        if (matcher.matches()) {
            return matcher.group(1).toLowerCase()
        }
        throw new UnsupportedOperationException("Cannot determine plugin id from class name '${getClass().simpleName}.")
    }
    
    String getMainTask() {
        return "assemble"
    }

    def "plugin does not force creation of build dir during configuration"() {
        given:
        applyPlugin()

        when:
        run "tasks"

        then:
        !file("build").exists()
    }

    def "plugin can build with empty project"() {
        given:
        applyPlugin()

        expect:
        succeeds mainTask
    }

    protected applyPlugin(File target = buildFile) {
        target << "apply plugin: '${getPluginId()}'\n"
    }
}
