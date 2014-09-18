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
package org.gradle.nativebinaries.toolchain.internal.gcc

import org.gradle.api.Action
import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.text.TreeFormatter
import org.gradle.nativebinaries.platform.Platform
import org.gradle.nativebinaries.platform.internal.ArchitectureInternal
import org.gradle.nativebinaries.platform.internal.DefaultArchitecture
import org.gradle.nativebinaries.platform.internal.DefaultOperatingSystem
import org.gradle.nativebinaries.toolchain.CommandLineToolConfiguration
import org.gradle.nativebinaries.toolchain.TargetedPlatformToolChain
import org.gradle.nativebinaries.toolchain.internal.PlatformToolChain
import org.gradle.nativebinaries.toolchain.internal.ToolSearchResult
import org.gradle.nativebinaries.toolchain.internal.ToolType
import org.gradle.nativebinaries.toolchain.internal.tools.DefaultGccCommandLineToolConfiguration
import org.gradle.nativebinaries.toolchain.internal.tools.ToolSearchPath
import org.gradle.process.internal.ExecActionFactory
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.gradle.util.TreeVisitor
import spock.lang.Specification

import static org.gradle.nativebinaries.platform.internal.ArchitectureInternal.InstructionSet.X86

class AbstractGccCompatibleToolChainTest extends Specification {
    def fileResolver = Mock(FileResolver)
    def execActionFactory = Mock(ExecActionFactory)
    def toolSearchPath = Stub(ToolSearchPath)
    def tool = Stub(CommandLineToolSearchResult) {
        isAvailable() >> true
    }

    def instantiator = new DirectInstantiator()
    def toolChain = new TestToolChain("test", fileResolver, execActionFactory, toolSearchPath, instantiator)
    def platform = Stub(Platform)

    def "is unavailable when platform is not known and is not the default platform"() {
        given:
        platform.name >> 'unknown'

        expect:
        def platformToolChain = toolChain.select(platform)
        !platformToolChain.available
        getMessage(platformToolChain) == "Don't know how to build for platform 'unknown'."
    }

    def "is unavailable when no language tools can be found and building for default platform"() {
        def missing = Stub(CommandLineToolSearchResult) {
            isAvailable() >> false
            explain(_) >> { TreeVisitor<String> visitor -> visitor.node("c compiler not found") }
        }

        given:
        platform.operatingSystem >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.architecture >> ArchitectureInternal.TOOL_CHAIN_DEFAULT

        and:
        toolSearchPath.locate(ToolType.C_COMPILER, "gcc") >> missing
        toolSearchPath.locate(ToolType.CPP_COMPILER, "g++") >> missing
        toolSearchPath.locate(ToolType.OBJECTIVEC_COMPILER, "gcc") >> missing
        toolSearchPath.locate(ToolType.OBJECTIVECPP_COMPILER, "g++") >> missing

        expect:
        def platformToolChain = toolChain.select(platform)
        !platformToolChain.available
        getMessage(platformToolChain) == "c compiler not found"
    }

    def "is available when any language tool can be found and building for default platform"() {
        def missing = Stub(CommandLineToolSearchResult) {
            isAvailable() >> false
        }

        given:
        platform.operatingSystem >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.architecture >> ArchitectureInternal.TOOL_CHAIN_DEFAULT

        and:
        toolSearchPath.locate(ToolType.CPP_COMPILER, "g++") >> missing
        toolSearchPath.locate(_, _) >> tool

        expect:
        toolChain.select(platform).available
    }

    def "is available when any language tool can be found and platform configuration registered for platform"() {
        given:
        toolSearchPath.locate(_, _) >> tool
        platform.name >> "SomePlatform"
        toolChain.target("SomePlatform", Mock(Action))

        expect:
        toolChain.select(platform).available
    }

    def "selected toolChain applies platform configuration action"() {
        Platform platform1 = Mock(Platform)
        Platform platform2 = Mock(Platform)
        platform1.getName() >> "platform1"

        platform1.getOperatingSystem() >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform2.getOperatingSystem() >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform2.getName() >> "platform2"
        when:
        toolSearchPath.locate(_, _) >> tool

        int platformActionApplied = 0
        toolChain.target([platform1.getName(), platform2.getName()], new Action<TargetedPlatformToolChain>() {
            void execute(TargetedPlatformToolChain configurableToolChain) {
                platformActionApplied++;
            }
        });
        PlatformToolChain selected = toolChain.select(platform1)
        then:
        selected.isAvailable();
        assert platformActionApplied == 1
        when:

        selected = toolChain.select(platform2)
        then:
        selected.isAvailable()
        assert platformActionApplied == 2
    }


