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

package org.gradle.api.internal.artifacts.ivyservice.ivyresolve
import org.gradle.api.internal.artifacts.ivyservice.ModuleVersionResolveException
import spock.lang.Specification

import static org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector
import static org.gradle.api.internal.artifacts.ivyservice.ivyresolve.BuildableModuleVersionSelectionResolveResult.State.*

class DefaultBuildableModuleVersionSelectionResolveResultTest extends Specification {
    def descriptor = new DefaultBuildableModuleVersionSelectionResolveResult()
    def listing = Mock(ModuleVersionListing)

    def "has unknown state by default"() {
        expect:
        descriptor.state == Unknown
        !descriptor.hasResult()
    }

    def "can mark as listed"() {
        when:
        descriptor.listed(listing)

        then:
        descriptor.state == Listed
        descriptor.failure == null
        descriptor.hasResult()
    }

    def "can mark as listed using version strings"() {
        when:
        descriptor.listed(['1.2', '1.3'])

        then:
        descriptor.state == Listed
        descriptor.versions.versions*.version as Set == ['1.2', '1.3'] as Set
    }

    def "can mark as probably listed"() {
        when:
        descriptor.probablyListed(listing)

        then:
        descriptor.state == ProbablyListed
        descriptor.failure == null
        descriptor.hasResult()
    }

    def "can mark as failed"() {
        def failure = new ModuleVersionResolveException(newSelector("a", "b", "c"), "broken")

        when:
        descriptor.failed(failure)

        then:
        descriptor.state == Failed
        descriptor.failure == failure
        descriptor.hasResult()
    }

    def "cannot get failure when has no result"() {
        when:
        descriptor.failure

        then:
        thrown(IllegalStateException)
    }

    def "cannot get listing when has no result"() {
        when:
        descriptor.versions

        then:
        thrown(IllegalStateException)
    }
}
