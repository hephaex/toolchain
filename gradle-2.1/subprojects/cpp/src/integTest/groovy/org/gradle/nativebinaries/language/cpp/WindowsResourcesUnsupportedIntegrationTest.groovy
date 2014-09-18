/*
 * Copyright 2013 the original author or authors.
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
package org.gradle.nativebinaries.language.cpp

import org.gradle.nativebinaries.language.cpp.fixtures.AbstractInstalledToolChainIntegrationSpec
import org.gradle.nativebinaries.language.cpp.fixtures.RequiresInstalledToolChain
import org.gradle.nativebinaries.language.cpp.fixtures.ToolChainRequirement
import org.gradle.nativebinaries.language.cpp.fixtures.app.CppHelloWorldApp
import org.gradle.nativebinaries.language.cpp.fixtures.app.HelloWorldApp
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition

class WindowsResourcesUnsupportedIntegrationTest extends AbstractInstalledToolChainIntegrationSpec {

    HelloWorldApp helloWorldApp = new CppHelloWorldApp()

    @Requires(TestPrecondition.NOT_WINDOWS)
    def "resource files are ignored on unsupported platforms"() {
        given:
        buildFile << """
            apply plugin: 'cpp'
            apply plugin: 'windows-resources'

            executables {
                main {}
            }
         """

        and:
        helloWorldApp.writeSources(file("src/main"))
        file("src/main/rc/broken.rc") << """
        #include <stdio.h>

        NOT A VALID RESOURCE
"""

        when:
        run "mainExecutable"

        then:
        !executedTasks.contains(":compileMainExecutableMainRc")
    }

    @Requires(TestPrecondition.WINDOWS)
    @RequiresInstalledToolChain(ToolChainRequirement.GccCompatible)
    def "reasonable error message when attempting to compile resource files with unsupported tool chain"() {
        given:
        buildFile << """
            apply plugin: 'cpp'
            apply plugin: 'windows-resources'

            executables {
                main {}
            }
         """

        and:
        helloWorldApp.writeSources(file("src/main"))
        file("src/main/rc/broken.rc") << """
        #include <stdio.h>

        NOT A VALID RESOURCE
"""

        when:
        fails "mainExecutable"

        then:
        failure.assertHasDescription("Execution failed for task ':compileMainExecutableMainRc'.")
        failure.assertHasCause("Windows resource compiler is not available")
    }
}

