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


package org.gradle.api.internal.artifacts.metadata

import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.component.ModuleComponentIdentifier

class DefaultMavenModuleVersionMetaDataTest extends AbstractModuleVersionMetaDataTest {

    @Override
    AbstractModuleVersionMetaData createMetaData(ModuleVersionIdentifier id, ModuleDescriptor moduleDescriptor, ModuleComponentIdentifier componentIdentifier) {
        return new DefaultMavenModuleVersionMetaData(id, moduleDescriptor, componentId, "pom", false)
    }

    def "can make a copy"() {
        def dependency1 = Stub(DependencyMetaData)
        def dependency2 = Stub(DependencyMetaData)

        given:
        metaData.changing = true
        metaData.dependencies = [dependency1, dependency2]
        metaData.status = 'a'
        metaData.statusScheme = ['a', 'b', 'c']

        when:
        def copy = metaData.copy()

        then:
        copy != metaData
        copy.descriptor == moduleDescriptor
        copy.changing
        copy.dependencies == [dependency1, dependency2]
        copy.status == 'a'
        copy.statusScheme == ['a', 'b', 'c']
        copy.packaging == "pom"
        !copy.relocated
    }

    def "recognises pom packaging"() {
        when:
        def metaData = new DefaultMavenModuleVersionMetaData(id, moduleDescriptor, componentId, packaging, false)

        then:
        metaData.packaging == packaging
        metaData.isPomPackaging() == isPom
        metaData.isKnownJarPackaging() == isJar

        where:
        packaging      | isPom | isJar
        "pom"          | true  | false
        "jar"          | false | true
        "war"          | false | false
        "maven-plugin" | false | true
    }
}
