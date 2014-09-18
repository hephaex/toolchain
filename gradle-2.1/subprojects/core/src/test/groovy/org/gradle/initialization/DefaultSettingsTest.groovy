/*
 * Copyright 2009 the original author or authors.
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
import org.gradle.api.Project
import org.gradle.api.UnknownProjectException
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.internal.ThreadGlobalInstantiator
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.initialization.ClassLoaderScope
import org.gradle.api.internal.initialization.ScriptHandlerFactory
import org.gradle.api.plugins.PluginContainer
import org.gradle.configuration.ScriptPluginFactory
import org.gradle.groovy.scripts.ScriptSource
import org.gradle.internal.service.ServiceRegistry
import org.gradle.internal.service.scopes.ServiceRegistryFactory
import org.gradle.util.JUnit4GroovyMockery
import org.jmock.integration.junit4.JMock
import org.jmock.lib.legacy.ClassImposteriser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

@RunWith(JMock)
class DefaultSettingsTest {
    File settingsDir
    StartParameter startParameter
    ClassLoaderScope classLoaderScope
    Map gradleProperties
    ScriptSource scriptSourceMock
    GradleInternal gradleMock
    DefaultSettings settings
    JUnit4GroovyMockery context = new JUnit4GroovyMockery()
    ProjectDescriptorRegistry projectDescriptorRegistry
    ServiceRegistryFactory serviceRegistryFactory
    PluginContainer pluginContainer
    FileResolver fileResolver
    ScriptPluginFactory scriptPluginFactory
    ScriptHandlerFactory scriptHandlerFactory

    @Before
    public void setUp() {
        context.setImposteriser(ClassImposteriser.INSTANCE)
        settingsDir = new File('/somepath/root').absoluteFile
        gradleProperties = [someGradleProp: 'someValue']
        startParameter = new StartParameter(currentDir: new File(settingsDir, 'current'), gradleUserHomeDir: new File('gradleUserHomeDir'))
        classLoaderScope = context.mock(ClassLoaderScope)

        scriptSourceMock = context.mock(ScriptSource)
        gradleMock = context.mock(GradleInternal)
        serviceRegistryFactory = context.mock(ServiceRegistryFactory.class)
        pluginContainer = context.mock(PluginContainer.class)
        scriptPluginFactory = context.mock(ScriptPluginFactory.class)
        scriptHandlerFactory = context.mock(ScriptHandlerFactory.class)
        fileResolver = context.mock(FileResolver.class)
        projectDescriptorRegistry = new DefaultProjectDescriptorRegistry()

        def settingsServices = context.mock(ServiceRegistry.class)
        context.checking{
                one(serviceRegistryFactory).createFor(with(any(Settings.class)));
                will(returnValue(settingsServices));
                one(settingsServices).get(PluginContainer.class);
                will(returnValue(pluginContainer));
                one(settingsServices).get(FileResolver.class);
                will(returnValue(fileResolver));
                one(settingsServices).get(ScriptPluginFactory.class);
                will(returnValue(scriptPluginFactory));
                one(settingsServices).get(ScriptHandlerFactory.class);
                will(returnValue(scriptHandlerFactory));
                one(settingsServices).get(ProjectDescriptorRegistry.class);
                will(returnValue(projectDescriptorRegistry));
        }
        settings = ThreadGlobalInstantiator.orCreate.newInstance(DefaultSettings, serviceRegistryFactory,
                    gradleMock, classLoaderScope, settingsDir, scriptSourceMock, startParameter);

    }

    @Test
    public void testSettings() {
        assert settings.startParameter.is(startParameter)
        assertSame(settings, settings.getSettings())
        assertEquals(settingsDir, settings.getSettingsDir())

        assertNull(settings.getRootProject().getParent())
        assertEquals(settingsDir, settings.getRootProject().getProjectDir())
        assertEquals(settings.getRootProject().getProjectDir().getName(), settings.getRootProject().getName())
        assertEquals(settings.rootProject.buildFileName, Project.DEFAULT_BUILD_FILE);
        assertSame(gradleMock, settings.gradle)
    }

    @Test
    public void testInclude() {
        ProjectDescriptor rootProjectDescriptor = settings.getRootProject();
        String projectA = "a"
        String projectB = "b"
        String projectC = "c"
        String projectD = "d"
        settings.include([projectA, "$projectB:$projectC"] as String[])

        assertEquals(2, rootProjectDescriptor.getChildren().size())
        testDescriptor(settings.project(":$projectA"), projectA, new File(settingsDir, projectA))
        testDescriptor(settings.project(":$projectB"), projectB, new File(settingsDir, projectB))

        assertEquals(1, settings.project(":$projectB").getChildren().size())
        testDescriptor(settings.project(":$projectB:$projectC"), projectC, new File(settingsDir, "$projectB/$projectC"))
    }

    @Test
    public void testIncludeFlat() {
        ProjectDescriptor rootProjectDescriptor = settings.getRootProject();
        String projectA = "a"
        String projectB = "b"
        String[] paths = [projectA, projectB]
        settings.includeFlat(paths)
        assertEquals(2, rootProjectDescriptor.getChildren().size())
        testDescriptor(settings.project(":" + projectA), projectA, new File(settingsDir.parentFile, projectA))
        testDescriptor(settings.project(":" + projectB), projectB, new File(settingsDir.parentFile, projectB))
    }

    private void testDescriptor(DefaultProjectDescriptor descriptor, String name, File projectDir) {
        assertEquals(name, descriptor.getName(), descriptor.getName())
        assertEquals(projectDir, descriptor.getProjectDir())
    }

    @Test
    public void testCreateProjectDescriptor() {
        String testName = "testname"
        File testDir = new File("testDir")
        DefaultProjectDescriptor projectDescriptor = settings.createProjectDescriptor(settings.getRootProject(), testName, testDir)
        assertSame(settings.getRootProject(), projectDescriptor.getParent())
        assertSame(settings.getProjectDescriptorRegistry(), projectDescriptor.getProjectDescriptorRegistry())
        assertEquals(testName, projectDescriptor.getName())
        assertEquals(testDir.canonicalFile, projectDescriptor.getProjectDir())
    }

    @Test
    public void testFindDescriptorByPath() {
        DefaultProjectDescriptor projectDescriptor = createTestDescriptor();
        DefaultProjectDescriptor foundProjectDescriptor = settings.project(projectDescriptor.getPath())
        assertSame(foundProjectDescriptor, projectDescriptor)
    }

    @Test
    public void testFindDescriptorByProjectDir() {
        DefaultProjectDescriptor projectDescriptor = createTestDescriptor()
        DefaultProjectDescriptor foundProjectDescriptor = settings.project(projectDescriptor.getProjectDir())
        assertSame(foundProjectDescriptor, projectDescriptor)
    }

    @Test(expected = UnknownProjectException)
    public void testDescriptorByPath() {
        DefaultProjectDescriptor projectDescriptor = createTestDescriptor()
        DefaultProjectDescriptor foundProjectDescriptor = settings.project(projectDescriptor.getPath())
        assertSame(foundProjectDescriptor, projectDescriptor)
        settings.project("unknownPath")
    }


    @Test(expected = UnknownProjectException)
    public void testDescriptorByProjectDir() {
        DefaultProjectDescriptor projectDescriptor = createTestDescriptor()
        DefaultProjectDescriptor foundProjectDescriptor = settings.project(projectDescriptor.getProjectDir())
        assertSame(foundProjectDescriptor, projectDescriptor)
        settings.project(new File("unknownPath"))
    }

    private DefaultProjectDescriptor createTestDescriptor() {
        String testName = "testname"
        File testDir = new File("testDir")
        return settings.createProjectDescriptor(settings.getRootProject(), testName, testDir)
    }

    @Test
    public void testCreateClassLoader() {
        StartParameter expectedStartParameter = settings.startParameter.newInstance()
        expectedStartParameter.setCurrentDir(new File(settingsDir, DefaultSettings.DEFAULT_BUILD_SRC_DIR))
        def createdClassLoaderScope = settings.getClassLoaderScope()
        assertSame(createdClassLoaderScope, classLoaderScope)
    }

    @Test
    public void testCanGetAndSetDynamicProperties() {
        settings.ext.dynamicProp = 'value'
        assertEquals('value', settings.dynamicProp)
    }

    @Test(expected = MissingPropertyException)
    public void testPropertyMissing() {
        settings.unknownProp
    }

    @Test
    public void testGetRootDir() {
        assertEquals(settingsDir, settings.rootDir);
    }

    @Test
    public void testHasUsefulToString() {
        assertEquals('settings \'root\'', settings.toString())
    }
}
