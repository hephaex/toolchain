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

package org.gradle.api.internal.artifacts.ivyservice.resolveengine.result

import spock.lang.Specification

import static org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.VersionSelectionReasons.*

class VersionSelectionReasonsTest extends Specification {

    def "decorates with conflict resolution"() {
        expect:
        withConflictResolution(REQUESTED) == CONFLICT_RESOLUTION
        withConflictResolution(SELECTED_BY_RULE) == CONFLICT_RESOLUTION_BY_RULE
        withConflictResolution(CONFLICT_RESOLUTION) == CONFLICT_RESOLUTION
        withConflictResolution(CONFLICT_RESOLUTION_BY_RULE) == CONFLICT_RESOLUTION_BY_RULE
    }

    def "does not decorate unsupported reasons"() {
        when:
        withConflictResolution(FORCED)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains FORCED.toString()
    }
}
