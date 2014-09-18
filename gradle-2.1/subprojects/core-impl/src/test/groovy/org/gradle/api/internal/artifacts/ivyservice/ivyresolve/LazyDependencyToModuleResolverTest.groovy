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

import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.gradle.api.artifacts.ModuleVersionSelector
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier
import org.gradle.api.internal.artifacts.DefaultModuleVersionSelector
import org.gradle.api.internal.artifacts.ivyservice.ArtifactResolver
import org.gradle.api.internal.artifacts.ivyservice.DependencyToModuleVersionResolver
import org.gradle.api.internal.artifacts.ivyservice.IvyUtil
import org.gradle.api.internal.artifacts.ivyservice.ModuleVersionResolveException
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionMatcher
import org.gradle.api.internal.artifacts.metadata.DefaultIvyModuleVersionMetaData
import org.gradle.api.internal.artifacts.metadata.DependencyMetaData
import org.gradle.api.internal.artifacts.metadata.ModuleVersionArtifactIdentifier
import org.gradle.api.internal.artifacts.metadata.ModuleVersionArtifactMetaData
import spock.lang.Specification

import static org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier.newId
import static org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector

class LazyDependencyToModuleResolverTest extends Specification {
    final target = Mock(DependencyToModuleVersionResolver)
    final matcher = Mock(VersionMatcher)
    final LazyDependencyToModuleResolver resolver = new LazyDependencyToModuleResolver(target, matcher)

    def "does not resolve module for static version dependency until requested"() {
        def dependency = dependency()
        def metaData = module()

        when:
        def idResolveResult = resolver.resolve(dependency)

        then:
        idResolveResult.id == metaData.id

        and:
        0 * target._

        when:
        def moduleResolveResult = idResolveResult.resolve()

        then:
        moduleResolveResult.id == metaData.id

        moduleResolveResult.metaData == metaData

        1 * target.resolve(dependency, _) >> { args -> args[1].resolved(metaData)}
        0 * target._
    }

    def "resolves module for dynamic version dependency immediately"() {
        def dependency = dependency()
        def metaData = module()

        given:
        matcher.isDynamic(_) >> true

        when:
        def idResolveResult = resolver.resolve(dependency)
        def id = idResolveResult.id

        then:
        id == metaData.id

        and:
        1 * target.resolve(dependency, _) >> { args -> args[1].resolved(metaData)}
        0 * target._

        when:
        def moduleResolveResult = idResolveResult.resolve()

        then:
        moduleResolveResult.id == metaData.id
        moduleResolveResult.metaData == metaData

        and:
        0 * target._
    }

    def moduleIdentifier(ModuleDescriptor moduleDescriptor) {
        return new DefaultModuleVersionIdentifier(moduleDescriptor.moduleRevisionId.organisation, moduleDescriptor.moduleRevisionId.name, moduleDescriptor.moduleRevisionId.revision)
    }

    def "does not resolve module more than once"() {
        def dependency = dependency()
        def module = module()

        when:
        def idResolveResult = resolver.resolve(dependency)
        idResolveResult.resolve()

        then:
        1 * target.resolve(dependency, _) >> { args -> args[1].resolved(module.moduleRevisionId, module, Mock(ArtifactResolver))}
        0 * target._

        when:
        idResolveResult.resolve()

        then:
        0 * target._
    }

    def "collects failure to resolve module"() {
        def dependency = dependency()
        def failure = new ModuleVersionResolveException(newSelector("a", "b", "c"), "broken")

        when:
        def idFailureResult = resolver.resolve(dependency)

        then:
        idFailureResult.failure == null;

        and:
        0 * target._

        when:
        def resolveResult = idFailureResult.resolve()

        then:
        resolveResult.failure.is(failure)

        and:
        1 * target.resolve(dependency, _) >> { args -> args[1].failed(failure)}
        0 * target._

        when:
        resolveResult.metaData

        then:
        ModuleVersionResolveException e = thrown()
        e.is(resolveResult.failure)

        and:
        0 * target._
    }

    def "rethrows resolution failure"() {
        def dependency = dependency()
        def failure = new ModuleVersionResolveException(newId("org", "a", "1.2"), "%s is broken")

        when:
        def resolveResult = resolver.resolve(dependency).resolve()

        then:
        resolveResult.failure == failure

        and:
        1 * target.resolve(dependency, _) >> { args -> args[1].failed(failure)}
    }

    def "collects and wraps unexpected module resolve failure"() {
        def dependency = dependency()
        def failure = new RuntimeException("broken")

        when:
        def resolveResult = resolver.resolve(dependency).resolve()

        then:
        resolveResult.failure instanceof ModuleVersionResolveException
        resolveResult.failure.message == "Could not resolve group:module:1.0."

        and:
        1 * target.resolve(dependency, _) >> { throw failure }
    }

    def module() {
        ModuleRevisionId id = IvyUtil.createModuleRevisionId("group", "module", "1.0")
        return new DefaultIvyModuleVersionMetaData(new DefaultModuleDescriptor(id, "release", new Date()))
    }

    def dependency() {
        DependencyDescriptor descriptor = Mock()
        ModuleRevisionId id = IvyUtil.createModuleRevisionId("group", "module", "1.0")
        _ * descriptor.dependencyRevisionId >> id
        DependencyMetaData metaData = Mock()
        _ * metaData.descriptor >> descriptor
        ModuleVersionSelector requested = DefaultModuleVersionSelector.newSelector("group", "module", "1.0")
        _ * metaData.requested >> requested
        return metaData
    }

    def artifact() {
        Artifact artifact = Mock()
        _ * artifact.moduleRevisionId >> IvyUtil.createModuleRevisionId("group", "module", "1.0")
        _ * artifact.name >> 'artifact'
        _ * artifact.ext >> 'zip'
        return Stub(ModuleVersionArtifactMetaData) {
            getArtifact() >> artifact
            getId() >> Stub(ModuleVersionArtifactIdentifier) {
                getDisplayName() >> "artifact.zip"
            }
        }
    }
}
