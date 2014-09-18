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
package org.gradle.api.internal.artifacts.ivyservice

import org.gradle.api.Action
import org.gradle.api.internal.artifacts.DefaultModuleVersionSelector
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.VersionSelectionReasons
import org.gradle.api.internal.artifacts.metadata.DependencyMetaData
import spock.lang.Specification

import static org.gradle.api.internal.artifacts.ivyservice.IvyUtil.createModuleRevisionId

class VersionForcingDependencyToModuleResolverSpec extends Specification {
    final target = Mock(DependencyToModuleVersionIdResolver)
    final resolvedVersion = Mock(ModuleVersionIdResolveResult)
    final forced = createModuleRevisionId('group', 'module', 'forced')

    def "passes through dependency when it does not match any rule"() {
        def dep = dependency('org', 'module', '1.0')
        def rule = Mock(Action)
        def resolver = new VersionForcingDependencyToModuleResolver(target, rule)

        when:
        def result = resolver.resolve(dep)

        then:
        result == resolvedVersion

        and:
        1 * target.resolve(dep) >> resolvedVersion
        1 * rule.execute( {it.requested.group == 'org' && it.requested.name == 'module' && it.requested.version == '1.0'} )
        0 * target._
    }

    def "replaces dependency by rule"() {
        def dep = dependency('org', 'module', '0.5')
        def modified = dependency('org', 'module', '1.0')

        def force = { it.useVersion("1.0") } as Action

        def resolver = new VersionForcingDependencyToModuleResolver(target, force)

        when:
        SubstitutedModuleVersionIdResolveResult result = resolver.resolve(dep)

        then:
        result.result == resolvedVersion
        result.selectionReason == VersionSelectionReasons.SELECTED_BY_RULE

        and:
        1 * dep.withRequestedVersion(DefaultModuleVersionSelector.newInstance("org", "module", "1.0")) >> modified
        1 * target.resolve(modified) >> resolvedVersion
        0 * target._
    }

    def "explosive rule yields failure result that provides context"() {
        def force = { throw new Error("Boo!") } as Action
        def resolver = new VersionForcingDependencyToModuleResolver(target, force)

        when:
        def result = resolver.resolve(dependency('org', 'module', '0.5'))

        then:
        result.failure.message == "Could not resolve org:module:0.5."
        result.failure.cause.message == 'Boo!'
        result.selectionReason == VersionSelectionReasons.REQUESTED
    }

    def "failed result uses correct exception"() {
        def force = { throw new Error("Boo!") } as Action
        def resolver = new VersionForcingDependencyToModuleResolver(target, force)
        def result = resolver.resolve(dependency('org', 'module', '0.5'))

        when:
        result.getId()

        then:
        def ex = thrown(ModuleVersionResolveException)
        ex == result.failure

        when:
        result.resolve()

        then:
        def ex2 = thrown(ModuleVersionResolveException)
        ex2 == result.failure
    }

    def dependency(String group, String module, String version) {
        Mock(DependencyMetaData) {
            getRequested() >> new DefaultModuleVersionSelector(group, module, version)
        }
    }
}
