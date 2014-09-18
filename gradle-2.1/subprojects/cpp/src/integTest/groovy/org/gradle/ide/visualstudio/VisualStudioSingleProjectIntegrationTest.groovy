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
package org.gradle.ide.visualstudio

import org.gradle.ide.visualstudio.fixtures.FiltersFile
import org.gradle.ide.visualstudio.fixtures.ProjectFile
import org.gradle.ide.visualstudio.fixtures.SolutionFile
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativebinaries.language.cpp.fixtures.AbstractInstalledToolChainIntegrationSpec
import org.gradle.nativebinaries.language.cpp.fixtures.RequiresInstalledToolChain
import org.gradle.nativebinaries.language.cpp.fixtures.app.*

import static org.gradle.nativebinaries.language.cpp.fixtures.ToolChainRequirement.VisualCpp

class VisualStudioSingleProjectIntegrationTest extends AbstractInstalledToolChainIntegrationSpec {
    private final Set<String> projectConfigurations = ['win32Debug', 'win32Release', 'x64Debug', 'x64Release'] as Set

    def app = new CppHelloWorldApp()

    def setup() {
        buildFile << """
    apply plugin: 'cpp'
    apply plugin: 'visual-studio'

    model {
        platforms {
            win32 {
                architecture "i386"
            }
            x64 {
                architecture "amd64"
            }
        }
        buildTypes {
            debug
            release
        }
    }

"""
    }

    def "create visual studio solution for single executable"() {
        when:
        app.writeSources(file("src/main"))
        buildFile << """
    executables {
        main {}
    }
    binaries.all {
        cppCompiler.define "TEST"
        cppCompiler.define "foo", "bar"
    }
"""
        and:
        run "mainVisualStudio"

        then:
        executedAndNotSkipped ":mainExeVisualStudio"

        and:
        final projectFile = projectFile("mainExe.vcxproj")
        projectFile.assertHasComponentSources(app, "src/main")
        projectFile.projectConfigurations.keySet() == projectConfigurations
        projectFile.projectConfigurations.values().each {
            assert it.macros == "TEST;foo=bar"
            assert it.includePath == filePath("src/main/headers")
            assert it.buildCommand == "gradle :install${it.name.capitalize()}MainExecutable"
            assert it.outputFile == OperatingSystem.current().getExecutableName("build/install/mainExecutable/${it.name}/lib/main")
        }

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe")
        mainSolution.assertReferencesProject(projectFile, projectConfigurations)
    }

    def "create visual studio solution for shared and library"() {
        when:
        app.library.writeSources(file("src/main"))
        buildFile << """
    libraries {
        main {}
    }
"""
        and:
        run "mainDllVisualStudio"

        then:
        executedAndNotSkipped ":mainDllVisualStudio"

        and:
        final projectFile = projectFile("mainDll.vcxproj")
        projectFile.assertHasComponentSources(app.library, "src/main")
        projectFile.projectConfigurations.keySet() == projectConfigurations
        projectFile.projectConfigurations.values().each {
            assert it.includePath == filePath("src/main/headers")
            assert it.buildCommand == "gradle :${it.name}MainSharedLibrary"
            assert it.outputFile == OperatingSystem.current().getSharedLibraryName("build/binaries/mainSharedLibrary/${it.name}/main")
        }

        and:
        final mainSolution = solutionFile("mainDll.sln")
        mainSolution.assertHasProjects("mainDll")
        mainSolution.assertReferencesProject(projectFile, projectConfigurations)
    }

    def "create visual studio solution for static library"() {
        when:
        app.library.writeSources(file("src/main"))
        buildFile << """
    libraries {
        main {
        }
    }
"""
        and:
        run "mainLibVisualStudio"

        then:
        executedAndNotSkipped ":mainLibVisualStudio"

        and:
        final projectFile = projectFile("mainLib.vcxproj")
        projectFile.assertHasComponentSources(app.library, "src/main")
        projectFile.projectConfigurations.keySet() == projectConfigurations
        projectFile.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers")

        and:
        final mainSolution = solutionFile("mainLib.sln")
        mainSolution.assertHasProjects("mainLib")
        mainSolution.assertReferencesProject(projectFile, projectConfigurations)
    }

