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

package org.gradle.nativebinaries.internal

import org.gradle.runtime.base.internal.DefaultBinaryNamingScheme
import org.gradle.nativebinaries.BuildType
import org.gradle.nativebinaries.NativeExecutableSpec
import org.gradle.nativebinaries.internal.resolve.NativeDependencyResolver
import org.gradle.nativebinaries.platform.Platform
import org.gradle.nativebinaries.toolchain.internal.ToolChainInternal
import spock.lang.Specification

class DefaultProjectNativeExecutableBinaryTest extends Specification {
    def namingScheme = new DefaultBinaryNamingScheme("bigOne", "executable", [])

    def "has useful string representation"() {
        given:
        def executable = Stub(NativeExecutableSpec)

        when:
        def binary = new DefaultNativeExecutableBinarySpec(executable, new DefaultFlavor("flavorOne"), Stub(ToolChainInternal), Stub(Platform), Stub(BuildType), namingScheme, Mock(NativeDependencyResolver))

        then:
        binary.toString() == "executable 'bigOne:executable'"
    }
}
