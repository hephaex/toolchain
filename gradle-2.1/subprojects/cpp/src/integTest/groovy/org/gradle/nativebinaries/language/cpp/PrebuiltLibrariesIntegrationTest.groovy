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
import org.gradle.nativebinaries.language.cpp.fixtures.app.CppHelloWorldApp
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition

@Requires(TestPrecondition.CAN_INSTALL_EXECUTABLE)
class PrebuiltLibrariesIntegrationTest extends AbstractInstalledToolChainIntegrationSpec {
    final app = new CppHelloWorldApp()

    def "setup"() {
        settingsFile << "rootProject.name = 'test'"
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("libs/src/hello"))

        file("libs/build.gradle") << """
            apply plugin: 'cpp'
            model {
                flavors {
                    english
                    french
                }
            }
            libraries {
                hello {
                    binaries.all {
                        if (flavor == flavors.french) {
                            cppCompiler.define "FRENCH"
                        }
                    }
                }
            }
"""
    }

    private void preBuildLibrary() {
        executer.inDirectory(file("libs"))
        run "assemble"
    }

    def "can link to a prebuilt header-only library with api linkage"() {
        given:
        app.alternateLibrarySources*.writeToDir(file("src/main"))
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs(PrebuiltLibraries) {
                        hello {
                            headers.srcDir "libs/src/hello/headers"
                        }
                    }
                }
            }
            executables {
                main {}
            }
            sources {
                main.cpp.lib library: 'hello', linkage: 'api'
            }
        """

        when:
        succeeds "installMainExecutable"

        then:
        installation("build/install/mainExecutable").exec().out == app.alternateLibraryOutput
    }

    def "can link to a prebuilt library with static and shared linkage"() {
        given:
        preBuildLibrary()

        and:
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs(PrebuiltLibraries) {
                        hello {
                            headers.srcDir "libs/src/hello/headers"
                            binaries.withType(StaticLibraryBinary) {
                                def libName = targetPlatform.operatingSystem.windows ? 'hello.lib' : 'libhello.a'
                                staticLibraryFile = file("libs/build/binaries/helloStaticLibrary/english/\${libName}")
                            }
                            binaries.withType(SharedLibraryBinary) {
                                def os = targetPlatform.operatingSystem
                                def baseDir = "libs/build/binaries/helloSharedLibrary/french"
                                if (os.windows) {
                                    // Windows uses a .dll file, and a different link file if it exists (not Cygwin or MinGW)
                                    sharedLibraryFile = file("\${baseDir}/hello.dll")
                                    if (file("\${baseDir}/hello.lib").exists()) {
                                        sharedLibraryLinkFile = file("\${baseDir}/hello.lib")
                                    }
                                } else if (os.macOsX) {
                                    sharedLibraryFile = file("\${baseDir}/libhello.dylib")
                                } else {
                                    sharedLibraryFile = file("\${baseDir}/libhello.so")
                                }
                            }
                        }
                    }
                }
            }
            executables {
                main {}
                mainStatic {}
            }
            sources {
                main.cpp {
                    lib library: 'hello'
                }
                mainStatic.cpp {
                    source.srcDir "src/main/cpp"
                    lib library: 'hello', linkage: 'static'
                }
            }
        """

        when:
        succeeds "installMainExecutable", "installMainStaticExecutable"

        then:
        installation("build/install/mainExecutable").exec().out == app.frenchOutput
        installation("build/install/mainStaticExecutable").exec().out == app.englishOutput
    }

    def "searches all prebuilt library repositories"() {
        given:
        preBuildLibrary()

        and:
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs1(PrebuiltLibraries) {
                        nope {
                            headers.srcDir "not/here"
                        }
                    }
                    libs2(PrebuiltLibraries) {
                        hello {
                            headers.srcDir "libs/src/hello/headers"
                            binaries.withType(StaticLibraryBinary) {
                                def libName = targetPlatform.operatingSystem.windows ? 'hello.lib' : 'libhello.a'
                                staticLibraryFile = file("libs/build/binaries/helloStaticLibrary/french/\${libName}")
                            }
                        }
                    }
                }
            }
            executables {
                main {}
            }
            sources {
                main.cpp.lib library: 'hello', linkage: 'static'
            }
        """

        when:
        succeeds "installMainExecutable"

        then:
        installation("build/install/mainExecutable").exec().out == app.frenchOutput
    }

    def "locates prebuilt library in another project"() {
        given:
        app.executable.writeSources(file("projectA/src/main"))
        app.librarySources*.writeToDir(file("projectA/src/main"))
        app.libraryHeader.writeToDir(file("projectB/libs/src/hello"))

        and:
        settingsFile.text = "include ':projectA', ':projectB'"
        buildFile << """
            project(':projectA') {
                apply plugin: 'cpp'
                executables {
                    main {}
                }
                sources {
                    main.cpp.lib project: ':projectB', library: 'hello', linkage: 'api'
                }
            }
            project(':projectB') {
                apply plugin: 'cpp'
                model {
                    repositories {
                        libs(PrebuiltLibraries) {
                            hello {
                                headers.srcDir "../libs/src/hello/headers"
                            }
                        }
                    }
                }
            }
        """

        when:
        succeeds "installMainExecutable"

        then:
        installation("projectA/build/install/mainExecutable").exec().out == app.englishOutput
    }

    def "produces reasonable error message when no output file is defined for binary"() {
        given:
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs(PrebuiltLibraries) {
                        hello {
                            headers.srcDir "libs/src/hello/headers"
                        }
                    }
                }
            }
            executables {
                main {}
            }
            sources {
                main.cpp.lib library: 'hello', linkage: 'static'
            }
        """

        when:
        fails "mainExecutable"

        then:
        failure.assertHasDescription("Static library file not set for prebuilt library 'hello'.")
    }

    def "produces reasonable error message when prebuilt library output file does not exist"() {
        given:
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs(PrebuiltLibraries) {
                        hello {
                            headers.srcDir "libs/src/hello/headers"
                            binaries.withType(StaticLibraryBinary) { binary ->
                                staticLibraryFile = file("does_not_exist")
                            }
                        }
                    }
                }
            }
            executables {
                main {}
            }
            sources {
                main.cpp.lib library: 'hello', linkage: 'static'
            }
        """

        when:
        succeeds "tasks"
        fails "mainExecutable"

        then:
        failure.assertHasDescription("Static library file ${file("does_not_exist").absolutePath} does not exist for prebuilt library 'hello'.")
    }

    def "produces reasonable error message when prebuilt library does not exist"() {
        given:
        buildFile << """
            apply plugin: 'cpp'
            model {
                repositories {
                    libs(PrebuiltLibraries) {
                        hello
                    }
                    libs2(PrebuiltLibraries) {
                        hello2
                    }
                }
            }
            executables {
                main {}
            }
            sources {
                main.cpp.lib library: 'other'
            }
        """

        when:
        fails "mainExecutable"

        then:
        failure.assertHasDescription("Could not locate library 'other'.")
        failure.assertHasCause("NativeLibrarySpec with name 'other' not found.")
        failure.assertHasCause("Prebuilt library with name 'other' not found in repositories '[libs, libs2]'.")
    }

    def "produces reasonable error message when prebuilt library does not exist in a different project"() {
        given:
        settingsFile.text = "include ':projectA', ':projectB'"
        buildFile << """
            project(':projectA') {
                apply plugin: 'cpp'
                model {
                    repositories {
                        libs(PrebuiltLibraries) {
                            hello {
                                headers.srcDir "libs/src/hello/headers"
                            }
                        }
                    }
                }
                executables {
                    main {}
                }
                sources {
                    main.cpp.lib project: ':projectB', library: 'hello', linkage: 'api'
                }
            }
            project(':projectB') {
                apply plugin: 'cpp'
                model {
                    repositories {
                        libs(PrebuiltLibraries) {
                            hello1
                        }
                        libs2(PrebuiltLibraries) {
                            hello2
                        }
                    }
                }
            }
        """

        when:
        fails "mainExecutable"

        then:
        failure.assertHasDescription("Could not locate library 'hello' for project ':projectB'.")
        failure.assertHasCause("NativeLibrarySpec with name 'hello' not found.")
        failure.assertHasCause("Prebuilt library with name 'hello' not found in repositories '[libs, libs2]'.")
    }
}