    def "lifecycle task creates visual studio solution for buildable static and shared libraries"() {
        when:
        app.library.writeSources(file("src/main"))
        buildFile << """
    libraries {
        both {}
        staticOnly {
            binaries.withType(SharedLibraryBinarySpec) {
                buildable false
            }
        }
    }
"""
        and:
        run "bothVisualStudio", "staticOnlyVisualStudio"

        then:
        executedAndNotSkipped ":bothDllVisualStudio", ":bothLibVisualStudio", ":staticOnlyLibVisualStudio"

        and:
        file("staticOnlyLib.sln").assertExists()
        file("staticOnlyDll.sln").assertDoesNotExist()
    }

    def "create visual studio solution for defined static library"() {
        when:
        app.library.writeSources(file("src/main"))
        buildFile << """
    libraries {
        main {
            binaries.withType(SharedLibraryBinarySpec) {
                buildable = false
            }
        }
    }
"""
        and:
        run "mainLibVisualStudio"

        then:
        executedAndNotSkipped ":mainLibVisualStudio"

        and:
        final projectFile = projectFile("mainLib.vcxproj")
        projectFile.assertHasComponentSources(app.library, "src/main")
        projectFile.projectConfigurations.keySet() == projectConfigurations
        projectFile.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers")

        and:
        final mainSolution = solutionFile("mainLib.sln")
        mainSolution.assertHasProjects("mainLib")
        mainSolution.assertReferencesProject(projectFile, projectConfigurations)
    }

    def "create visual studio solution for executable that depends on static library"() {
        when:
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        buildFile << """
    libraries {
        hello {}
    }
    executables {
        main {}
    }
    sources {
        main.cpp.lib libraries.hello.static
    }
"""
        and:
        run "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        exeProject.assertHasComponentSources(app.executable, "src/main")
        exeProject.projectConfigurations.keySet() == projectConfigurations
        exeProject.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers", "src/hello/headers")

        and:
        final libProject = projectFile("helloLib.vcxproj")
        libProject.assertHasComponentSources(app.library, "src/hello")
        libProject.projectConfigurations.keySet() == projectConfigurations
        libProject.projectConfigurations['win32Debug'].includePath == filePath("src/hello/headers")

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloLib")
        mainSolution.assertReferencesProject(exeProject, projectConfigurations)
        mainSolution.assertReferencesProject(libProject, projectConfigurations)
    }

    def "create visual studio solution for executable that depends on shared library"() {
        when:
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        buildFile << """
    libraries {
        hello {}
    }
    executables {
        main {}
    }
    sources {
        main.cpp.lib libraries.hello
    }
"""
        and:
        run "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        exeProject.assertHasComponentSources(app.executable, "src/main")
        exeProject.projectConfigurations.keySet() == projectConfigurations
        exeProject.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers", "src/hello/headers")

        and:
        final dllProject = projectFile("helloDll.vcxproj")
        dllProject.assertHasComponentSources(app.library, "src/hello")
        dllProject.projectConfigurations.keySet() == projectConfigurations
        dllProject.projectConfigurations['win32Debug'].includePath == filePath("src/hello/headers")

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloDll")
        mainSolution.assertReferencesProject(exeProject, projectConfigurations)
        mainSolution.assertReferencesProject(dllProject, projectConfigurations)
    }

