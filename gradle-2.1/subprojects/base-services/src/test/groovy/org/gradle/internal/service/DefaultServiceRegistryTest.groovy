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

package org.gradle.internal.service

import org.gradle.api.Action
import org.gradle.internal.Factory
import org.gradle.internal.concurrent.Stoppable
import org.gradle.util.TextUtil
import spock.lang.Specification

import java.lang.reflect.Type
import java.util.concurrent.Callable

class DefaultServiceRegistryTest extends Specification {
    def TestRegistry registry = new TestRegistry()

    def throwsExceptionForUnknownService() {
        when:
        registry.get(StringBuilder.class)

        then:
        UnknownServiceException e = thrown()
        e.message == "No service of type StringBuilder available in TestRegistry."
    }

    def delegatesToParentForUnknownService() {
        def value = BigDecimal.TEN
        def parent = Mock(ServiceRegistry)
        def registry = new TestRegistry(parent)

        when:
        def result = registry.get(BigDecimal)

        then:
        result == value

        and:
        1 * parent.get(BigDecimal) >> value
    }

    def delegatesToParentsForUnknownService() {
        def value = BigDecimal.TEN
        def parent1 = Mock(ServiceRegistry)
        def parent2 = Mock(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent1, parent2)

        when:
        def result = registry.get(BigDecimal)

        then:
        result == value

        and:
        1 * parent1.get(BigDecimal) >> { throw new UnknownServiceException(BigDecimal, "fail") }
        1 * parent2.get(BigDecimal) >> value
    }

    def throwsExceptionForUnknownParentService() {
        def parent = Mock(ServiceRegistry);
        def registry = new TestRegistry(parent)

        given:
        _ * parent.get(StringBuilder) >> { throw new UnknownServiceException(StringBuilder.class, "fail") }

        when:
        registry.get(StringBuilder)

        then:
        UnknownServiceException e = thrown()
        e.message == "No service of type StringBuilder available in TestRegistry."
    }

    def returnsServiceInstanceThatHasBeenRegistered() {
        def value = BigDecimal.TEN
        def registry = new DefaultServiceRegistry()

        given:
        registry.add(BigDecimal, value)

        expect:
        registry.get(BigDecimal) == value
        registry.get(Number) == value
        registry.get(Object) == value
    }

    def createsInstanceOfServiceImplementation() {
        def registry = new DefaultServiceRegistry()
        registry.register({ ServiceRegistration registration ->
            registration.add(TestServiceImpl)
        } as Action)

        expect:
        registry.get(TestService) instanceof TestServiceImpl
        registry.get(TestService) == registry.get(TestServiceImpl)
    }

    def injectsServicesIntoServiceImplementation() {
        def registry = new DefaultServiceRegistry()
        registry.register({ ServiceRegistration registration ->
            registration.add(ServiceWithDependency)
            registration.add(TestServiceImpl)
        } as Action)

        expect:
        registry.get(ServiceWithDependency).service == registry.get(TestServiceImpl)
    }

