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

import org.gradle.internal.hash.HashUtil
import org.gradle.nativebinaries.language.assembler.internal.AssembleSpec
import org.gradle.nativebinaries.toolchain.internal.CommandLineTool
import org.gradle.nativebinaries.toolchain.internal.CommandLineToolInvocation
import org.gradle.nativebinaries.toolchain.internal.MutableCommandLineToolInvocation
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification

class AssemblerTest extends Specification {
    @Rule final TestNameTestDirectoryProvider tmpDirProvider = new TestNameTestDirectoryProvider()

    def executable = new File("executable")
    def baseInvocation = Mock(CommandLineToolInvocation)
    def invocation = Mock(MutableCommandLineToolInvocation)
    def commandLineTool = Mock(CommandLineTool)
    def assembler = new Assembler(commandLineTool, baseInvocation, ".o");

    def "assembles each source file independently"() {
        given:
        def testDir = tmpDirProvider.testDirectory
        def objectFileDir = testDir.file("output/objects")

        def sourceOne = testDir.file("one.s")
        def sourceTwo = testDir.file("two.s")

        when:
        AssembleSpec assembleSpec = Mock(AssembleSpec)
        assembleSpec.getObjectFileDir() >> objectFileDir
        assembleSpec.getAllArgs() >> ["-firstArg", "-secondArg"]
        assembleSpec.getSourceFiles() >> [sourceOne, sourceTwo]

        and:
        assembler.execute(assembleSpec)

        then:
        1 * baseInvocation.copy() >> invocation
        1 * invocation.setWorkDirectory(objectFileDir)
        1 * invocation.setArgs(["-firstArg", "-secondArg", "-o", outputFilePathFor(objectFileDir, sourceOne), sourceOne.absolutePath])
        1 * commandLineTool.execute(invocation)

        1 * invocation.setArgs(["-firstArg", "-secondArg", "-o", outputFilePathFor(objectFileDir, sourceTwo), sourceTwo.absolutePath])
        1 * commandLineTool.execute(invocation)
        0 * _
    }

    String outputFilePathFor(File objectFileRoot, File sourceFile) {
        String relativeObjectFilePath = "${HashUtil.createCompactMD5(sourceFile.absolutePath)}/${sourceFile.name - ".s"}.o"
        String outputFilePath = new File(objectFileRoot, relativeObjectFilePath).absolutePath;
        outputFilePath
    }
}