    def "create visual studio solution for executable that depends on library that depends on another library"() {
        given:
        def testApp = new ExeWithLibraryUsingLibraryHelloWorldApp()
        testApp.writeSources(file("src/main"), file("src/hello"), file("src/greetings"))

        buildFile << """
            apply plugin: "cpp"
            libraries {
                greetings {}
                hello {}
            }
            executables {
                main {}
            }
            sources {
                hello.cpp.lib libraries.greetings.static
                main.cpp.lib libraries.hello
            }
        """
        when:
        run "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        exeProject.assertHasComponentSources(testApp.executable, "src/main")
        exeProject.projectConfigurations.keySet() == projectConfigurations
        exeProject.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers", "src/hello/headers")

        and:
        final helloDllProject = projectFile("helloDll.vcxproj")
        helloDllProject.assertHasComponentSources(testApp.library, "src/hello")
        helloDllProject.projectConfigurations.keySet() == projectConfigurations
        helloDllProject.projectConfigurations['win32Debug'].includePath == filePath("src/hello/headers", "src/greetings/headers")

        and:
        final greetingsLibProject = projectFile("greetingsLib.vcxproj")
        greetingsLibProject.assertHasComponentSources(testApp.greetingsLibrary, "src/greetings")
        greetingsLibProject.projectConfigurations.keySet() == projectConfigurations
        greetingsLibProject.projectConfigurations['win32Debug'].includePath == filePath("src/greetings/headers")

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloDll", "greetingsLib")
        mainSolution.assertReferencesProject(exeProject, projectConfigurations)
        mainSolution.assertReferencesProject(helloDllProject, projectConfigurations)
        mainSolution.assertReferencesProject(greetingsLibProject, projectConfigurations)
    }

    def "create visual studio solutions for 2 executables that depend on different linkages of the same library"() {
        when:
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        buildFile << """
    libraries {
        hello {}
    }
    executables {
        main {}
        mainStatic {}
    }
    sources {
        main.cpp.lib libraries.hello
        mainStatic.cpp.source.srcDirs "src/main/cpp"
        mainStatic.cpp.lib libraries.hello.static
    }
"""
        and:
        run "mainVisualStudio", "mainStaticVisualStudio"

        then:
        solutionFile("mainExe.sln").assertHasProjects("mainExe", "helloDll")
        solutionFile("mainStaticExe.sln").assertHasProjects("mainStaticExe", "helloLib")

        and:
        final exeProject = projectFile("mainExe.vcxproj")
        final staticExeProject = projectFile("mainStaticExe.vcxproj")
        exeProject.sourceFiles == staticExeProject.sourceFiles
        exeProject.headerFiles == []
        staticExeProject.headerFiles == []

        and:
        final dllProject = projectFile("helloDll.vcxproj")
        final libProject = projectFile("helloLib.vcxproj")
        dllProject.sourceFiles == libProject.sourceFiles
        dllProject.headerFiles == libProject.headerFiles
    }

    def "create visual studio solutions for 2 executables that depend on different build types of the same library"() {
        when:
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        buildFile << """
    libraries {
        hello {}
    }
    executables {
        main {}
        mainRelease {
            targetBuildTypes "release"
        }
    }
    sources {
        main.cpp.lib libraries.hello

        mainRelease.cpp.source.srcDirs "src/main/cpp"
        mainRelease.cpp.lib libraries.hello
    }
"""
        and:
        run "mainVisualStudio", "mainReleaseVisualStudio"

        then:
        solutionFile("mainExe.sln").assertHasProjects("mainExe", "helloDll")
        solutionFile("mainReleaseExe.sln").assertHasProjects("mainReleaseExe", "helloDll")

        and:
        final helloProjectFile = projectFile("helloDll.vcxproj")
        helloProjectFile.projectConfigurations.keySet() == projectConfigurations
        final mainProjectFile = projectFile("mainExe.vcxproj")
        mainProjectFile.projectConfigurations.keySet() == projectConfigurations
        final mainReleaseProjectFile = projectFile("mainReleaseExe.vcxproj")
        mainReleaseProjectFile.projectConfigurations.keySet() == ['win32', 'x64'] as Set

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertReferencesProject(helloProjectFile, projectConfigurations)
        final mainReleaseSolution = solutionFile("mainReleaseExe.sln")
        mainReleaseSolution.assertReferencesProject(helloProjectFile, [win32: 'win32Release', x64: 'x64Release'])
    }

    def "create visual studio project for executable that targets multiple platforms with the same architecture"() {
        when:
        app.writeSources(file("src/main"))
        buildFile << """
    model {
        platforms {
            otherWin32 {
                architecture "i386"
            }
        }
    }
    executables {
        main {
            targetPlatforms "win32", "otherWin32"
        }
    }
"""
        and:
        run "mainVisualStudio"

        then:
        final mainProjectFile = projectFile("mainExe.vcxproj")
        mainProjectFile.projectConfigurations.keySet() == ['win32Debug', 'otherWin32Debug', 'win32Release', 'otherWin32Release'] as Set
    }

