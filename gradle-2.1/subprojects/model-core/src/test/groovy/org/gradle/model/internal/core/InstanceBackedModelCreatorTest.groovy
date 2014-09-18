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

package org.gradle.model.internal.core

import org.gradle.model.internal.core.rule.describe.SimpleModelRuleDescriptor
import org.gradle.model.internal.registry.DefaultModelRegistry
import spock.lang.Specification

class InstanceBackedModelCreatorTest extends Specification {

    def registry = new DefaultModelRegistry()

    def "action is called"() {
        when:
        def foo = ModelReference.of("foo", List)
        def bar = ModelReference.of("bar", List)

        def descriptor = new SimpleModelRuleDescriptor("foo")

        def fooList = []
        def fooCreator = InstanceBackedModelCreator.of(foo, descriptor, fooList)
        registry.create(fooCreator)

        def barList = []
        def factory = Mock(org.gradle.internal.Factory) {
            1 * create() >> barList
        }
        def barCreator = InstanceBackedModelCreator.of(bar, descriptor, factory)
        registry.create(barCreator)

        then:
        !fooCreator.promise.asReadOnly(ModelType.of(String))
        !fooCreator.promise.asWritable(ModelType.of(String))
        fooCreator.promise.asReadOnly(ModelType.of(List))
        fooCreator.promise.asWritable(ModelType.of(List))

        registry.get(foo.path, foo.type).is(fooList)
        registry.get(bar.path, bar.type).is(barList)
    }

}
