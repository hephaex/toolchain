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

package org.gradle.nativebinaries.internal.prebuilt

import org.gradle.nativebinaries.BuildType
import org.gradle.nativebinaries.Flavor
import org.gradle.nativebinaries.PrebuiltLibrary
import org.gradle.nativebinaries.platform.Platform
import spock.lang.Specification

class DefaultPrebuiltSharedLibraryBinaryTest extends Specification {
    def binary = new DefaultPrebuiltSharedLibraryBinary("name", Stub(PrebuiltLibrary), Stub(BuildType), Stub(Platform), Stub(Flavor))

    def "has useful string representation"() {
        expect:
        binary.toString() == "shared library 'name'"
        binary.displayName == "shared library 'name'"
    }

    def "uses library file when link file not set"() {
        given:
        def sharedLibraryFile = Mock(File)
        def sharedLibraryLinkFile = Mock(File)

        when:
        binary.sharedLibraryFile = sharedLibraryFile

        then:
        binary.sharedLibraryFile == sharedLibraryFile
        binary.sharedLibraryLinkFile == sharedLibraryFile

        when:
        binary.sharedLibraryLinkFile = sharedLibraryLinkFile

        then:
        binary.sharedLibraryFile == sharedLibraryFile
        binary.sharedLibraryLinkFile == sharedLibraryLinkFile
    }

    def "uses specified linke file and library file"() {
        given:
        def sharedLibraryFile = createFile()
        def sharedLibraryLinkFile = createFile()

        when:
        binary.sharedLibraryFile = sharedLibraryFile
        binary.sharedLibraryLinkFile = sharedLibraryLinkFile

        then:
        binary.linkFiles.files == [sharedLibraryLinkFile] as Set
        binary.runtimeFiles.files == [sharedLibraryFile] as Set
    }

    def createFile() {
        def file = Stub(File) {
            exists() >> true
            isFile() >> true
        }
    }
}
