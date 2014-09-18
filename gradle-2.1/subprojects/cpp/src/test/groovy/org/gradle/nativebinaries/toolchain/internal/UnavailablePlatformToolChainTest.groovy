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

package org.gradle.nativebinaries.toolchain.internal

import org.gradle.api.GradleException
import org.gradle.util.TreeVisitor
import spock.lang.Specification

class UnavailablePlatformToolChainTest extends Specification {
    def reason = new ToolChainAvailability().unavailable("broken")
    def toolChain = new UnavailablePlatformToolChain(reason)

    def "is not available"() {
        expect:
        !toolChain.available

        def visitor = Mock(TreeVisitor)

        when:
        toolChain.explain(visitor)

        then:
        1 * visitor.node("broken")
    }

    def "throws failure when attempting to create a compiler"() {
        when:
        toolChain.newCompiler(Stub(NativeCompileSpec))

        then:
        GradleException e = thrown()
        e.message == "broken"
    }
}
