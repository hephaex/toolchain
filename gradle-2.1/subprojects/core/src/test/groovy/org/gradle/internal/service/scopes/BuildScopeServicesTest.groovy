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

package org.gradle.internal.service.scopes

import org.gradle.StartParameter
import org.gradle.api.internal.*
import org.gradle.api.internal.artifacts.DependencyManagementServices
import org.gradle.api.internal.classpath.DefaultModuleRegistry
import org.gradle.api.internal.classpath.ModuleRegistry
import org.gradle.api.internal.classpath.PluginModuleRegistry
import org.gradle.api.internal.file.FileLookup
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.project.*
import org.gradle.cache.CacheRepository
import org.gradle.cache.internal.CacheFactory
import org.gradle.cache.internal.DefaultCacheRepository
import org.gradle.configuration.BuildConfigurer
import org.gradle.configuration.DefaultBuildConfigurer
import org.gradle.configuration.DefaultScriptPluginFactory
import org.gradle.configuration.ScriptPluginFactory
import org.gradle.groovy.scripts.DefaultScriptCompilerFactory
import org.gradle.groovy.scripts.ScriptCompilerFactory
import org.gradle.initialization.*
import org.gradle.internal.Factory
import org.gradle.internal.classloader.ClassLoaderFactory
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.listener.DefaultListenerManager
import org.gradle.listener.ListenerManager
import org.gradle.logging.LoggingManagerInternal
import org.gradle.logging.ProgressLoggerFactory
import org.gradle.messaging.remote.MessagingServer
import org.gradle.plugin.use.internal.PluginRequestApplicator
import org.gradle.process.internal.DefaultWorkerProcessFactory
import org.gradle.process.internal.WorkerProcessBuilder
import org.gradle.profile.ProfileEventAdapter
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification

import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.sameInstance
import static org.junit.Assert.assertThat

public class BuildScopeServicesTest extends Specification {
    @Rule
    TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()
    StartParameter startParameter = new StartParameter()
    ServiceRegistry parent = Stub()
    Factory<CacheFactory> cacheFactoryFactory = Mock()
    ClosableCacheFactory cacheFactory = Mock()
    ClassLoaderRegistry classLoaderRegistry = Mock()

    BuildScopeServices registry = new BuildScopeServices(parent, startParameter)

    def setup() {
        startParameter.gradleUserHomeDir = tmpDir.testDirectory
        parent.getFactory(CacheFactory) >> cacheFactoryFactory
        cacheFactoryFactory.create() >> cacheFactory
        parent.get(ClassLoaderRegistry) >> classLoaderRegistry
        parent.getFactory(LoggingManagerInternal) >> Stub(Factory)
        parent.get(ModuleRegistry) >> new DefaultModuleRegistry()
        parent.get(PluginModuleRegistry) >> Stub(PluginModuleRegistry)
        parent.get(DependencyManagementServices) >> Stub(DependencyManagementServices)
        parent.get(Instantiator) >> ThreadGlobalInstantiator.getOrCreate()
        parent.get(FileResolver) >> Stub(FileResolver)
        parent.get(ProgressLoggerFactory) >> Stub(ProgressLoggerFactory)
        parent.get(CacheFactory) >> Stub(CacheFactory)
        parent.get(DocumentationRegistry) >> new DocumentationRegistry()
        parent.get(FileLookup) >> Stub(FileLookup)
        parent.get(PluginRequestApplicator) >> Mock(PluginRequestApplicator)
    }

    def delegatesToParentForUnknownService() {
        setup:
        parent.get(String) >> "value"

        expect:
        registry.get(String) == "value"
    }

    def addsAllPluginBuildScopeServices() {
        def plugin2 = Mock(PluginServiceRegistry)
        def plugin1 = Mock(PluginServiceRegistry)

        given:
        parent.getAll(PluginServiceRegistry) >> [plugin1, plugin2]

        when:
        new BuildScopeServices(parent, startParameter)

        then:
        1 * plugin1.registerBuildServices(_)
        1 * plugin2.registerBuildServices(_)
    }

    def throwsExceptionForUnknownDomainObject() {
        when:
        registry.get(ServiceRegistryFactory).createFor("string")
        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Cannot create services for unknown domain object of type String."
    }

    def canCreateServicesForAGradleInstance() {
        setup:
        GradleInternal gradle = Mock()
        def registry = registry.get(ServiceRegistryFactory).createFor(gradle)
        expect:
        registry instanceof GradleScopeServices
    }

    def "closing the registry closes gradle scoped services, closing project services"() {
        given:
        GradleInternal gradle = Mock()
        def gradleRegistry = registry.get(ServiceRegistryFactory).createFor(gradle)
        def project = Mock(ProjectInternal)
        def projectRegistry = gradleRegistry.get(ServiceRegistryFactory).createFor(project)

        expect:
        !gradleRegistry.closed
        !projectRegistry.closed

        when:
        registry.close()

        then:
        gradleRegistry.closed
        projectRegistry.closed
    }

    def canCreateServicesForASettingsInstance() {
        setup:
        SettingsInternal settings = Mock()
        def registry = registry.get(ServiceRegistryFactory).createFor(settings)
        expect:
        registry instanceof SettingsScopeServices
    }

    def providesAListenerManager() {
        setup:
        ListenerManager listenerManager = expectListenerManagerCreated()
        expect:
        assertThat(registry.get(ListenerManager), sameInstance(listenerManager))
    }

