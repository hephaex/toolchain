/*
 * Copyright 2014 the original author or authors.
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

import org.gradle.api.internal.file.FileResolver
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.nativebinaries.toolchain.internal.clang.ClangToolChain
import org.gradle.process.internal.ExecActionFactory
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

class ClangToolChainTest extends Specification {
    @Rule final TestNameTestDirectoryProvider tmpDirProvider = new TestNameTestDirectoryProvider()
    final FileResolver fileResolver = Mock(FileResolver)
    final Instantiator instantiator = new DirectInstantiator()
    final toolChain = new ClangToolChain("clang", OperatingSystem.current(), fileResolver, Stub(ExecActionFactory), instantiator)

    @Unroll
    def "has default #tool registered using #name"() {
        expect:
        toolChain.getByName(tool).executable == executable
        where:
        tool                | executable
        "cCompiler"         | "clang"
        "cppCompiler"       | "clang++"
        "linker"            | "clang++"
        "staticLibArchiver" | "ar"
    }

    def "can update tool names"() {
        when:
        toolChain.getByName("cCompiler").executable = "foo"

        then:
        toolChain.getByName("cCompiler").executable == "foo"
    }
}
