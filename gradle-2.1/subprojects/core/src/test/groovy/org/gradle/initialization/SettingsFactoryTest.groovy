/*
 * Copyright 2007-2008 the original author or authors.
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
package org.gradle.initialization

import org.gradle.StartParameter
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.ThreadGlobalInstantiator
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.initialization.DefaultClassLoaderCache
import org.gradle.api.internal.initialization.RootClassLoaderScope
import org.gradle.api.internal.initialization.ScriptHandlerFactory
import org.gradle.api.plugins.PluginContainer
import org.gradle.configuration.ScriptPluginFactory
import org.gradle.groovy.scripts.ScriptSource
import org.gradle.internal.service.ServiceRegistry
import org.gradle.internal.service.scopes.ServiceRegistryFactory
import org.gradle.util.WrapUtil
import spock.lang.Specification

class SettingsFactoryTest extends Specification {

    void createSettings() {
        given:
        def settingsDir = new File("settingsDir")
        def scriptSource = Mock(ScriptSource)
        def expectedGradleProperties = WrapUtil.toMap("key", "myvalue")
        def startParameter = new StartParameter()
        def serviceRegistryFactory = Mock(ServiceRegistryFactory)
        def settingsServices = Mock(ServiceRegistry)
        def pluginContainer = Mock(PluginContainer)
        def fileResolver = Mock(FileResolver)
        def scriptPluginFactory = Mock(ScriptPluginFactory)
        def scriptHandlerFactory = Mock(ScriptHandlerFactory)
        def projectDescriptorRegistry = Mock(ProjectDescriptorRegistry)

        1 * serviceRegistryFactory.createFor(_ as Settings) >> settingsServices
        1 * settingsServices.get(PluginContainer) >> pluginContainer
        1 * settingsServices.get(FileResolver) >> fileResolver
        1 * settingsServices.get(ScriptPluginFactory) >> scriptPluginFactory
        1 * settingsServices.get(ScriptHandlerFactory) >> scriptHandlerFactory
        1 * settingsServices.get(ProjectDescriptorRegistry) >> projectDescriptorRegistry
        1 * projectDescriptorRegistry.addProject(_ as DefaultProjectDescriptor)

        when:
        SettingsFactory settingsFactory = new SettingsFactory(ThreadGlobalInstantiator.getOrCreate(), serviceRegistryFactory);
        GradleInternal gradle = Mock(GradleInternal)

        DefaultSettings settings = (DefaultSettings) settingsFactory.createSettings(gradle,
                settingsDir, scriptSource, expectedGradleProperties, startParameter, new RootClassLoaderScope(getClass().classLoader, new DefaultClassLoaderCache()));

        then:
        gradle.is(settings.gradle)
        projectDescriptorRegistry.is(settings.projectDescriptorRegistry)
        expectedGradleProperties.each {
            settings.properties[it.key] == it.value
        }

        settingsDir.is settings.getSettingsDir()
        scriptSource.is settings.getSettingsScript()
        startParameter.is settings.getStartParameter()
    }
}
