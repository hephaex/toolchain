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
package org.gradle.nativebinaries.internal.resolve

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.UnknownProjectException
import org.gradle.api.internal.DefaultDomainObjectSet
import org.gradle.api.internal.plugins.ExtensionContainerInternal
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.nativebinaries.NativeLibraryBinary
import org.gradle.nativebinaries.NativeLibraryRequirement
import org.gradle.nativebinaries.NativeBinarySpec
import org.gradle.nativebinaries.NativeLibrarySpec
import org.gradle.nativebinaries.internal.ProjectNativeLibraryRequirement
import org.gradle.runtime.base.ComponentSpecContainer
import spock.lang.Specification

// TODO:DAZ Improve test names, at the very least
class ProjectLibraryBinaryLocatorTest extends Specification {
    def project = Mock(ProjectInternal)
    def projectLocator = Mock(ProjectLocator)
    def requirement = Mock(NativeLibraryRequirement)
    def library = Mock(NativeLibrarySpec)
    def binary = Mock(MockNativeLibraryBinary)
    def binaries = new DefaultDomainObjectSet(NativeBinarySpec, [binary])
    def convertedBinaries = new DefaultDomainObjectSet(NativeLibraryBinary, [binary])
    def locator = new ProjectLibraryBinaryLocator(projectLocator)

    def setup() {
        library.binaries >> binaries
    }

    def "locates binaries for library in same project"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("libName", null)

        and:
        projectLocator.locateProject(null) >> project
        findLibraryInProject()

        then:
        locator.getBinaries(requirement) == convertedBinaries
    }

    def "locates binaries for library in other project"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("other", "libName", null)

        and:
        projectLocator.locateProject("other") >> project
        findLibraryInProject()

        then:
        locator.getBinaries(requirement) == convertedBinaries
    }

    def "parses map notation for library with static linkage"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("other", "libName", "static")

        and:
        projectLocator.locateProject("other") >> project
        findLibraryInProject()

        then:
        locator.getBinaries(requirement) == convertedBinaries
    }

    def "fails for unknown project"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("unknown", "libName", "static")

        and:
        projectLocator.locateProject("unknown") >> { throw new UnknownProjectException("unknown")}

        and:
        locator.getBinaries(requirement)

        then:
        thrown(UnknownProjectException)
    }

    def "fails for unknown library"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("other", "unknown", "static")

        and:
        projectLocator.locateProject("other") >> project
        def libraries = findLibraryContainer(project)
        libraries.getByName("unknown") >> { throw new UnknownDomainObjectException("unknown") }

        and:
        locator.getBinaries(requirement)

        then:
        thrown(UnknownDomainObjectException)
    }

    def "fails when project does not have libraries"() {
        when:
        requirement = new ProjectNativeLibraryRequirement("other", "libName", "static")

        and:
        projectLocator.locateProject("other") >> project
        def extensions = Mock(ExtensionContainerInternal)
        project.getExtensions() >> extensions
        extensions.findByName("libraries") >> null
        project.path >> "project-path"

        and:
        locator.getBinaries(requirement)

        then:
        def e = thrown(LibraryResolveException)
        e.message == "Project does not have a libraries container: 'project-path'"
    }

    private void findLibraryInProject() {
        def libraries = findLibraryContainer(project)
        libraries.getByName("libName") >> library
    }

    private findLibraryContainer(ProjectInternal project) {
        def extensions = Mock(ExtensionContainerInternal)
        def components = Mock(ComponentSpecContainer)
        def libraryContainer = Mock(NamedDomainObjectSet)
        project.getExtensions() >> extensions
        extensions.findByType(ComponentSpecContainer) >> components
        components.withType(NativeLibrarySpec) >> libraryContainer
        return libraryContainer
    }

    interface MockNativeLibraryBinary extends NativeBinarySpec, NativeLibraryBinary {}

}