    def providesAScriptCompilerFactory() {
        setup:
        expectListenerManagerCreated()

        expect:
        registry.get(ScriptCompilerFactory) instanceof DefaultScriptCompilerFactory
        registry.get(ScriptCompilerFactory) == registry.get(ScriptCompilerFactory)
    }

    def providesACacheRepositoryAndCleansUpOnClose() {
        expect:
        registry.get(CacheRepository) instanceof DefaultCacheRepository
        registry.get(CacheRepository) == registry.get(CacheRepository)
    }

    def providesAnInitScriptHandler() {
        setup:
        allowGetCoreImplClassLoader()
        expectListenerManagerCreated()
        allowGetGradleDistributionLocator()

        expect:
        registry.get(InitScriptHandler) instanceof InitScriptHandler
        registry.get(InitScriptHandler) == registry.get(InitScriptHandler)
    }

    def providesAScriptObjectConfigurerFactory() {
        setup:
        allowGetCoreImplClassLoader()
        expectListenerManagerCreated()
        expect:
        assertThat(registry.get(ScriptPluginFactory), instanceOf(DefaultScriptPluginFactory))
        assertThat(registry.get(ScriptPluginFactory), sameInstance(registry.get(ScriptPluginFactory)))
    }

    def providesASettingsProcessor() {
        setup:
        allowGetCoreImplClassLoader()
        expectListenerManagerCreated()
        expect:
        assertThat(registry.get(SettingsProcessor), instanceOf(PropertiesLoadingSettingsProcessor))
        assertThat(registry.get(SettingsProcessor), sameInstance(registry.get(SettingsProcessor)))
    }

    def providesAnExceptionAnalyser() {
        setup:
        expectListenerManagerCreated()
        expect:
        assertThat(registry.get(ExceptionAnalyser), instanceOf(StackTraceSanitizingExceptionAnalyser))
        assertThat(registry.get(ExceptionAnalyser).analyser, instanceOf(MultipleBuildFailuresExceptionAnalyser))
        assertThat(registry.get(ExceptionAnalyser).analyser.delegate, instanceOf(DefaultExceptionAnalyser))
        assertThat(registry.get(ExceptionAnalyser), sameInstance(registry.get(ExceptionAnalyser)))
    }

    def providesAWorkerProcessFactory() {
        setup:
        expectParentServiceLocated(MessagingServer)
        allowGetCoreImplClassLoader()

        expect:
        assertThat(registry.getFactory(WorkerProcessBuilder), instanceOf(DefaultWorkerProcessFactory))
    }

    def providesAnIsolatedAntBuilder() {
        setup:
        expectParentServiceLocated(ClassLoaderFactory)
        allowGetCoreImplClassLoader()
        expect:

        assertThat(registry.get(IsolatedAntBuilder), instanceOf(DefaultIsolatedAntBuilder))
        assertThat(registry.get(IsolatedAntBuilder), sameInstance(registry.get(IsolatedAntBuilder)))
    }

    def providesAProjectFactory() {
        setup:
        expectParentServiceLocated(Instantiator)
        expectParentServiceLocated(ClassGenerator)
        expect:
        assertThat(registry.get(IProjectFactory), instanceOf(ProjectFactory))
        assertThat(registry.get(IProjectFactory), sameInstance(registry.get(IProjectFactory)))
    }

    def providesABuildConfigurer() {
        expect:
        assertThat(registry.get(BuildConfigurer), instanceOf(DefaultBuildConfigurer))
        assertThat(registry.get(BuildConfigurer), sameInstance(registry.get(BuildConfigurer)))
    }

    def providesAPropertiesLoader() {
        expect:
        assertThat(registry.get(IGradlePropertiesLoader), instanceOf(DefaultGradlePropertiesLoader))
        assertThat(registry.get(IGradlePropertiesLoader), sameInstance(registry.get(IGradlePropertiesLoader)))
    }

    def providesABuildLoader() {
        setup:
        expectParentServiceLocated(Instantiator)
        expect:
        assertThat(registry.get(BuildLoader), instanceOf(ProjectPropertySettingBuildLoader))
        assertThat(registry.get(BuildLoader), sameInstance(registry.get(BuildLoader)))
    }

    def providesAProfileEventAdapter() {
        setup:
        expectParentServiceLocated(BuildRequestMetaData)
        expectListenerManagerCreated()

        expect:
        assertThat(registry.get(ProfileEventAdapter), instanceOf(ProfileEventAdapter))
        assertThat(registry.get(ProfileEventAdapter), sameInstance(registry.get(ProfileEventAdapter)))
    }

    def "provides a project registry"() {
        when:
        def projectRegistry = registry.get(ProjectRegistry)
        def secondRegistry = registry.get(ProjectRegistry)

        then:
        projectRegistry instanceof DefaultProjectRegistry
        projectRegistry sameInstance(secondRegistry)
    }

    private <T> T expectParentServiceLocated(Class<T> type) {
        T t = Mock(type)
        parent.get(type) >> t
        t
    }

    private ListenerManager expectListenerManagerCreated() {
        final ListenerManager listenerManager = new DefaultListenerManager()
        final ListenerManager listenerManagerParent = Mock()
        parent.get(ListenerManager) >> listenerManagerParent
        1 * listenerManagerParent.createChild() >> listenerManager
        listenerManager
    }

    private void allowGetCoreImplClassLoader() {
        classLoaderRegistry.getCoreImplClassLoader() >> new ClassLoader() {}
    }

    private void allowGetGradleDistributionLocator() {
        parent.get(GradleDistributionLocator) >> Mock(GradleDistributionLocator)
    }

    public interface ClosableCacheFactory extends CacheFactory {
        void close()
    }
}