    def "selected toolChain uses objectfile suffix based on targetplatform"() {
        Platform platform1 = Mock(Platform)
        Platform platform2 = Mock(Platform)
        platform1.getName() >> "platform1"
        def  platformOSWin = Mock(org.gradle.nativebinaries.platform.OperatingSystem)
        platformOSWin.isWindows() >> true
        def  platformOSNonWin = Mock(org.gradle.nativebinaries.platform.OperatingSystem)
        platformOSNonWin.isWindows() >> false
        platform1.getOperatingSystem() >> platformOSWin
        platform2.getOperatingSystem() >> platformOSNonWin
        platform2.getName() >> "platform2"
        when:
        toolSearchPath.locate(_, _) >> tool

        toolChain.target(platform1.getName())
        toolChain.target(platform2.getName())
        PlatformToolChain selected = toolChain.select(platform1)
        then:
        selected.outputFileSuffix == ".obj"
        when:

        selected = toolChain.select(platform2)
        then:
        selected.outputFileSuffix == ".o"
    }


    def "supplies no additional arguments to target native binary for tool chain default"() {
        when:
        toolSearchPath.locate(_, _) >> tool
        platform.getOperatingSystem() >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.getArchitecture() >> ArchitectureInternal.TOOL_CHAIN_DEFAULT

        TargetedPlatformToolChain configurableToolChain = newConfigurableToolChain()
        then:

        with(toolChain.getPlatformConfiguration(platform).apply(configurableToolChain)) {
            def args = []
            configurableToolChain.getByName("linker").getArgAction().execute(args)
            args == []
            configurableToolChain.getByName("cppCompiler").getArgAction().execute(args)
            args == []
            configurableToolChain.getByName("cCompiler").getArgAction().execute(args)
            args == []
            configurableToolChain.getByName("assembler").getArgAction().execute(args)
            args == []
            configurableToolChain.getByName("staticLibArchiver").getArgAction().execute(args)
            args == []
            configurableToolChain.getByName("objcCompiler").getArgAction().execute(args)
            args == []
        }
    }


    @Requires(TestPrecondition.NOT_WINDOWS)
    def "supplies args for supported architecture"() {
        when:
        toolSearchPath.locate(_, _) >> tool
        platform.operatingSystem >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.architecture >> new DefaultArchitecture(arch, instructionSet, registerSize)

        then:
        toolChain.select(platform).available

        with(toolChain.getPlatformConfiguration(platform).apply(newConfigurableToolChain())) {
            argsFor(getByName("linker")) == [linkerArg]

            argsFor(getByName("cppCompiler")) == [compilerArg]
            argsFor(getByName("cCompiler")) == [compilerArg]

            if (OperatingSystem.current().isMacOsX()) {
                argsFor(getByName("assembler")) == osxAssemblerArgs
            } else {
                argsFor(getByName("assembler")) == [assemblerArg]
            }
            argsFor(getByName("staticLibArchiver")) == []
        }

        where:
        arch     | instructionSet | registerSize | linkerArg | compilerArg | assemblerArg | osxAssemblerArgs
        "i386"   | X86            | 32           | "-m32"    | "-m32"      | "--32"       | ["-arch", "i386"]
        "x86_64" | X86            | 64           | "-m64"    | "-m64"      | "--64"       | ["-arch", "x86_64"]
    }

    @Requires(TestPrecondition.WINDOWS)
    def "supplies args for supported architecture for i386 architecture on windows"() {
        when:
        toolSearchPath.locate(_, _) >> tool
        platform.operatingSystem >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.architecture >> new DefaultArchitecture("i386", X86, 32)

        then:
        toolChain.select(platform).available

        with(toolChain.getPlatformConfiguration(platform).apply(newConfigurableToolChain())) {
            argsFor(getByName("cppCompiler")) == ["-m32"]
            argsFor(getByName("cCompiler")) == ["-m32"]
            argsFor(getByName("linker")) == ["-m32"]
            argsFor(getByName("assembler")) == ["--32"]
            argsFor(getByName("staticLibArchiver")) == []
        }
    }

    @Requires(TestPrecondition.WINDOWS)
    def "cannot target x86_64 architecture on windows"() {
        given:
        toolSearchPath.locate(_, _) >> tool

        and:
        platform.getName() >> "x64"
        platform.operatingSystem >> DefaultOperatingSystem.TOOL_CHAIN_DEFAULT
        platform.architecture >> new DefaultArchitecture("x64", X86, 64)

        when:
        def platformToolChain = toolChain.select(platform)

        then:
        !platformToolChain.available
        getMessage(platformToolChain) == "Don't know how to build for platform 'x64'."
    }

