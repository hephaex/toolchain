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

package org.gradle.tooling.internal.adapter

import org.gradle.api.Action
import org.gradle.messaging.remote.internal.Message
import org.gradle.tooling.model.DomainObjectSet
import org.gradle.tooling.model.UnsupportedMethodException
import org.gradle.util.Matchers
import spock.lang.Specification

import java.lang.reflect.InvocationHandler
import java.nio.channels.ByteChannel
import java.nio.channels.Channel

class ProtocolToModelAdapterTest extends Specification {
    final ProtocolToModelAdapter adapter = new ProtocolToModelAdapter()

    def mapsNullToNull() {
        expect:
        adapter.adapt(TestModel.class, null) == null
    }

    def createsProxyAdapterForProtocolModel() {
        TestProtocolModel protocolModel = Mock()

        expect:
        adapter.adapt(TestModel.class, protocolModel) instanceof TestModel
    }

    def proxiesAreEqualWhenTargetProtocolObjectsAreEqual() {
        TestProtocolModel protocolModel1 = Mock()
        TestProtocolModel protocolModel2 = Mock()

        def model = adapter.adapt(TestModel.class, protocolModel1)
        def equal = adapter.adapt(TestModel.class, protocolModel1)
        def different = adapter.adapt(TestModel.class, protocolModel2)

        expect:
        Matchers.strictlyEquals(model, equal)
        model != different
    }

    def methodInvocationOnModelDelegatesToTheProtocolModelObject() {
        TestProtocolModel protocolModel = Mock()
        _ * protocolModel.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.name == 'name'
    }

    def createsProxyAdapterForMethodReturnValue() {
        TestProtocolModel protocolModel = Mock()
        TestProtocolProject protocolProject = Mock()
        _ * protocolModel.getProject() >> protocolProject
        _ * protocolProject.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.project instanceof TestProject
        model.project.name == 'name'
    }

    def doesNotAdaptNullReturnValue() {
        TestProtocolModel protocolModel = Mock()
        _ * protocolModel.getProject() >> null

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.project == null
    }

    def adaptsIterableToDomainObjectSet() {
        TestProtocolModel protocolModel = Mock()
        TestProtocolProject protocolProject = Mock()
        _ * protocolModel.getChildren() >> [protocolProject]
        _ * protocolProject.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.children.size() == 1
        model.children[0] instanceof TestProject
        model.children[0].name == 'name'
    }

    def adaptsIterableToCollectionType() {
        TestProtocolModel protocolModel = Mock()
        TestProtocolProject protocolProject = Mock()
        _ * protocolModel.getChildList() >> [protocolProject]
        _ * protocolProject.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.childList.size() == 1
        model.childList[0] instanceof TestProject
        model.childList[0].name == 'name'
    }

    def adaptsMapElements() {
        TestProtocolModel protocolModel = Mock()
        TestProtocolProject protocolProject = Mock()
        _ * protocolModel.project >> protocolProject
        _ * protocolModel.getChildMap() >> Collections.singletonMap(protocolProject, protocolProject)
        _ * protocolProject.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.childMap.size() == 1
        model.childMap[model.project] == model.project
    }

    def cachesPropertyValues() {
        TestProtocolModel protocolModel = Mock()
        TestProtocolProject protocolProject = Mock()
        _ * protocolModel.getProject() >> protocolProject
        _ * protocolModel.getChildren() >> [protocolProject]
        _ * protocolProject.getName() >> 'name'

        expect:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.project.is(model.project)
        model.children.is(model.children)
    }

    def reportsMethodWhichDoesNotExistOnProtocolObject() {
        PartialTestProtocolModel protocolModel = Mock()

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.project

        then:
        UnsupportedMethodException e = thrown()
        e.message.contains "TestModel.getProject()"
    }

    def propagatesExceptionThrownByProtocolObject() {
        TestProtocolModel protocolModel = Mock()
        RuntimeException failure = new RuntimeException()

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)
        model.name

