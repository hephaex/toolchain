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

import org.apache.commons.lang.RandomStringUtils
import org.gradle.nativebinaries.language.cpp.fixtures.RequiresInstalledToolChain
import org.gradle.nativebinaries.language.cpp.fixtures.app.HelloWorldApp
import org.gradle.nativebinaries.language.cpp.fixtures.app.WindowsResourceHelloWorldApp
import org.gradle.test.fixtures.file.TestFile
import spock.lang.Ignore

import static org.gradle.nativebinaries.language.cpp.fixtures.ToolChainRequirement.VisualCpp

@RequiresInstalledToolChain(VisualCpp)
class WindowsResourcesIntegrationTest extends AbstractLanguageIntegrationTest {

    HelloWorldApp helloWorldApp = new WindowsResourceHelloWorldApp()

    def "user receives a reasonable error message when resource compilation fails"() {
        given:
        buildFile << """
             executables {
                 main {}
             }
         """

        and:
        file("src/main/rc/broken.rc") << """
        #include <stdio.h>

        NOT A VALID RESOURCE
"""

        expect:
        fails "mainExecutable"
        failure.assertHasDescription("Execution failed for task ':compileMainExecutableMainRc'.");
        failure.assertHasCause("Windows resource compiler failed; see the error output for details.")
    }

    def "can create resources-only shared library"() {
        given:
        buildFile << """
            executables {
                main {}
            }
            libraries {
                resources {
                    binaries.all {
                        linker.args "/noentry", "/machine:x86"
                    }
                }
            }
            sources {
                main.cpp.lib libraries.resources
            }
"""

        and:
        file("src/resources/rc/resources.rc") << """
            #include "resources.h"

            STRINGTABLE
            {
                IDS_HELLO, "Hello!"
            }
        """

        and:
        file("src/resources/headers/resources.h") << """
            #define IDS_HELLO    111
        """

        and:
        file("src/main/cpp/main.cpp") << """
            #include <iostream>
            #include <windows.h>
            #include <string>
            #include "resources.h"

            std::string LoadStringFromResource(UINT stringID)
            {
                HMODULE instance = LoadLibraryEx("resources.dll", NULL, LOAD_LIBRARY_AS_DATAFILE);
                WCHAR * pBuf = NULL;
                int len = LoadStringW(instance, stringID, reinterpret_cast<LPWSTR>(&pBuf), 0);
                std::wstring wide = std::wstring(pBuf, len);
                return std::string(wide.begin(), wide.end());
            }

            int main() {
                std::string hello = LoadStringFromResource(IDS_HELLO);
                std::cout << hello;
                return 0;
            }
        """

        when:
        run "installMainExecutable"

        then:
        resourceOnlyLibrary("build/binaries/resourcesSharedLibrary/resources").assertExists()
        installation("build/install/mainExecutable").exec().out == "Hello!"
    }

    @Ignore
    def "windows resources compiler can use long file paths"() {
        // windows can't handle a path up to 260 characters
        // we create a project path that is ~180 characters to end up
        // with a path for the compiled resources.res > 260 chars
        def projectPathOffset = 180 - testDirectory.getAbsolutePath().length()
        def nestedProjectPath = RandomStringUtils.randomAlphanumeric(projectPathOffset-10) + "/123456789"

        setup:
        def deepNestedProjectFolder = file(nestedProjectPath)
        executer.usingProjectDirectory(deepNestedProjectFolder)
        def TestFile buildFile = deepNestedProjectFolder.file("build.gradle")
        buildFile << helloWorldApp.pluginScript
        buildFile << helloWorldApp.extraConfiguration
        buildFile << """
            executables {
                main {}
            }
        """

        and:
        helloWorldApp.writeSources(file("$nestedProjectPath/src/main"));

        expect:
        // this test is just for verifying explicitly the behaviour of the windows resource compiler
        // that's why we explicitly trigger this task instead of main.
        succeeds "mainExecutable"
    }
}
