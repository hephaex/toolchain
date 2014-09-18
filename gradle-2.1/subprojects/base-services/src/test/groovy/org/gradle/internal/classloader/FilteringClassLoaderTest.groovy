/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.internal.classloader

import org.junit.Before
import org.junit.Test
import org.junit.runners.BlockJUnit4ClassRunner
import spock.lang.Specification

import static org.junit.Assert.fail

class FilteringClassLoaderTest extends Specification {
    private final FilteringClassLoader classLoader = new FilteringClassLoader(FilteringClassLoaderTest.class.getClassLoader())

    void passesThroughSystemClasses() {
        expect:
        canLoadClass(String)
    }

    void passesThroughSystemPackages() {
        expect:
        canSeePackage('java.lang')
    }

    void passesThroughSystemResources() {
        expect:
        canSeeResource('com/sun/jndi/ldap/jndiprovider.properties')
    }

    void filtersClassesByDefault() {
        given:
        classLoader.parent.loadClass(Test.class.name)

        when:
        classLoader.loadClass(Test.class.name, false)

        then:
        ClassNotFoundException e = thrown()
        e.message == "$Test.name not found."

        when:
        classLoader.loadClass(Test.class.name)

        then:
        ClassNotFoundException e2 = thrown()
        e2.message == "$Test.name not found."
    }

    void filtersPackagesByDefault() {
        given:
        assert classLoader.parent.getPackage('org.junit') != null

        expect:
        cannotSeePackage('org.junit')
    }

    void filtersResourcesByDefault() {
        given:
        assert classLoader.parent.getResource('org/gradle/util/ClassLoaderTest.txt') != null

        expect:
        cannotSeeResource('org/gradle/util/ClassLoaderTest.txt')
    }

    void passesThroughClassesInSpecifiedPackagesAndSubPackages() {
        given:
        cannotLoadClass(Test)
        cannotLoadClass(BlockJUnit4ClassRunner)

        and:
        classLoader.allowPackage('org.junit')

        expect:
        canLoadClass(Test)
        canLoadClass(Before)
        canLoadClass(BlockJUnit4ClassRunner)
    }

    void passesThroughSpecifiedClasses() {
        given:
        cannotLoadClass(Test)

        and:
        classLoader.allowClass(Test.class)

        expect:
        canLoadClass(Test)
        cannotLoadClass(Before)
    }

    void filtersSpecifiedClasses() {
        given:
        cannotLoadClass(Test)
        cannotLoadClass(Before)

        and:
        classLoader.allowPackage("org.junit")
        classLoader.disallowClass("org.junit.Test")

        expect:
        canLoadClass(Before)
        cannotLoadClass(Test)
    }

    void disallowClassWinsOverAllowClass() {
        given:
        classLoader.allowClass(Test)
        classLoader.disallowClass(Test.name)

        expect:
        cannotLoadClass(Test)
    }

    void passesThroughSpecifiedPackagesAndSubPackages() {
        given:
        cannotSeePackage('org.junit')
        cannotSeePackage('org.junit.runner')

        and:
        classLoader.allowPackage('org.junit')

        expect:
        canSeePackage('org.junit')
        canSeePackage('org.junit.runner')
    }

    void passesThroughResourcesInSpecifiedPackages() {
        given:
        cannotSeeResource('org/gradle/util/ClassLoaderTest.txt')

        classLoader.allowPackage('org.gradle')

        expect:
        canSeeResource('org/gradle/util/ClassLoaderTest.txt')
    }

    void passesThroughResourcesWithSpecifiedPrefix() {
        given:
        cannotSeeResource('org/gradle/util/ClassLoaderTest.txt')

        and:
        classLoader.allowResources('org/gradle')

        expect:
        canSeeResource('org/gradle/util/ClassLoaderTest.txt')
    }

    void passesThroughSpecifiedResources() {
        given:
        cannotSeeResource('org/gradle/util/ClassLoaderTest.txt')

        and:
        classLoader.allowResource('org/gradle/util/ClassLoaderTest.txt')

        expect:
        canSeeResource('org/gradle/util/ClassLoaderTest.txt')
    }

    void "visits self and parent"() {
        def visitor = Mock(ClassLoaderVisitor)
        given:
        classLoader.allowClass(Test)
        classLoader.allowPackage("org.junit")
        classLoader.allowResource("a/b/c")
        classLoader.disallowClass(Before.name)

        when:
        classLoader.visit(visitor)

        then:
        1 * visitor.visitSpec({ it instanceof FilteringClassLoader.Spec }) >> { FilteringClassLoader.Spec spec ->
            spec.classNames == [Test.name]
            spec.disallowedClassNames == [Before.name]
            spec.packageNames == ["org.junit"]
            spec.packagePrefixes == ["org.junit."]
            spec.resourceNames == ["a/b/c"]
            spec.resourcePrefixes == ["org/junit/"]
        }
        1 * visitor.visitParent(classLoader.parent)
        0 * visitor._
    }

    void cannotSeeResource(String name) {
        assert classLoader.getResource(name) == null
        assert classLoader.getResourceAsStream(name) == null
        assert !classLoader.getResources(name).hasMoreElements()
    }

    void canSeeResource(String name) {
        assert classLoader.getResource(name) != null
        def instr = classLoader.getResourceAsStream(name)
        assert instr != null
        instr.close()
        assert classLoader.getResources(name).hasMoreElements()
    }

    void canSeePackage(String name) {
        assert classLoader.getPackage(name) != null
        assert classLoader.packages.any { it.name == name }
    }

    void cannotSeePackage(String name) {
        assert classLoader.getPackage(name) == null
        assert !classLoader.packages.any { it.name == name }
    }

    void canLoadClass(Class<?> clazz) {
        assert classLoader.loadClass(clazz.name, false).is(clazz)
        assert classLoader.loadClass(clazz.name).is(clazz)
    }

    void cannotLoadClass(Class<?> clazz) {
        try {
            classLoader.loadClass(clazz.name, false)
            fail()
        } catch (ClassNotFoundException expected) {}
        try {
            classLoader.loadClass(clazz.name)
            fail()
        } catch (ClassNotFoundException expected) {}
    }

    def "does not attempt to load not allowed class"() {
        given:
        def parent = Mock(ClassLoader, useObjenesis: false)
        def loader = new FilteringClassLoader(parent)

        and:
        loader.allowPackage("good")

        when:
        loader.loadClass("good.Clazz")

        //noinspection GroovyAccessibility
        then:
        1 * parent.loadClass("good.Clazz", false) >> String
        0 * parent._

        when:
        loader.loadClass("bad.Clazz")

        then:
        thrown(ClassNotFoundException)

        and:
        0 * parent._
    }
}
