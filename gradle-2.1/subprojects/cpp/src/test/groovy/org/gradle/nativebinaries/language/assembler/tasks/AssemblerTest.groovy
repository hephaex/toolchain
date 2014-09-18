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

package org.gradle.nativebinaries.language.assembler.tasks
import org.gradle.language.base.internal.compile.Compiler
import org.gradle.api.tasks.WorkResult
import org.gradle.nativebinaries.platform.internal.PlatformInternal
import org.gradle.nativebinaries.toolchain.internal.PlatformToolChain
import org.gradle.nativebinaries.toolchain.internal.ToolChainInternal
import org.gradle.nativebinaries.language.assembler.internal.AssembleSpec
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.util.TestUtil
import spock.lang.Specification

class AssemblerTest extends Specification {
    def testDir = new TestNameTestDirectoryProvider().testDirectory
    Assemble assembleTask = TestUtil.createTask(Assemble)
    def toolChain = Mock(ToolChainInternal)
    def platform = Mock(PlatformInternal)
    def platformToolChain = Mock(PlatformToolChain)
    Compiler<AssembleSpec> assembler = Mock(Compiler)

    def "executes using the Assembler"() {
        def inputDir = testDir.file("sourceFile")
        def result = Mock(WorkResult)
        when:
        assembleTask.toolChain = toolChain
        assembleTask.targetPlatform = platform
        assembleTask.assemblerArgs = ["arg"]
        assembleTask.objectFileDir = testDir.file("outputFile")
        assembleTask.source inputDir
        assembleTask.execute()

        then:
        _ * toolChain.outputType >> "c"
        _ * platform.compatibilityString >> "p"
        1 * toolChain.select(platform) >> platformToolChain
        1 * platformToolChain.newCompiler({it instanceof AssembleSpec}) >> assembler
        1 * assembler.execute({ AssembleSpec spec ->
            assert spec.sourceFiles*.name == ["sourceFile"]
            assert spec.args == ['arg']
            assert spec.allArgs == ['arg']
            assert spec.objectFileDir.name == "outputFile"
            true
        }) >> result
        1 * result.didWork >> true
        0 * _._

        and:
        assembleTask.didWork
    }
}