    def "create visual studio solution for executable that has diamond dependency"() {
        def testApp = new ExeWithDiamondDependencyHelloWorldApp()
        testApp.writeSources(file("src/main"), file("src/hello"), file("src/greetings"))

        buildFile << """
            executables {
                main {}
            }
            libraries {
                hello {}
                greetings {}
            }
            sources {
                main.cpp.lib libraries.hello.shared
                main.cpp.lib libraries.greetings.static
                hello.cpp.lib libraries.greetings.static
            }
        """

        when:
        succeeds "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        final helloProject = projectFile("helloDll.vcxproj")
        final greetProject = projectFile("greetingsLib.vcxproj")
        final mainSolution = solutionFile("mainExe.sln")

        and:
        mainSolution.assertHasProjects("mainExe", "helloDll", "greetingsLib")
        mainSolution.assertReferencesProject(exeProject, projectConfigurations)
        mainSolution.assertReferencesProject(helloProject, projectConfigurations)
        mainSolution.assertReferencesProject(greetProject, projectConfigurations)

        and:
        exeProject.assertHasComponentSources(testApp.executable, "src/main")
        exeProject.projectConfigurations.keySet() == projectConfigurations
        exeProject.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers", "src/hello/headers", "src/greetings/headers")
    }

    def "create visual studio solution for executable that depends on both static and shared linkage of library"() {
        given:
        def testApp = new ExeWithDiamondDependencyHelloWorldApp()
        testApp.writeSources(file("src/main"), file("src/hello"), file("src/greetings"))

        and:
        buildFile << """
            executables {
                main {}
            }
            libraries {
                hello {}
                greetings {}
            }
            sources {
                main.cpp.lib libraries.hello.shared
                main.cpp.lib libraries.greetings.shared
                hello.cpp.lib libraries.greetings.static
            }
        """

        when:
        succeeds "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        final helloProject = projectFile("helloDll.vcxproj")
        final greetDllProject = projectFile("greetingsDll.vcxproj")
        final greetLibProject = projectFile("greetingsLib.vcxproj")
        final mainSolution = solutionFile("mainExe.sln")

        and:
        mainSolution.assertHasProjects("mainExe", "helloDll", "greetingsLib", "greetingsDll")
        mainSolution.assertReferencesProject(exeProject, projectConfigurations)
        mainSolution.assertReferencesProject(helloProject, projectConfigurations)
        mainSolution.assertReferencesProject(greetDllProject, projectConfigurations)
        mainSolution.assertReferencesProject(greetLibProject, projectConfigurations)

        and:
        exeProject.assertHasComponentSources(testApp.executable, "src/main")
        exeProject.projectConfigurations.keySet() == projectConfigurations
        exeProject.projectConfigurations['win32Debug'].includePath == filePath("src/main/headers", "src/hello/headers", "src/greetings/headers")

        and:
        helloProject.assertHasComponentSources(testApp.library, "src/hello")
        helloProject.projectConfigurations.keySet() == projectConfigurations
        helloProject.projectConfigurations['win32Debug'].includePath == filePath("src/hello/headers", "src/greetings/headers")
    }

    def "generate visual studio solution for executable with mixed sources"() {
        given:
        def testApp = new MixedLanguageHelloWorldApp(toolChain)
        testApp.writeSources(file("src/main"))

        and:
        buildFile << """
            apply plugin: 'c'
            apply plugin: 'assembler'
            executables {
                main {}
            }
        """

        when:
        run "mainVisualStudio"

        then:
        final projectFile = projectFile("mainExe.vcxproj")
        projectFile.assertHasComponentSources(testApp, "src/main")
        projectFile.projectConfigurations.keySet() == projectConfigurations
        with (projectFile.projectConfigurations['win32Debug']) {
            includePath == filePath("src/main/headers")
        }

        and:
        solutionFile("mainExe.sln").assertHasProjects("mainExe")
    }