    def usesFactoryMethodOnProviderToCreateServiceInstance() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new TestProvider())

        expect:
        registry.get(Integer) == 12
        registry.get(Number) == 12
    }

    def injectsServicesIntoProviderFactoryMethod() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new Object() {
            Integer createInteger() {
                return 12
            }

            String createString(Integer integer) {
                return integer.toString()
            }
        })

        expect:
        registry.get(String) == "12"
    }

    def injectsGenericTypesIntoProviderFactoryMethod() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new Object() {
            Integer createInteger(Factory<String> factory) {
                return factory.create().length()
            }

            Factory<String> createString(Callable<String> action) {
                return { action.call() } as Factory
            }

            Callable<String> createAction() {
                return { "hi" }
            }
        })

        expect:
        registry.get(Integer) == 2
    }

    def handlesInheritanceInGenericTypes() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new ProviderWithGenericTypes())

        expect:
        registry.get(Integer) == 123
    }

    def canHaveMultipleServicesWithParameterizedTypesAndSameRawType() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new Object() {
            Integer createInteger(Callable<Integer> factory) {
                return factory.call()
            }

            String createString(Callable<String> factory) {
                return factory.call()
            }

            Callable<Integer> createIntFactory() {
                return { 123 }
            }

            Callable<String> createStringFactory() {
                return { "hi" }
            }
        })

        expect:
        registry.get(Integer) == 123
        registry.get(String) == "hi"
    }

    def injectsParentServicesIntoProviderFactoryMethod() {
        def parent = Mock(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent)
        registry.addProvider(new Object() {
            String createString(Number n) {
                return n.toString()
            }
        })

        when:
        def result = registry.get(String)

        then:
        result == '123'

        and:
        1 * parent.get(Number) >> 123
    }

    def injectsGenericTypesFromParentIntoProviderFactoryMethod() {
        def parent = new DefaultServiceRegistry() {
            Callable<String> createStringCallable() {
                return { "hello" }
            }
            Factory<String> createStringFactory() {
                return { "world" } as Factory
            }
        }
        def registry = new DefaultServiceRegistry(parent)
        registry.addProvider(new Object() {
            String createString(Callable<String> callable, Factory<String> factory) {
                return callable.call() + ' ' + factory.create()
            }
        })

        expect:
        registry.get(String) == 'hello world'
    }

    def injectsServiceRegistryIntoProviderFactoryMethod() {
        def parent = Mock(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent)
        registry.addProvider(new Object() {
            String createString(ServiceRegistry services) {
                assert services.is(registry)
                return services.get(Number).toString()
            }
        })
        registry.add(Integer, 123)

        expect:
        registry.get(String) == '123'
    }

    def failsWhenProviderFactoryMethodRequiresUnknownService() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new StringProvider())

        when:
        registry.get(String)

        then:
        ServiceCreationException e = thrown()
        e.message == "Cannot create service of type String using StringProvider.createString() as required service of type Runnable is not available."

        when:
        registry.get(Number)

        then:
        e = thrown()
        e.message == "Cannot create service of type String using StringProvider.createString() as required service of type Runnable is not available."
    }

    def failsWhenProviderFactoryMethodThrowsException() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new BrokenProvider())

        when:
        registry.get(String)

        then:
        ServiceCreationException e = thrown()
        e.message == "Could not create service of type String using BrokenProvider.createString()."
        e.cause == BrokenProvider.failure

        when:
        registry.get(Number)

        then:
        e = thrown()
        e.message == "Could not create service of type String using BrokenProvider.createString()."
        e.cause == BrokenProvider.failure
    }

    def cachesInstancesCreatedUsingAProviderFactoryMethod() {
        def registry = new DefaultServiceRegistry()
        def provider = new Object() {
            String createString(Number number) {
                return number.toString()
            }

            Integer createInteger() {
                return 12
            }
        }
        registry.addProvider(provider)

        expect:
        registry.get(Integer).is(registry.get(Integer))
        registry.get(Number).is(registry.get(Number))

        and:
        registry.get(String).is(registry.get(String))
    }

    def usesProviderDecoratorMethodToDecorateParentServiceInstance() {
        def parent = Mock(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent)
        registry.addProvider(new TestDecoratingProvider())

        given:
        _ * parent.get(Long) >> 110L

        expect:
        registry.get(Long) == 112L
        registry.get(Number) == 112L
        registry.get(Object) == 112L
    }

    def cachesServiceCreatedUsingProviderDecoratorMethod() {
        def parent = Mock(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent)
        registry.addProvider(new TestDecoratingProvider())

        given:
        _ * parent.get(Long) >> 11L

        expect:
        registry.get(Long).is(registry.get(Long))
    }

    def providerDecoratorMethodFailsWhenNoParentRegistry() {
        def registry = new DefaultServiceRegistry()

        when:
        registry.addProvider(new TestDecoratingProvider())

        then:
        ServiceLookupException e = thrown()
        e.message == "Cannot use decorator method TestDecoratingProvider.createLong() when no parent registry is provided."
    }

    def failsWhenProviderDecoratorMethodRequiresUnknownService() {
        def parent = Stub(ServiceRegistry) {
            get(_) >> { throw new UnknownServiceException(it[0], "broken") }
        }
        def registry = new DefaultServiceRegistry(parent)

        given:
        registry.addProvider(new TestDecoratingProvider())

        when:
        registry.get(Long)

        then:
        ServiceCreationException e = thrown()
        e.message == "Cannot create service of type Long using TestDecoratingProvider.createLong() as required service of type Long is not available in parent registries."
    }

    def failsWhenProviderDecoratorMethodThrowsException() {
        def parent = Stub(ServiceRegistry) {
            get(Long) >> 12L
        }
        def registry = new DefaultServiceRegistry(parent)

        given:
        registry.addProvider(new BrokenDecoratingProvider())

        when:
        registry.get(Long)

        then:
        ServiceCreationException e = thrown()
        e.message == "Could not create service of type Long using BrokenDecoratingProvider.createLong()."
        e.cause == BrokenDecoratingProvider.failure
    }

    def failsWhenThereIsACycleInDependenciesForProviderFactoryMethods() {
        def registry = new DefaultServiceRegistry()

        given:
        registry.addProvider(new ProviderWithCycle())

        when:
        registry.get(String)

        then:
        ServiceCreationException e = thrown()
        e.message == "Cannot create service of type Integer using ProviderWithCycle.createInteger() as there is a problem with parameter #1 of type String."
        e.cause.message == 'Cycle in dependencies of service of type String.'

        when:
        registry.getAll(Number)

        then:
        e = thrown()
        e.message == "Cannot create service of type Integer using ProviderWithCycle.createInteger() as there is a problem with parameter #1 of type String."
        e.cause.message == 'Cycle in dependencies of service of type String.'
    }

    def failsWhenAProviderFactoryMethodReturnsNull() {
        def registry = new DefaultServiceRegistry()

        given:
        registry.addProvider(new NullProvider())

        when:
        registry.get(String)

        then:
        ServiceCreationException e = thrown()
        e.message == "Could not create service of type String using NullProvider.createString() as this method returned null."
    }

    def failsWhenAProviderDecoratorMethodReturnsNull() {
        def parent = Stub(ServiceRegistry) {
            get(String) >> "parent"
        }
        def registry = new DefaultServiceRegistry(parent)

        given:
        registry.addProvider(new NullDecorator())

        when:
        registry.get(String)

        then:
        ServiceCreationException e = thrown()
        e.message == "Could not create service of type String using NullDecorator.createString() as this method returned null."
    }

    def usesFactoryMethodToCreateServiceInstance() {
        expect:
        registry.get(String.class) == "12"
        registry.get(Integer.class) == 12
    }

    def cachesInstancesCreatedUsingAFactoryMethod() {
        expect:
        registry.get(Integer).is(registry.get(Integer))
        registry.get(Number).is(registry.get(Number))
    }

    def usesOverriddenFactoryMethodToCreateServiceInstance() {
        def registry = new TestRegistry() {
            @Override
            protected String createString() {
                return "overridden"
            }
        };

        expect:
        registry.get(String) == "overridden"
    }

    def failsWhenMultipleFactoryMethodsCanCreateRequestedServiceType() {
        def registry = new DefaultServiceRegistry();
        registry.addProvider(new TestProvider())

        expect:
        registry.get(String)

        when:
        registry.get(Object)

        then:
        ServiceLookupException e = thrown()
        e.message == TextUtil.toPlatformLineSeparators("""Multiple services of type Object available in DefaultServiceRegistry:
   - Service Callable<BigDecimal> at TestProvider.createCallable()
   - Service Factory<BigDecimal> at TestProvider.createTestFactory()
   - Service Integer at TestProvider.createInt()
   - Service String at TestProvider.createString()""")
    }

    def failsWhenArrayClassRequested() {
        when:
        registry.get(String[].class)

        then:
        ServiceLookupException e = thrown()
        e.message == "Locating services with array type is not supported."
    }

    def cannotInjectAnArrayType() {
        given:
        registry.addProvider(new UnsupportedInjectionProvider())

        when:
        registry.get(Number)

        then:
        ServiceCreationException e = thrown()
        e.message == "Cannot create service of type Number using UnsupportedInjectionProvider.create() as there is a problem with parameter #1 of type String[]."
        e.cause.message == 'Locating services with array type is not supported.'
    }

    def usesDecoratorMethodToDecorateParentServiceInstance() {
        def parent = Mock(ServiceRegistry)
        def registry = new RegistryWithDecoratorMethods(parent)

        when:
        def result = registry.get(Long)

        then:
        result == 120L

        and:
        1 * parent.get(Long) >> 110L
    }

    def decoratorMethodFailsWhenNoParentRegistry() {
        when:
        new RegistryWithDecoratorMethods()

        then:
        ServiceLookupException e = thrown()
        e.message.matches(/Cannot use decorator method RegistryWithDecoratorMethods\..*() when no parent registry is provided./)
    }

    def canRegisterServicesUsingAction() {
        def registry = new DefaultServiceRegistry()

        given:
        registry.register({ ServiceRegistration registration ->
            registration.add(Number, 12)
            registration.add(TestServiceImpl)
            registration.addProvider(new Object() {
                String createString() {
                    return "hi"
                }
            })
        } as Action)

        expect:
        registry.get(Number) == 12
        registry.get(TestServiceImpl)
        registry.get(String) == "hi"
    }

    def providerConfigureMethodCanRegisterServices() {
        def registry = new DefaultServiceRegistry()

        given:
        registry.addProvider(new Object() {
            void configure(ServiceRegistration registration, Number value) {
                registration.addProvider(new Object() {
                    String createString() {
                        return value.toString()
                    }
                })
            }

            Integer createNumber() {
                return 123
            }
        })

        expect:
        registry.get(Number) == 123
        registry.get(String) == "123"
    }

    def failsWhenProviderConfigureMethodRequiresUnknownService() {
        def registry = new DefaultServiceRegistry()

        when:
        registry.addProvider(new NoOpConfigureProvider())

        then:
        ServiceLookupException e = thrown()
        e.message == 'Cannot configure services using NoOpConfigureProvider.configure() as required service of type String is not available.'
    }

    def failsWhenProviderConfigureMethodFails() {
        def registry = new DefaultServiceRegistry()

        when:
        registry.addProvider(new BrokenConfigureProvider())

        then:
        ServiceLookupException e = thrown()
        e.message == 'Could not configure services using BrokenConfigureProvider.configure().'
        e.cause == BrokenConfigureProvider.failure
    }

    def failsWhenCannotCreateServiceInstanceFromImplementationClass() {
        given:
        registry.register({ registration -> registration.add(ClassWithBrokenConstructor)} as Action)

        when:
        registry.get(ClassWithBrokenConstructor)

        then:
        ServiceCreationException e = thrown()
        e.message == 'Could not create service of type ClassWithBrokenConstructor.'
        e.cause == ClassWithBrokenConstructor.failure
    }

    def canGetAllServicesOfAGivenType() {
        registry.addProvider(new Object(){
            String createOtherString() {
                return "hi"
            }
        })

        expect:
        registry.getAll(String) == ["12", "hi"]
        registry.getAll(Number) == [12]
    }

    def canGetAllServicesOfARawType() {
        def registry = new DefaultServiceRegistry()
        registry.addProvider(new Object(){
            String createString() {
                return "hi"
            }
            Factory<String> createFactory() {
                return {} as Factory
            }
            Callable<String> createCallable() {
                return {}
            }
        })

        expect:
        registry.getAll(Factory).size() == 1
        registry.getAll(Object).size() == 3
    }

    def allServicesReturnsEmptyCollectionWhenNoServicesOfGivenType() {
        expect:
        registry.getAll(Long).empty
    }

    def allServicesIncludesServicesFromParents() {
        def parent1 = Stub(ServiceRegistry)
        def parent2 = Stub(ServiceRegistry)
        def registry = new DefaultServiceRegistry(parent1, parent2)
        registry.addProvider(new Object() {
            Long createLong() {
                return 12;
            }
        });

        given:
        _ * parent1.getAll(Number) >> [123L]
        _ * parent2.getAll(Number) >> [456]

        expect:
        registry.getAll(Number) == [12, 123L, 456]
    }

    def canGetServiceAsFactoryWhenTheServiceImplementsFactoryInterface() {
        expect:
        registry.getFactory(BigDecimal) instanceof TestFactory
        registry.getFactory(Number) instanceof TestFactory
        registry.getFactory(BigDecimal).is(registry.getFactory(BigDecimal))
        registry.getFactory(Number).is(registry.getFactory(BigDecimal))
    }

    def canLocateFactoryWhenServiceInterfaceExtendsFactory() {
        def registry = new DefaultServiceRegistry()

        given:
        registry.add(StringFactory, new StringFactory() {
            public String create() {
                return "value"
            }
        })

        expect:
        registry.getFactory(String.class).create() == "value"
    }

    def canGetAFactoryUsingParameterizedFactoryType() {
        def registry = new RegistryWithMultipleFactoryMethods()

        expect:
        def stringFactory = registry.get(stringFactoryType)
        stringFactory.create() == "hello"

        def numberFactory = registry.get(numberFactoryType)
        numberFactory.create() == 12
    }

    def canGetAFactoryUsingFactoryTypeWithBounds() throws NoSuchFieldException {
        expect:
        def superBigDecimalFactory = registry.get(superBigDecimalFactoryType)
        superBigDecimalFactory.create() == BigDecimal.valueOf(0)

        def extendsBigDecimalFactory = registry.get(extendsBigDecimalFactoryType)
        extendsBigDecimalFactory.create() == BigDecimal.valueOf(1)

        def extendsNumberFactory = registry.get(extendsNumberFactoryType)
        extendsNumberFactory.create() == BigDecimal.valueOf(2)
    }

    def usesAFactoryServiceToCreateInstances() {
        expect:
        registry.newInstance(BigDecimal) == BigDecimal.valueOf(0)
        registry.newInstance(BigDecimal) == BigDecimal.valueOf(1)
        registry.newInstance(BigDecimal) == BigDecimal.valueOf(2)
    }

    def throwsExceptionForUnknownFactory() {
        when:
        registry.getFactory(String)

        then:
        UnknownServiceException e = thrown()
        e.message == "No factory for objects of type String available in TestRegistry."
    }

    def delegatesToParentForUnknownFactory() {
        def factory = Mock(Factory)
        def parent = Mock(ServiceRegistry)
        def registry = new TestRegistry(parent)

        when:
        def result = registry.getFactory(Map)

        then:
        result == factory

        and:
        1 * parent.getFactory(Map) >> factory
    }

    def usesDecoratorMethodToDecorateParentFactoryInstance() {
        def factory = Mock(Factory)
        def parent = Mock(ServiceRegistry)
        def registry = new RegistryWithDecoratorMethods(parent)

        given:
        _ * parent.getFactory(Long) >> factory
        _ * factory.create() >>> [10L, 20L]

        expect:
        registry.newInstance(Long) == 12L
        registry.newInstance(Long) == 22L
    }

    def failsWhenMultipleFactoriesAreAvailableForServiceType() {
        def registry = new RegistryWithAmbiguousFactoryMethods()

        when:
        registry.getFactory(Object)

        then:
        ServiceLookupException e = thrown()
        e.message == TextUtil.toPlatformLineSeparators("""Multiple factories for objects of type Object available in RegistryWithAmbiguousFactoryMethods:
   - Service Factory<Object> at RegistryWithAmbiguousFactoryMethods.createObjectFactory()
   - Service Factory<String> at RegistryWithAmbiguousFactoryMethods.createStringFactory()""")
    }

    def servicesCreatedByFactoryMethodsAreVisibleWhenUsingASubClass() {
        def registry = new TestRegistry() {
        }

        expect:
        registry.get(String) == "12"
        registry.get(Integer) == 12
    }

    def closeInvokesCloseMethodOnEachService() {
        def service = Mock(TestCloseService)

        given:
        registry.add(TestCloseService, service)

        when:
        registry.close()

        then:
        1 * service.close()
    }

    def closeInvokesStopMethodOnEachService() {
        def service = Mock(TestStopService)

        given:
        registry.add(TestStopService, service)

        when:
        registry.close()

        then:
        1 * service.stop()
    }

    def closeIgnoresServiceWithNoCloseOrStopMethod() {
        registry.add(String, "service")
        registry.getAll(Object)

        when:
        registry.close()

        then:
        noExceptionThrown()
    }

    def closeInvokesCloseMethodOnEachServiceCreatedFromImplementationClass() {
        given:
        registry.register({ registration -> registration.add(ClosableService)} as Action)
        def service = registry.get(ClosableService)

        when:
        registry.close()

        then:
        service.closed
    }

    def closeInvokesCloseMethodOnEachServiceCreatedByProviderFactoryMethod() {
        def service = Mock(TestStopService)

        given:
        registry.addProvider(new Object() {
            TestStopService createServices() {
                return service
            }
        })
        registry.get(TestStopService)

        when:
        registry.close()

        then:
        1 * service.stop()
    }

    def closeClosesServicesInDependencyOrder() {
        def service1 = Mock(TestCloseService)
        def service2 = Mock(TestStopService)
        def service3 = Mock(ClosableService)
        def registry = new DefaultServiceRegistry()

        given:
        registry.addProvider(new Object() {
            TestStopService createService2(ClosableService b) {
                return service2
            }
            ClosableService createService3() {
                return service3
            }
        })
        registry.addProvider(new Object() {
            TestCloseService createService1(TestStopService a, ClosableService b) {
                return service1
            }
        })
        registry.get(TestCloseService)

        when:
        registry.close()

        then:
        1 * service1.close()

        then:
        1 * service2.stop()

        then:
        1 * service3.close()
        0 * _._
    }

    def closeContinuesToCloseServicesAfterFailingToStopSomeService() {
        def service1 = Mock(TestCloseService)
        def service2 = Mock(TestStopService)
        def service3 = Mock(ClosableService)
        def failure = new RuntimeException()
        def registry = new DefaultServiceRegistry()

        given:
        registry.addProvider(new Object() {
            TestStopService createService2(ClosableService b) {
                return service2
            }
            TestCloseService createService1(TestStopService a) {
                return service1
            }
            ClosableService createService3() {
                return service3
            }
        })
        registry.get(TestCloseService)

        when:
        registry.close()

        then:
        RuntimeException e = thrown()
        e == failure

        and:
        1 * service1.close()
        1 * service2.stop() >> { throw failure }
        1 * service3.close()
        0 * _._
    }

    def doesNotStopServiceThatHasNotBeenCreated() {
        def service = Mock(TestStopService)

        given:
        registry.addProvider(new Object() {
            TestStopService createServices() {
                return service
            }
        })

        when:
        registry.close()

        then:
        0 * service.stop()
    }

    def canStopMultipleTimes() {
        def service = Mock(TestCloseService)

        given:
        registry.add(TestCloseService, service)

        when:
        registry.close()

        then:
        1 * service.close()

        when:
        registry.close()

        then:
        0 * service._
    }

    def cannotLookupServicesWhenClosed() {
        given:
        registry.get(String)
        registry.getAll(String)
        registry.close()

        when:
        registry.get(String)

        then:
        IllegalStateException e = thrown()
        e.message == "Cannot locate service of type String, as TestRegistry has been closed."

        when:
        registry.getAll(String)

        then:
        e = thrown()
        e.message == "Cannot locate service of type String, as TestRegistry has been closed."
    }

    def cannotLookupFactoriesWhenClosed() {
        given:
        registry.getFactory(BigDecimal)
        registry.close()

        when:
        registry.getFactory(BigDecimal)

        then:
        IllegalStateException e = thrown()
        e.message == "Cannot locate factory for objects of type BigDecimal, as TestRegistry has been closed."
    }

    private Factory<Number> numberFactory
    private Factory<String> stringFactory
    private Factory<? super BigDecimal> superBigDecimalFactory
    private Factory<? extends BigDecimal> extendsBigDecimalFactory
    private Factory<? extends Number> extendsNumberFactory

    private Type getNumberFactoryType() {
        return getClass().getDeclaredField("numberFactory").getGenericType();
    }

    private Type getStringFactoryType() {
        return getClass().getDeclaredField("stringFactory").getGenericType();
    }

    private Type getSuperBigDecimalFactoryType() {
        return getClass().getDeclaredField("superBigDecimalFactory").getGenericType()
    }

    private Type getExtendsBigDecimalFactoryType() {
        return getClass().getDeclaredField("extendsBigDecimalFactory").getGenericType()
    }

    private Type getExtendsNumberFactoryType() {
        return getClass().getDeclaredField("extendsNumberFactory").getGenericType()
    }

    private static class TestFactory implements Factory<BigDecimal> {
        int value;
        public BigDecimal create() {
            return BigDecimal.valueOf(value++)
        }
    }

    private interface TestService {
    }

    private static class TestServiceImpl implements TestService {
    }

    private static class ServiceWithDependency {
        final TestService service

        ServiceWithDependency(TestService service) {
            this.service = service
        }
    }

    private interface StringFactory extends Factory<String> {
    }

    private static class TestRegistry extends DefaultServiceRegistry {
        public TestRegistry() {
        }

        public TestRegistry(ServiceRegistry parent) {
            super(parent)
        }

        protected String createString() {
            return get(Integer).toString()
        }

        protected Integer createInt() {
            return 12
        }

        protected Factory<BigDecimal> createTestFactory() {
            return new TestFactory()
        }
    }

    private static class TestProvider {
        String createString(Integer integer) {
            return integer.toString()
        }

        Integer createInt() {
            return 12
        }

        Factory<BigDecimal> createTestFactory() {
            return new TestFactory()
        }

        Callable<BigDecimal> createCallable() {
            return { 12 }
        }
    }

    private static class StringProvider {
        String createString(Runnable r) {
            return "hi"
        }

        Integer createInteger(String value) {
            return value.length()
        }
    }

    private static class ProviderWithCycle {
        String createString(Integer value) {
            return value.toString()
        }

        Integer createInteger(String value) {
            return value.length()
        }
    }

    private static class NullProvider {
        String createString() {
            return null
        }
    }

    private static class UnsupportedInjectionProvider {
        Number create(String[] values) {
            return values.length
        }
    }

    private static class NoOpConfigureProvider {
        void configure(ServiceRegistration registration, String value) {
        }
    }

    private static class BrokenConfigureProvider {
        static def failure = new RuntimeException()

        void configure(ServiceRegistration registration) {
            throw failure
        }
    }

    private static class BrokenProvider {
        static def failure = new RuntimeException()

        String createString() {
            throw failure.fillInStackTrace()
        }

        Integer createInteger(String value) {
            return value.length()
        }
    }

    private static class TestDecoratingProvider {
        Long createLong(Long value) {
            return value + 2
        }
    }

    private static class BrokenDecoratingProvider {
        static def failure = new RuntimeException()

        Long createLong(Long value) {
            throw failure
        }
    }

    private static class NullDecorator {
        String createString(String value) {
            return null
        }
    }

    private static class RegistryWithAmbiguousFactoryMethods extends DefaultServiceRegistry {
        Object createObject() {
            return "hello"
        }

        String createString() {
            return "hello"
        }

        Factory<Object> createObjectFactory() {
            return new Factory<Object>() {
                public Object create() {
                    return createObject()
                }
            };
        }

        Factory<String> createStringFactory() {
            return new Factory<String>() {
                public String create() {
                    return createString()
                }
            };
        }
    }

    private static class RegistryWithDecoratorMethods extends DefaultServiceRegistry {
        public RegistryWithDecoratorMethods() {
        }

        public RegistryWithDecoratorMethods(ServiceRegistry parent) {
            super(parent)
        }

        protected Long createLong(Long value) {
            return value + 10
        }

        protected Factory<Long> createLongFactory(final Factory<Long> factory) {
            return new Factory<Long>() {
                public Long create() {
                    return factory.create() + 2
                }
            };
        }
    }

    private static class RegistryWithMultipleFactoryMethods extends DefaultServiceRegistry {
        Factory<Number> createObjectFactory() {
            return new Factory<Number>() {
                public Number create() {
                    return 12
                }
            };
        }

        Factory<String> createStringFactory() {
            return new Factory<String>() {
                public String create() {
                    return "hello"
                }
            };
        }
    }

    public interface TestCloseService extends Closeable {
        void close()
    }

    public interface TestStopService extends Stoppable {
        void stop()
    }

    static class ClassWithBrokenConstructor {
        static def failure = new RuntimeException("broken")

        ClassWithBrokenConstructor() {
            throw failure
        }
    }

    static class ClosableService implements Closeable {
        boolean closed

        void close() {
            closed = true
        }
    }
}