        then:
        protocolModel.name >> { throw failure }
        RuntimeException e = thrown()
        e == failure
    }

    def isPropertySupportedMethodReturnsTrueWhenProtocolObjectHasAssociatedProperty() {
        TestProtocolModel protocolModel = Mock()

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)

        then:
        model.configSupported
    }

    def isPropertySupportedMethodReturnsFalseWhenProtocolObjectDoesNotHaveAssociatedProperty() {
        PartialTestProtocolModel protocolModel = Mock()

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)

        then:
        !model.configSupported
    }

    def safeGetterDelegatesToProtocolObject() {
        TestProtocolModel protocolModel = Mock()

        given:
        protocolModel.config >> "value"

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)

        then:
        model.getConfig("default") == "value"
    }

    def safeGetterDelegatesReturnsDefaultValueWhenProtocolObjectDoesNotHaveAssociatedProperty() {
        PartialTestProtocolModel protocolModel = Mock()

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)

        then:
        model.getConfig("default") == "default"
    }

    def safeGetterDelegatesReturnsDefaultValueWhenPropertyValueIsNull() {
        TestProtocolModel protocolModel = Mock()

        given:
        protocolModel.config >> null

        when:
        def model = adapter.adapt(TestModel.class, protocolModel)

        then:
        model.getConfig("default") == "default"
    }

    def "mapper can register method invoker to override getter method"() {
        MethodInvoker methodInvoker = Mock()
        Action mapper = Mock()
        TestProtocolModel protocolModel = Mock()
        TestProject project = Mock()

        given:
        mapper.execute(_) >> { SourceObjectMapping mapping ->
            mapping.mixIn(methodInvoker)
        }
        methodInvoker.invoke({ it.name == 'getProject' }) >> { MethodInvocation method -> method.result = project }

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, mapper)

        then:
        model.project == project

        and:
        0 * protocolModel._
    }

    def "mapper can register method invoker to provide getter method implementation"() {
        MethodInvoker methodInvoker = Mock()
        Action mapper = Mock()
        PartialTestProtocolModel protocolModel = Mock()
        TestProject project = Mock()

        given:
        mapper.execute(_) >> { SourceObjectMapping mapping ->
            mapping.mixIn(methodInvoker)
        }
        methodInvoker.invoke({ it.name == 'getProject' }) >> { MethodInvocation method -> method.result = project }

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, mapper)

        then:
        model.project == project

        and:
        0 * protocolModel._
    }

    def "adapts values returned by method invoker"() {
        MethodInvoker methodInvoker = Mock()
        Action mapper = Mock()

        given:
        mapper.execute(_) >> { SourceObjectMapping mapping ->
            mapping.mixIn(methodInvoker)
        }
        methodInvoker.invoke({ it.name == 'getProject' }) >> { MethodInvocation method -> method.result = new Object() }

        when:
        def model = adapter.adapt(TestModel.class, new Object(), mapper)

        then:
        model.project
    }

    def "method invoker properties are cached"() {
        MethodInvoker methodInvoker = Mock()
        Action mapper = Mock()
        PartialTestProtocolModel protocolModel = Mock()
        TestProject project = Mock()

        given:
        mapper.execute(_) >> { SourceObjectMapping mapping ->
            mapping.mixIn(methodInvoker)
        }

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, mapper)
        model.project
        model.project

        then:
        1 * methodInvoker.invoke(!null) >> { MethodInvocation method -> method.result = project }
        0 * methodInvoker._
        0 * protocolModel._
    }

    def canMixInMethodsFromAnotherBean() {
        PartialTestProtocolModel protocolModel = Mock()

        given:
        protocolModel.name >> 'name'

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, ConfigMixin)

        then:
        model.name == "[name]"
        model.getConfig('default') == "[default]"
    }

    def "adapts values returned from mix in beans"() {
        PartialTestProtocolModel protocolModel = Mock()

        given:
        protocolModel.name >> 'name'

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, ConfigMixin)

        then:
        model.project != null
    }

    def "mapper can mix in methods from another bean"() {
        def mapper = Mock(Action)
        def protocolModel = Mock(PartialTestProtocolModel)

        given:
        protocolModel.name >> 'name'

        when:
        def model = adapter.adapt(TestModel.class, protocolModel, mapper)

        then:
        1 * mapper.execute({it.sourceObject == protocolModel}) >> { SourceObjectMapping mapping ->
            mapping.mixIn(ConfigMixin)
        }

        and:
        model.name == "[name]"
        model.getConfig('default') == "[default]"
    }

    def "delegates to type provider to determine type to wrap an object in"() {
        def typeProvider = Mock(TargetTypeProvider)
        def adapter = new ProtocolToModelAdapter(typeProvider)
        def sourceObject = new Object()

        given:
        _ * typeProvider.getTargetType(Channel, sourceObject) >> ByteChannel

        when:
        def result = adapter.adapt(Channel.class, sourceObject)

        then:
        result instanceof ByteChannel
    }

    def "mapper can specify the type to wrap an object in"() {
        def mapper = Mock(Action)
        def sourceObject = Mock(TestProtocolModel)
        def sourceProject = Mock(TestProtocolProject)
        def adapter = new ProtocolToModelAdapter()

        given:
        sourceObject.project >> sourceProject

        when:
        def result = adapter.adapt(TestModel.class, sourceObject, mapper)

        then:
        1 * mapper.execute({it.sourceObject == sourceObject})

        when:
        def project = result.project

        then:
        project instanceof TestExtendedProject

        and:
        1 * mapper.execute({it.sourceObject == sourceProject}) >> { SourceObjectMapping mapping ->
            mapping.mapToType(TestExtendedProject)
        }
    }

    def "view objects can be serialized"() {
        def protocolModel = new TestProtocolProjectImpl()

        given:
        def model = adapter.adapt(TestProject.class, protocolModel)

        expect:
        def serialized = new ByteArrayOutputStream()
        Message.send(model, serialized)
        def copiedModel = Message.receive(new ByteArrayInputStream(serialized.toByteArray()), getClass().classLoader)
        copiedModel instanceof TestProject
        copiedModel != model
        copiedModel.name == "name"
    }

    def "unpacks source object from view"() {
        def source = new Object()

        given:
        def view = adapter.adapt(TestProject.class, source)

        expect:
        adapter.unpack(view).is(source)
    }

    def "fails when source object is not a view object"() {
        when:
        adapter.unpack("not a view")

        then:
        thrown(IllegalArgumentException)

        when:
        adapter.unpack(java.lang.reflect.Proxy.newProxyInstance(getClass().classLoader, [Runnable] as Class[], Stub(InvocationHandler)))

        then:
        thrown(IllegalArgumentException)
    }
}

interface TestModel {
    String getName()

    TestProject getProject()

    boolean isConfigSupported()

    String getConfig(String defaultValue)

    DomainObjectSet<? extends TestProject> getChildren()

    List<TestProject> getChildList()

    Map<TestProject, TestProject> getChildMap()
}

interface TestProject {
    String getName()
}

interface TestExtendedProject extends TestProject {
}

interface TestProtocolModel {
    String getName()

    TestProtocolProject getProject()

    Iterable<? extends TestProtocolProject> getChildren()

    Iterable<? extends TestProtocolProject> getChildList()

    Map<String, ? extends TestProtocolProject> getChildMap()

    String getConfig();
}

interface PartialTestProtocolModel {
    String getName()
}

interface TestProtocolProject {
    String getName()
}

class TestProtocolProjectImpl implements Serializable {
    String name = "name"
}

class ConfigMixin {
    TestModel model

    ConfigMixin(TestModel model) {
        this.model = model
    }

    Object getProject() {
        return new Object()
    }

    String getConfig(String value) {
        return "[${model.getConfig(value)}]"
    }

    String getName() {
        return "[${model.name}]"
    }
}
