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

package org.gradle.runtime.base.library
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.language.base.FunctionalSourceSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.DefaultFunctionalSourceSet
import org.gradle.runtime.base.ComponentSpecIdentifier
import org.gradle.runtime.base.ModelInstantiationException
import spock.lang.Specification

class DefaultLibrarySpecTest extends Specification {
    def instantiator = new DirectInstantiator()
    def libraryId = Mock(ComponentSpecIdentifier)
    FunctionalSourceSet functionalSourceSet;

    def setup() {
        functionalSourceSet = new DefaultFunctionalSourceSet("testFSS", new DirectInstantiator());
    }

    def "library has name and path"() {
        def library = DefaultLibrarySpec.create(DefaultLibrarySpec, libraryId, functionalSourceSet, instantiator)

        when:
        _ * libraryId.name >> "jvm-lib"
        _ * libraryId.projectPath >> ":project-path"

        then:
        library.name == "jvm-lib"
        library.projectPath == ":project-path"
        library.displayName == "DefaultLibrarySpec 'jvm-lib'"
    }

    def "has sensible display name"() {
        def library = DefaultLibrarySpec.create(MySampleLibrary, libraryId, functionalSourceSet, instantiator)

        when:
        _ * libraryId.name >> "jvm-lib"

        then:
        library.displayName == "MySampleLibrary 'jvm-lib'"
    }

    def "create fails if subtype does not have a public no-args constructor"() {

        when:
        DefaultLibrarySpec.create(MyConstructedLibrary, libraryId, functionalSourceSet, instantiator)

        then:
        def e = thrown ModelInstantiationException
        e.message == "Could not create library of type MyConstructedLibrary"
        e.cause instanceof IllegalArgumentException
        e.cause.message.startsWith "Could not find any public constructor for class"
    }

    def "contains sources of associated main sourceSet"() {
        when:
        def lss1 = languageSourceSet("lss1")
        functionalSourceSet.add(lss1)

        def library = DefaultLibrarySpec.create(MySampleLibrary, libraryId, functionalSourceSet, instantiator)

        and:
        def lss2 = languageSourceSet("lss2")
        functionalSourceSet.add(lss2)

        then:
        library.getSource() as List == [lss1, lss2]
    }

    def languageSourceSet(String name) {
        Stub(LanguageSourceSet) {
            getName() >> name
        }
    }

    static class MySampleLibrary extends DefaultLibrarySpec {}
    static class MyConstructedLibrary extends DefaultLibrarySpec {
        MyConstructedLibrary(String arg) {}
    }
}