    @RequiresInstalledToolChain(VisualCpp)
    def "generate visual studio solution for executable with windows resource files"() {
        given:
        def resourceApp = new WindowsResourceHelloWorldApp()
        resourceApp.writeSources(file("src/main"))

        and:
        buildFile << """
            apply plugin: 'windows-resources'
            executables {
                main {}
            }
            binaries.all {
                rcCompiler.define "TEST"
                rcCompiler.define "foo", "bar"
            }
        """

        when:
        run "mainVisualStudio"

        then:
        final projectFile = projectFile("mainExe.vcxproj")
        final List<SourceFile> resources = resourceApp.resourceSources
        final List<SourceFile> sources = resourceApp.sourceFiles - resources
        assert projectFile.headerFiles == resourceApp.headerFiles*.withPath("src/main").sort()
        assert projectFile.sourceFiles == ['build.gradle'] + sources*.withPath("src/main").sort()
        assert projectFile.resourceFiles == resources*.withPath("src/main").sort()

        projectFile.projectConfigurations.keySet() == projectConfigurations
        with (projectFile.projectConfigurations['win32Debug']) {
            macros == "TEST;foo=bar"
            includePath == filePath("src/main/headers")
        }

        and:
        solutionFile("mainExe.sln").assertHasProjects("mainExe")
    }

    def "builds solution for component with no source"() {
        given:
        buildFile << """
            executables {
                main {}
            }
        """

        when:
        run "mainVisualStudio"

        then:
        final projectFile = projectFile("mainExe.vcxproj")
        projectFile.sourceFiles == ['build.gradle']
        projectFile.headerFiles == []
        projectFile.projectConfigurations.keySet() == projectConfigurations
        with (projectFile.projectConfigurations['win32Debug']) {
            includePath == filePath("src/main/headers")
        }

        and:
        solutionFile("mainExe.sln").assertHasProjects("mainExe")
    }

    def "visual studio project includes headers co-located with sources"() {
        when:
        // Write headers so they sit with sources
        app.allFiles.each {
            it.writeToFile(file("src/main/cpp/${it.name}"))
        }
        buildFile << """
    executables {
        main {}
    }
    sources {
        main.cpp.source.include "**/*.cpp"
    }
"""
        and:
        run "mainVisualStudio"

        then:
        executedAndNotSkipped ":mainExeVisualStudio"

        and:
        final projectFile = projectFile("mainExe.vcxproj")
        assert projectFile.sourceFiles == ['build.gradle'] + app.sourceFiles.collect({"src/main/cpp/${it.name}"}).sort()
        assert projectFile.headerFiles == app.headerFiles.collect({"src/main/cpp/${it.name}"}).sort()
    }

    def "visual studio solution with header-only library"() {
        given:
        def app = new CppHelloWorldApp()
        app.executable.writeSources(file("src/main"))

        app.library.headerFiles*.writeToDir(file("src/helloApi"))
        app.library.sourceFiles*.writeToDir(file("src/hello"))

        and:
        buildFile << """
            executables {
                main {}
            }
            libraries {
                helloApi {}
                hello {}
            }
            sources {
                main.cpp.lib library: 'helloApi', linkage: 'api' // TODO:DAZ This should not be needed
                main.cpp.lib library: 'hello'
                hello.cpp.lib library: 'helloApi', linkage: 'api'
            }
        """

        when:
        succeeds "mainVisualStudio"

        then:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloDll", "helloApiDll")

        and:
        final mainExeProject = projectFile("mainExe.vcxproj")
        with (mainExeProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/main/headers", "src/helloApi/headers", "src/hello/headers")
        }

        and:
        final helloDllProject = projectFile("helloDll.vcxproj")
        with (helloDllProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/hello/headers", "src/helloApi/headers")
        }

        and:
        final helloApiDllProject = projectFile("helloApiDll.vcxproj")
        with (helloApiDllProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/helloApi/headers")
        }
    }

