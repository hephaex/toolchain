/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.api.internal.artifacts.ivyservice.resolutionstrategy;


import org.gradle.api.Action
import org.gradle.api.internal.artifacts.DependencyResolveDetailsInternal
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.VersionSelectionReasons
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector
import static org.gradle.util.Assertions.assertThat

public class DefaultResolutionStrategySpec extends Specification {

    def cachePolicy = Mock(DefaultCachePolicy)
    def strategy = new DefaultResolutionStrategy(cachePolicy, [] as Set)

    def "allows setting forced modules"() {
        expect:
        strategy.forcedModules.empty

        when:
        strategy.force 'org.foo:bar:1.0', 'org.foo:baz:2.0'

        then:
        def versions = strategy.forcedModules as List
        versions.size() == 2

        versions[0].group == 'org.foo'
        versions[0].name == 'bar'
        versions[0].version == '1.0'

        versions[1].group == 'org.foo'
        versions[1].name == 'baz'
        versions[1].version == '2.0'
    }

    def "allows replacing forced modules"() {
        given:
        strategy.force 'org.foo:bar:1.0'

        when:
        strategy.forcedModules = ['hello:world:1.0', [group:'g', name:'n', version:'1']]

        then:
        def versions = strategy.forcedModules as List
        versions.size() == 2
        versions[0].group == 'hello'
        versions[1].group == 'g'
    }

    def "provides no op resolve rule when no rules or forced modules configured"() {
        given:
        def details = Mock(DependencyResolveDetailsInternal)

        when:
        strategy.dependencyResolveRule.execute(details)

        then:
        0 * details._
    }

    def "provides dependency resolve rule that forces modules"() {
        given:
        strategy.force 'org:bar:1.0', 'org:foo:2.0'
        def details = Mock(DependencyResolveDetailsInternal)

        when:
        strategy.dependencyResolveRule.execute(details)

        then:
        _ * details.getRequested() >> newSelector("org", "foo", "1.0")
        1 * details.useVersion("2.0", VersionSelectionReasons.FORCED)
        0 * details._
    }

    def "provides dependency resolve rule that orderly aggregates user specified rules"() {
        given:
        strategy.eachDependency({ it.useVersion("1.0") } as Action)
        strategy.eachDependency({ it.useVersion("2.0") } as Action)
        def details = Mock(DependencyResolveDetailsInternal)

        when:
        strategy.dependencyResolveRule.execute(details)

        then:
        1 * details.useVersion("1.0")
        then:
        1 * details.useVersion("2.0")
        0 * details._
    }

    def "provides dependency resolve rule with forced modules first and then user specified rules"() {
        given:
        strategy.force 'org:bar:1.0', 'org:foo:2.0'
        strategy.eachDependency({ it.useVersion("5.0") } as Action)
        strategy.eachDependency({ it.useVersion("6.0") } as Action)

        def details = Mock(DependencyResolveDetailsInternal)

        when:
        strategy.dependencyResolveRule.execute(details)

        then: //forced modules:
        _ * details.requested >> newSelector("org", "foo", "1.0")
        1 * details.useVersion("2.0", VersionSelectionReasons.FORCED)

        then: //user rules, in order:
        1 * details.useVersion("5.0")
        then:
        1 * details.useVersion("6.0")
        0 * details._
    }

    def "copied instance does not share state"() {
        when:
        def copy = strategy.copy()

        then:
        !copy.is(strategy)
        assertThat(copy).doesNotShareStateWith(strategy)
    }

    def "provides a copy"() {
        given:
        def newCachePolicy = Mock(DefaultCachePolicy)
        cachePolicy.copy() >> newCachePolicy

        strategy.failOnVersionConflict()
        strategy.force("org:foo:1.0")
        strategy.eachDependency(Mock(Action))

        when:
        def copy = strategy.copy()

        then:
        copy.forcedModules == strategy.forcedModules
        copy.dependencyResolveRules == strategy.dependencyResolveRules
        copy.conflictResolution instanceof StrictConflictResolution

        strategy.cachePolicy == cachePolicy
        copy.cachePolicy == newCachePolicy
    }

    def "configures changing modules cache with jdk5+ units"() {
        when:
        strategy.cacheChangingModulesFor(30000, "milliseconds")

        then:
        1 * cachePolicy.cacheChangingModulesFor(30000, TimeUnit.MILLISECONDS)
    }

    def "configures changing modules cache with jdk6+ units"() {
        when:
        strategy.cacheChangingModulesFor(5, "minutes")

        then:
        1 * cachePolicy.cacheChangingModulesFor(5 * 60 * 1000, TimeUnit.MILLISECONDS)
    }

    def "configures dynamic version cache with jdk5+ units"() {
        when:
        strategy.cacheDynamicVersionsFor(10000, "milliseconds")

        then:
        1 * cachePolicy.cacheDynamicVersionsFor(10000, TimeUnit.MILLISECONDS)
    }

    def "configures dynamic version cache with jdk6+ units"() {
        when:
        strategy.cacheDynamicVersionsFor(1, "hours")

        then:
        1 * cachePolicy.cacheDynamicVersionsFor(1 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
    }
}