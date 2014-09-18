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

package org.gradle.api.internal.plugins

import org.gradle.api.Action
import org.gradle.api.InvalidUserDataException
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.plugins.DeferredConfigurable
import spock.lang.Specification

class ExtensionsStorageTest extends Specification {
    def storage = new ExtensionsStorage()
    def listExtension = Mock(List)
    def setExtension = Mock(Set)

    def "setup"() {
        storage.add("list", listExtension)
        storage.add("set", setExtension)
    }

    def "has extension"() {
        expect:
        storage.hasExtension("list")
        !storage.hasExtension("not")
    }

    def "get extension"() {
        when:
        def list = storage.getByName("list")
        def set = storage.getByType(Set)

        then:
        list == listExtension
        set == setExtension

        when:
        storage.getByName("foo")

        then:
        thrown UnknownDomainObjectException

        when:
        storage.getByType(String)

        then:
        thrown UnknownDomainObjectException
    }

    def "find extension"() {
        when:
        def list = storage.findByName("list")
        def set = storage.findByType(Set)
        def foo = storage.findByName("foo")
        def string = storage.findByType(String)

        then:
        list == listExtension
        set == setExtension
        foo == null
        string == null
    }

    def "get as map"() {
        expect:
        storage.getAsMap() == ["list": listExtension, "set": setExtension]
    }

    def "configures regular extension"() {
        when:
        def extension = Mock(TestExtension)
        storage.add("ext", extension)

        and:
        storage.configureExtension("ext", {
            it.call(1)
        })
        storage.configureExtension(TestExtension, new Action<TestExtension>() {
            void execute(TestExtension t) {
                t.call(2)
            }
        })

        then:
        extension.call(1)
        extension.call(2)

        when:
        def val = storage.getByName("ext")

        then:
        val == extension
    }

    def "configures deferred configurable extension"() {

        TestDeferredExtension extension = new TestDeferredExtension()
        def delegate = Mock(TestExtension)
        extension.delegate = delegate

        when:
        storage.add("ext", extension)
        storage.configureExtension("ext", {
            it.call(1)
        })
        storage.configureExtension(TestDeferredExtension, new Action<TestDeferredExtension>() {
            void execute(TestDeferredExtension t) {
                t.call(2)
            }
        })

        then:
        0 * _

        when:
        storage.getByName("ext")

        then:
        1 * delegate.call(1)
        1 * delegate.call(2)
    }

    def "propagates configure exception on each attempt to access deferred configurable exception"() {

        TestDeferredExtension extension = new TestDeferredExtension()
        def delegate = Mock(TestExtension)
        extension.delegate = delegate

        given:
        storage.add("ext", extension)
        storage.configureExtension("ext", {
            throw new RuntimeException("bad")
        })

        when:
        storage.getByName("ext")

        then:
        def first = thrown RuntimeException
        first.message == "bad"

        when:
        storage.getByName("ext")

        then:
        def second = thrown RuntimeException
        second == first
    }

    def "rethrows unknown domain object exception thrown by deferred configurable extension config"() {

        TestDeferredExtension extension = new TestDeferredExtension()
        def delegate = Mock(TestExtension)
        extension.delegate = delegate

        when:
        storage.add("ext", extension)
        storage.configureExtension("ext", {
            throw new UnknownDomainObjectException("ORIGINAL")
        })

        then:
        0 * _

        when:
        storage.findByType(TestDeferredExtension)

        then:
        def t = thrown UnknownDomainObjectException
        t.message == "ORIGINAL"
    }

    def "cannot configure deferred configurable extension after access"() {

        TestDeferredExtension extension = new TestDeferredExtension()
        def delegate = Mock(TestExtension)
        extension.delegate = delegate

        given:
        storage.add("ext", extension)
        storage.configureExtension("ext", {
            it.call(1)
        })

        and:
        storage.getByName("ext")

        when:
        storage.configureExtension("ext", {
            it.call(2)
        })

        then:
        def t = thrown InvalidUserDataException
        t.message == "Cannot configure the 'ext' extension after it has been accessed."
    }

    public static interface TestExtension {
        void call(def value);
    }

    @DeferredConfigurable
    public static class TestDeferredExtension {
        TestExtension delegate

        void call(def value) {
            delegate.call(value)
        }
    }
}
