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

import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.VersionSelectionReasons
import spock.lang.Specification

import static org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector

class DefaultDependencyResolveDetailsSpec extends Specification {

    def "can specify version to use"() {
        def requested = newSelector("org", "foo", "1.0")

        when:
        def details = new DefaultDependencyResolveDetails(requested)

        then:
        details.requested == requested
        details.target == requested
        !details.updated
        !details.selectionReason

        when:
        details.useVersion("1.0") //the same version

        then:
        details.requested == requested
        details.target == requested
        details.updated
        details.selectionReason == VersionSelectionReasons.SELECTED_BY_RULE

        when:
        details.useVersion("2.0") //different version

        then:
        details.requested == requested
        details.target != requested
        details.updated
        details.selectionReason == VersionSelectionReasons.SELECTED_BY_RULE

        details.target.version == "2.0"
        details.target.name == requested.name
        details.target.group == requested.group
    }

    def "can specify version with selection reason"() {
        def requested = newSelector("org", "foo", "1.0")
        def details = new DefaultDependencyResolveDetails(requested)

        when:
        details.useVersion("1.0", VersionSelectionReasons.FORCED) //same version

        then:
        details.requested == requested
        details.target == requested
        details.updated
        details.selectionReason == VersionSelectionReasons.FORCED

        when:
        details.useVersion("3.0", VersionSelectionReasons.FORCED) //different version

        then:
        details.requested == requested
        details.target.version == "3.0"
        details.target.name == requested.name
        details.target.group == requested.group
        details.updated
        details.selectionReason == VersionSelectionReasons.FORCED
    }

    def "can override version and selection reason"() {
        def requested = newSelector("org", "foo", "1.0")
        def details = new DefaultDependencyResolveDetails(requested)

        when:
        details.useVersion("2.0", VersionSelectionReasons.FORCED)
        details.useVersion("3.0", VersionSelectionReasons.SELECTED_BY_RULE)

        then:
        details.requested == requested
        details.target.version == "3.0"
        details.target.name == requested.name
        details.target.group == requested.group
        details.updated
        details.selectionReason == VersionSelectionReasons.SELECTED_BY_RULE
    }

    def "does not allow null version"() {
        def details = new DefaultDependencyResolveDetails(newSelector("org", "foo", "1.0"))

        when:
        details.useVersion(null)

        then:
        thrown(IllegalArgumentException)

        when:
        details.useVersion(null, VersionSelectionReasons.SELECTED_BY_RULE)

        then:
        thrown(IllegalArgumentException)
    }

    def "can specify target module"() {
        def details = new DefaultDependencyResolveDetails(newSelector("org", "foo", "1.0"))

        when:
        details.useTarget("org:bar:2.0")

        then:
        details.target.toString() == 'org:bar:2.0'
        details.updated
        details.selectionReason == VersionSelectionReasons.SELECTED_BY_RULE
    }

    def "can mix configuring version and target module"() {
        def details = new DefaultDependencyResolveDetails(newSelector("org", "foo", "1.0"))

        when:
        details.useVersion("1.5")

        then:
        details.target.toString() == 'org:foo:1.5'

        when:
        details.useTarget("com:bar:3.0")

        then:
        details.target.toString() == 'com:bar:3.0'

        when:
        details.useVersion('5.0')

        then:
        details.target.toString() == 'com:bar:5.0'
    }
}
