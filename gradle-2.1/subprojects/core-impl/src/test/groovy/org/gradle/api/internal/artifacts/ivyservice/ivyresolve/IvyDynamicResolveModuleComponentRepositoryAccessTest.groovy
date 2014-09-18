/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.api.internal.artifacts.ivyservice.ivyresolve

import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.internal.artifacts.ivyservice.IvyUtil
import org.gradle.api.internal.artifacts.metadata.DependencyMetaData
import org.gradle.api.internal.artifacts.metadata.MutableModuleVersionMetaData
import spock.lang.Specification

class IvyDynamicResolveModuleComponentRepositoryAccessTest extends Specification {
    final target = Mock(ModuleComponentRepositoryAccess)
    final metaData = Mock(MutableModuleVersionMetaData)
    final requestedDependency = Mock(DependencyMetaData)
    final moduleComponentId = Mock(ModuleComponentIdentifier)
    final result = Mock(BuildableModuleVersionMetaDataResolveResult)
    final ModuleComponentRepositoryAccess access = new IvyDynamicResolveModuleComponentRepositoryAccess(target)

    def "replaces each dependency version with revConstraint"() {
        def original = dependency('1.2+')
        def transformed = dependency()

        given:
        result.state >> BuildableModuleVersionMetaDataResolveResult.State.Resolved
        result.metaData >> metaData

        when:
        access.resolveComponentMetaData(requestedDependency, moduleComponentId, result)

        then:
        1 * target.resolveComponentMetaData(requestedDependency, moduleComponentId, result)

        and:
        1 * metaData.dependencies >> [original]
        1 * original.withRequestedVersion('1.2+') >> transformed
        1 * metaData.setDependencies([transformed])
    }

    def "does nothing when dependency has not been resolved"() {
        when:
        access.resolveComponentMetaData(requestedDependency, moduleComponentId, result)

        then:
        1 * target.resolveComponentMetaData(requestedDependency, moduleComponentId, result)
        _ * result.state >> BuildableModuleVersionMetaDataResolveResult.State.Missing
        0 * result._
    }

    def dependency(String revConstraint = '1.0') {
        def dep = Mock(DependencyMetaData)
        def descriptor = Mock(DependencyDescriptor)
        _ * descriptor.dynamicConstraintDependencyRevisionId >> IvyUtil.createModuleRevisionId('org', 'module', revConstraint)
        _ * dep.descriptor >> descriptor
        return dep
    }
}