    def "uses supplied platform configurations in order to target binary"() {
        setup:
        _ * platform.getName() >> "platform2"
        def platformConfig1 = Mock(Action)
        def platformConfig2 = Mock(Action)

        when:
        toolSearchPath.locate(_, _) >> tool
        platform.getOperatingSystem() >> new DefaultOperatingSystem("other", OperatingSystem.SOLARIS)
        toolChain.target("platform1", platformConfig1)
        toolChain.target("platform2", platformConfig2)

        PlatformToolChain platformToolChain = toolChain.select(platform)

        then:
        platformToolChain.available

        and:
        1 * platformConfig2.execute(_)
    }

    def "uses platform specific toolchain configuration"() {
        given:
        boolean configurationApplied = false
        _ * platform.getName() >> "testPlatform"
        when:
        toolSearchPath.locate(_, _) >> tool
        platform.getOperatingSystem() >> new DefaultOperatingSystem("other", OperatingSystem.SOLARIS)

        and:
        toolChain.target(platform.getName(), new Action<TargetedPlatformToolChain>() {
            void execute(TargetedPlatformToolChain configurableToolChain) {
                configurationApplied = true;
            }
        })

        then:
        toolChain.select(platform).available
        configurationApplied
    }

    def getMessage(ToolSearchResult result) {
        def formatter = new TreeFormatter()
        result.explain(formatter)
        return formatter.toString()
    }

    static class TestToolChain extends AbstractGccCompatibleToolChain {
        TestToolChain(String name, FileResolver fileResolver, ExecActionFactory execActionFactory, ToolSearchPath tools, Instantiator instantiator) {
            super(name, org.gradle.internal.os.OperatingSystem.current(), fileResolver, execActionFactory, tools, instantiator)
            add(new DefaultGccCommandLineToolConfiguration("cCompiler", ToolType.C_COMPILER, "gcc"));
            add(new DefaultGccCommandLineToolConfiguration("cppCompiler", ToolType.CPP_COMPILER, "g++"));
            add(new DefaultGccCommandLineToolConfiguration("objcCompiler", ToolType.OBJECTIVEC_COMPILER, "gcc"));
            add(new DefaultGccCommandLineToolConfiguration("objcppCompiler", ToolType.OBJECTIVECPP_COMPILER, "g++"));
            add(new DefaultGccCommandLineToolConfiguration("assembler", ToolType.ASSEMBLER, "as"));
            add(new DefaultGccCommandLineToolConfiguration("linker", ToolType.LINKER, "ld"));
            add(new DefaultGccCommandLineToolConfiguration("staticLibArchiver", ToolType.STATIC_LIB_ARCHIVER, "ar"));
        }

        @Override
        protected String getTypeName() {
            return "Test"
        }
    }


    TargetedPlatformToolChain newConfigurableToolChain() {
        def tools = [:]
        tools.put("assembler", new DefaultGccCommandLineToolConfiguration("assembler", ToolType.ASSEMBLER, ""))
        tools.put("cCompiler", new DefaultGccCommandLineToolConfiguration("cCompiler", ToolType.C_COMPILER, ""))
        tools.put("cppCompiler", new DefaultGccCommandLineToolConfiguration("cppCompiler", ToolType.CPP_COMPILER, ""))
        tools.put("objcCompiler", new DefaultGccCommandLineToolConfiguration("objcCompiler", ToolType.OBJECTIVEC_COMPILER, ""))
        tools.put("objcppCompiler", new DefaultGccCommandLineToolConfiguration("objcppCompiler", ToolType.OBJECTIVECPP_COMPILER, ""))
        tools.put("linker", new DefaultGccCommandLineToolConfiguration("linker", ToolType.LINKER, ""))
        tools.put("staticLibArchiver", new DefaultGccCommandLineToolConfiguration("staticLibArchiver", ToolType.STATIC_LIB_ARCHIVER, ""))

        TargetedPlatformToolChain configurableToolChain = new DefaultGccPlatformToolChain(CommandLineToolConfiguration.class,
                tools,
                instantiator,
                "PlatformTestToolChain",
                "Platform specific toolchain")

        return configurableToolChain;
    }

    def argsFor(def tool) {
        def args = []
        tool.getArgAction().execute(args)
        args
    }
}
