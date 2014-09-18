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

package org.gradle.nativebinaries.toolchain.plugins

import org.gradle.api.Plugin
import org.gradle.nativebinaries.toolchain.ToolChain
import org.gradle.nativebinaries.toolchain.Clang
import org.gradle.nativebinaries.toolchain.internal.clang.ClangToolChain

class ClangCompilerPluginTest extends ToolChainPluginTest {

    @Override
    Class<? extends Plugin> getPluginClass() {
        ClangCompilerPlugin
    }

    @Override
    Class<? extends ToolChain> getToolchainClass() {
        Clang
    }

    @Override
    String getToolchainName() {
        "clang"
    }

    def "makes a Clang tool chain available"() {
        when:
        register()

        then:
        toolchain instanceof ClangToolChain
        toolchain.displayName == "Tool chain 'clang' (Clang)"
    }

    def "registers default Clang tool chain"() {
        when:
        addDefaultToolchain()

        then:
        toolchain instanceof ClangToolChain
    }
}
