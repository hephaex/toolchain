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

package org.gradle.api.internal.tasks.compile.incremental.deps

import org.gradle.messaging.serialize.InputStreamBackedDecoder
import org.gradle.messaging.serialize.OutputStreamBackedEncoder
import spock.lang.Specification
import spock.lang.Subject

import static org.gradle.api.internal.tasks.compile.incremental.deps.DefaultDependentsSet.dependents

class ClassSetAnalysisDataSerializerTest extends Specification {

    @Subject serializer = new ClassSetAnalysisDataSerializer()

    def "serializes"() {
        def data = new DefaultClassSetAnalysisData(
                ["A": dependents("B", "C"), "B": new DefaultDependentsSet(true, ["C"]), "C": dependents(), "D": new DependencyToAll(), ])
        def os = new ByteArrayOutputStream()
        def e = new OutputStreamBackedEncoder(os)

        when:
        serializer.write(e, data)
        DefaultClassSetAnalysisData read = serializer.read(new InputStreamBackedDecoder(new ByteArrayInputStream(os.toByteArray())))

        then:
        read.dependents.keySet() == data.dependents.keySet()

        ["A", "B", "C"].each {
            assert read.dependents[it].dependentClasses == data.dependents[it].dependentClasses
            assert read.dependents[it].dependencyToAll == data.dependents[it].dependencyToAll
        }

        read.dependents["D"] instanceof DependencyToAll
    }
}