    def "create visual studio solution for executable with variant conditional sources"() {
        when:
        app.writeSources(file("src/win32"))
        app.alternate.writeSources(file("src/x64"))
        buildFile << """
    executables {
        main {}
    }

    sources {
        win32 {
            cpp(CppSourceSet)
        }
        x64 {
            cpp(CppSourceSet)
        }
    }

    binaries.all {
        source sources[it.targetPlatform.name]
    }
"""
        and:
        run "mainVisualStudio"

        then:
        final projectFile = projectFile("mainExe.vcxproj")
        projectFile.assertHasComponentSources(app, "src/win32", app.alternate, "src/x64")
        projectFile.projectConfigurations.keySet() == projectConfigurations

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe")
        mainSolution.assertReferencesProject(projectFile, projectConfigurations)
    }

    def "visual studio solution with pre-built library"() {
        given:
        app.writeSources(file("src/main"))
        buildFile << """
    model {
        repositories {
            libs(PrebuiltLibraries) {
                test {
                    headers.srcDir "libs/test/include"
                }
            }
        }
    }
    executables {
        main {}
    }
    sources {
        main.cpp.lib library: 'test', linkage: 'api'
    }
"""

        when:
        run "mainVisualStudio"

        then:
        executedAndNotSkipped ":mainExeVisualStudio"
        and:

        then:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe")

        and:
        final mainExeProject = projectFile("mainExe.vcxproj")
        with (mainExeProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/main/headers", "libs/test/include")
        }
    }

    def "visual studio solution for component graph with library dependency cycle"() {
        given:
        def app = new ExeWithLibraryUsingLibraryHelloWorldApp()
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        app.greetingsHeader.writeToDir(file("src/hello"))
        app.greetingsSources*.writeToDir(file("src/greetings"))

        and:
        buildFile << """
            executables {
                main {}
            }
            libraries {
                hello {}
                greetings {}
            }
            sources {
                main.cpp.lib library: 'hello'
                hello.cpp.lib library: 'greetings', linkage: 'static'
                greetings.cpp.lib library: 'hello', linkage: 'api'
            }
        """

        when:
        succeeds "mainVisualStudio"

        then:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloDll", "greetingsLib")

        and:
        final mainExeProject = projectFile("mainExe.vcxproj")
        with (mainExeProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/main/headers", "src/hello/headers")
        }

        and:
        final helloDllProject = projectFile("helloDll.vcxproj")
        with (helloDllProject.projectConfigurations['win32Debug']) {
            includePath == filePath( "src/hello/headers", "src/greetings/headers")
        }

        and:
        final greetingsLibProject = projectFile("greetingsLib.vcxproj")
        with (greetingsLibProject.projectConfigurations['win32Debug']) {
            includePath == filePath("src/greetings/headers", "src/hello/headers")
        }
    }

    def "create visual studio solution where referenced projects have different configurations"() {
        when:
        app.executable.writeSources(file("src/main"))
        app.library.writeSources(file("src/hello"))
        buildFile << """
    libraries {
        hello {}
    }
    executables {
        main {
            targetPlatforms "win32"
            targetBuildTypes "release"
        }
    }
    sources {
        main.cpp.lib libraries.hello
    }
"""
        and:
        run "mainVisualStudio"

        then:
        final exeProject = projectFile("mainExe.vcxproj")
        exeProject.assertHasComponentSources(app.executable, "src/main")
        exeProject.projectConfigurations.keySet() == ['release'] as Set
        exeProject.projectConfigurations['release'].includePath == filePath("src/main/headers", "src/hello/headers")

        and:
        final dllProject = projectFile("helloDll.vcxproj")
        dllProject.assertHasComponentSources(app.library, "src/hello")
        dllProject.projectConfigurations.keySet() == projectConfigurations
        dllProject.projectConfigurations['win32Debug'].includePath == filePath("src/hello/headers")

        and:
        final mainSolution = solutionFile("mainExe.sln")
        mainSolution.assertHasProjects("mainExe", "helloDll")
        mainSolution.assertReferencesProject(exeProject, ['release'])
        mainSolution.assertReferencesProject(dllProject, [release: 'win32Release'])
    }

    private SolutionFile solutionFile(String path) {
        return new SolutionFile(file(path))
    }

    private ProjectFile projectFile(String path) {
        return new ProjectFile(file(path))
    }

    private FiltersFile filtersFile(String path) {
        return new FiltersFile(file(path))
    }

    private static String filePath(String... paths) {
        return (paths as List).join(';')
    }
}
