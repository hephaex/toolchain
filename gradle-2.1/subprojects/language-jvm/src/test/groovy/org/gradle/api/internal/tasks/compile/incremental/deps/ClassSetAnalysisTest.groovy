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

package org.gradle.api.internal.tasks.compile.incremental.deps

import spock.lang.Specification

import static org.gradle.api.internal.tasks.compile.incremental.deps.DefaultDependentsSet.dependents

class ClassSetAnalysisTest extends Specification {

    ClassSetAnalysis analysis(Map<String, DependentsSet> dependents) {
        new ClassSetAnalysis(new DefaultClassSetAnalysisData(dependents))
    }

    def "returns empty analysis"() {
        def a = analysis([:])
        expect: a.getRelevantDependents("Foo").dependentClasses.isEmpty()
    }

    def "does not recurse if root class is a dependency to all"() {
        def a = analysis(["Foo": new DefaultDependentsSet(true, ["Bar"])])
        def deps = a.getRelevantDependents("Foo")

        expect:
        deps.dependencyToAll

        when: deps.getDependentClasses()
        then: thrown(UnsupportedOperationException)
    }

    def "marks as dependency to all only if root class is a dependency to all"() {
        def a = analysis([
                "a":   new DefaultDependentsSet(false, ['b']),
                'b': new DefaultDependentsSet(true, ['c']),
                "c": new DefaultDependentsSet(true, [])
        ])
        def deps = a.getRelevantDependents("a")

        expect:
        deps.dependentClasses == ['b', 'c'] as Set
        !deps.dependencyToAll
    }

    def "recurses nested dependencies"() {
        def a = analysis([
                "Foo": dependents("Bar"),
                "Bar": dependents("Baz"),
                "Baz": dependents(),
        ])
        def deps = a.getRelevantDependents("Foo")

        expect:
        deps.dependentClasses == ["Bar", "Baz"] as Set
        a.getRelevantDependents("Bar").dependentClasses == ["Baz"] as Set
        a.getRelevantDependents("Baz").dependentClasses == [] as Set
    }

    def "recurses multiple dependencies"() {
        def a = analysis([
                "a": dependents("b", "c"),
                "b": dependents("d"),
                "c": dependents("e"),
                "d": dependents(),
                "e": dependents()
        ])
        def deps = a.getRelevantDependents("a")

        expect:
        deps.dependentClasses == ["b", "c", "d", "e"] as Set
    }

    def "removes self from dependents"() {
        def a = analysis([
                "Foo": dependents("Foo")
        ])
        def deps = a.getRelevantDependents("Foo")

        expect:
        deps.dependentClasses == [] as Set
    }

    def "handles dependency cycles"() {
        def a = analysis([
                "Foo": dependents("Bar"),
                "Bar": dependents("Baz"),
                "Baz": dependents("Foo"),
        ])
        def deps = a.getRelevantDependents("Foo")

        expect:
        deps.dependentClasses == ["Bar", "Baz"] as Set
    }

    def "recurses but filters out inner classes"() {
        def a = analysis([
                "a":   dependents('a$b', 'c'),
                'a$b': dependents('d'),
                "c": dependents(),
                "d": dependents(),
        ])
        def deps = a.getRelevantDependents("a")

        expect:
        deps.dependentClasses == ["c", "d"] as Set
    }

    def "handles cycles with inner classes"() {
        def a = analysis([
                "a":   dependents('a$b'),
                'a$b': dependents('a$b', 'c'),
                "c": dependents()
        ])
        def deps = a.getRelevantDependents("a")

        expect:
        deps.dependentClasses == ["c"] as Set
    }

    def "provides dependents of all input classes"() {
        def a = analysis([
                "A": dependents("B"), "B": dependents(),
                "C": dependents("D"), "D": dependents(),
                "E": dependents("D"), "F": dependents(),
        ])
        def deps = a.getRelevantDependents(["A", "E"])

        expect:
        deps.dependentClasses == ["D", "B"] as Set
    }

    def "provides recursive dependents of all input classes"() {
        def a = analysis([
                "A": dependents("B"), "B": dependents("C"), "C": dependents(),
                "D": dependents("E"), "E": dependents(),
                "F": dependents("G"), "G": dependents(),
        ])
        def deps = a.getRelevantDependents(["A", "D"])

        expect:
        deps.dependentClasses == ["E", "B", "C"] as Set
    }

    def "knows when any of the input classes is a dependency to all"() {
        def a = analysis([
                "A": dependents("B"), "B": dependents(),
                "C": new DefaultDependentsSet(true, []),
                "D": dependents("E"), "E": dependents(),
        ])
        def deps = a.getRelevantDependents(["A", "C", "will not be reached"])

        expect:
        deps.dependencyToAll
    }

    def "knows when input class is a dependency to all"() {
        def a = analysis([
                "A": dependents("B"), "B": dependents(),
                "C": new DefaultDependentsSet(true, []),
        ])
        expect:
        !a.isDependencyToAll("A")
        a.isDependencyToAll("C")
        !a.isDependencyToAll("Unknown")
    }
}
