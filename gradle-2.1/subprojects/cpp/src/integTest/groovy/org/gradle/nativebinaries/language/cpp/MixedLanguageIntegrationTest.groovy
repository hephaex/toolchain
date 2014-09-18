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

import org.gradle.nativebinaries.language.cpp.fixtures.app.HelloWorldApp
import org.gradle.nativebinaries.language.cpp.fixtures.app.MixedLanguageHelloWorldApp
import org.gradle.nativebinaries.language.cpp.fixtures.app.SourceFile

class MixedLanguageIntegrationTest extends AbstractLanguageIntegrationTest {

    HelloWorldApp helloWorldApp = new MixedLanguageHelloWorldApp(toolChain)

    def "can have all source files co-located in a common directory"() {
        given:
        buildFile << """
            executables {
                main {}
            }

            sources {
                main {
                    cpp {
                        source {
                            srcDirs "src/main/flat"
                            include "**/*.cpp"
                        }
                    }
                    c {
                        source {
                            srcDirs "src/main/flat"
                            include "**/*.c"
                        }
                        exportedHeaders {
                            srcDirs "src/main/flat"
                        }
                    }
                    asm {
                        source {
                            srcDirs "src/main/flat"
                            include "**/*.s"
                        }
                    }
                }
            }
        """

        and:
        helloWorldApp.allFiles.each { SourceFile sourceFile ->
            file("src/main/flat/${sourceFile.name}") << sourceFile.content
        }

        when:
        run "mainExecutable"

        then:
        def mainExecutable = executable("build/binaries/mainExecutable/main")
        mainExecutable.assertExists()
        mainExecutable.exec().out == helloWorldApp.englishOutput
    }

    def "build and execute program with non-conventional source layout"() {
        given:
        buildFile << """
            executables {
                main {}
            }

                        sources {
                main {
                    cpp {
                        source {
                            srcDirs "source"
                            include "**/*.cpp"
                        }
                        exportedHeaders {
                            srcDirs "source/hello", "include"
                        }
                    }
                    c {
                        source {
                            srcDirs "source", "include"
                            include "**/*.c"
                        }
                        exportedHeaders {
                            srcDirs "source/hello", "include"
                        }
                    }
                }
            }
        """
        settingsFile << "rootProject.name = 'test'"

        and:
        file("source", "hello", "hello.cpp") << """
            #include <iostream>

            void hello () {
              std::cout << "${HelloWorldApp.HELLO_WORLD}";
            }
        """

        and:
        file("source", "hello", "french", "bonjour.c") << """
            #include <stdio.h>
            #include "otherProject/bonjour.h"

            void bonjour() {
                printf("${HelloWorldApp.HELLO_WORLD_FRENCH}");
            }
        """

        and:
        file("source", "hello", "hello.h") << """
            void hello();
        """

        and:
        file("source", "app", "main", "main.cpp") << """
            #include "hello.h"
            extern "C" {
                #include "otherProject/bonjour.h"
            }

            int main () {
              hello();
              bonjour();
              return 0;
            }
        """

        and:
        file("include", "otherProject", "bonjour.h") << """
            void bonjour();
        """

        and:
        file("include", "otherProject", "bonjour.cpp") << """
            THIS FILE WON'T BE COMPILED
        """
        file("src", "main", "cpp", "bad.cpp") << """
            THIS FILE WON'T BE COMPILED
        """
        file("src", "main", "headers", "hello.h") << """
            THIS FILE WON'T BE INCLUDED
        """

        when:
        run "mainExecutable"

        then:
        executable("build/binaries/mainExecutable/main").exec().out == HelloWorldApp.HELLO_WORLD + HelloWorldApp.HELLO_WORLD_FRENCH
    }


}

