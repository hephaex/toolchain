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
package org.gradle.listener

import spock.lang.Specification
import org.gradle.internal.Factory
import java.util.concurrent.Callable

class LazyCreationProxyTest extends Specification {
    final Factory<Callable<String>> factory = Mock()
    final Callable<String> callable = Mock()

    def "instantiates and caches object on first method invocation on source"() {
        when:
        def proxy = new LazyCreationProxy<Callable<String>>(Callable, factory)
        def source = proxy.source

        then:
        0 * factory._

        when:
        def result = source.call()

        then:
        result == 'a'
        1 * factory.create() >> callable
        1 * callable.call() >> 'a'
        0 * factory._
        0 * callable._

        when:
        result = source.call()

        then:
        result == 'b'
        1 * callable.call() >> 'b'
        0 * factory._
        0 * callable._
    }

    def "rethrows exception thrown by factory on creation"() {
        def failure = new RuntimeException()
        
        given:
        def proxy = new LazyCreationProxy<Callable<String>>(Callable, factory)
        def source = proxy.source

        when:
        source.call()

        then:
        Exception e = thrown(Exception)
        e == failure

        and:
        _ * factory.create() >> { throw failure }
    }

    def "rethrows checked exception thrown by method call on target object"() {
        def failure = new IOException()

        given:
        def proxy = new LazyCreationProxy<Callable<String>>(Callable, factory)
        def source = proxy.source
        _ * factory.create() >> callable

        when:
        source.call()

        then:
        Exception e = thrown(Exception)
        e == failure

        and:
        _ * callable.call() >> { throw failure }
    }
}
