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

package org.gradle.nativebinaries.language.cpp.internal
import org.gradle.api.internal.file.FileResolver
import org.gradle.language.base.FunctionalSourceSet
import org.gradle.language.cpp.internal.DefaultCppSourceSet
import org.gradle.nativebinaries.NativeDependencySet
import org.gradle.nativebinaries.NativeLibraryBinary
import org.gradle.nativebinaries.NativeLibrarySpec
import spock.lang.Specification

class DefaultCppSourceSetTest extends Specification {
    def parent = Stub(FunctionalSourceSet) {
        getName() >> "main"
    }
    def fileResolver = Mock(FileResolver)
    def sourceSet = new DefaultCppSourceSet("cpp", parent, fileResolver)

    def "has useful string representation"() {
        expect:
        sourceSet.displayName == "C++ source 'main:cpp'"
        sourceSet.toString() == "C++ source 'main:cpp'"
    }

    def "can add a library as a dependency of the source set"() {
        def library = Mock(NativeLibrarySpec)

        when:
        sourceSet.lib(library)

        then:
        sourceSet.libs.contains(library)
    }

    def "can add a library binary as a dependency of the binary"() {
        def library = Mock(NativeLibraryBinary)

        when:
        sourceSet.lib(library)

        then:
        sourceSet.libs.contains(library)
    }

    def "can add a native dependency as a dependency of the binary"() {
        def dependency = Stub(NativeDependencySet)

        when:
        sourceSet.lib(dependency)

        then:
        sourceSet.libs.contains(dependency)
    }
}
