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

package org.gradle.initialization;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.StartParameter;
import org.gradle.api.initialization.ProjectDescriptor;
import org.gradle.api.internal.ExceptionAnalyser;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.SettingsInternal;
import org.gradle.api.internal.file.TestFiles;
import org.gradle.api.internal.initialization.ClassLoaderScope;
import org.gradle.api.internal.project.DefaultProject;
import org.gradle.configuration.BuildConfigurer;
import org.gradle.execution.BuildExecuter;
import org.gradle.execution.TaskGraphExecuter;
import org.gradle.logging.LoggingManagerInternal;
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider;
import org.gradle.util.JUnit4GroovyMockery;
import org.gradle.util.TestUtil;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(org.jmock.integration.junit4.JMock.class)
public class DefaultGradleLauncherTest {
    private BuildLoader buildLoaderMock;
    private InitScriptHandler initscriptHandlerMock;
    private SettingsHandler settingsHandlerMock;
    private BuildConfigurer buildConfigurerMock;
    private DefaultProject expectedRootProject;
    private DefaultProject expectedCurrentProject;
    private SettingsInternal settingsMock;
    private StartParameter expectedStartParams;
    private GradleInternal gradleMock;
    private BuildListener buildBroadcaster;
    private BuildExecuter buildExecuter;

    private GradleLauncher gradleLauncher;

    private TaskGraphExecuter taskExecuterMock;

    private ProjectDescriptor expectedRootProjectDescriptor;

    private ProjectDescriptor expectedDefaultProjectDescriptor;

    private JUnit4Mockery context = new JUnit4GroovyMockery();

    private ClassLoaderScope settingsClassLoaderScope = context.mock(ClassLoaderScope.class);
    private ClassLoaderScope baseClassLoaderScope = context.mock(ClassLoaderScope.class);
    private ExceptionAnalyser exceptionAnalyserMock = context.mock(ExceptionAnalyser.class);
    private LoggingManagerInternal loggingManagerMock = context.mock(LoggingManagerInternal.class);
    private ModelConfigurationListener modelListenerMock = context.mock(ModelConfigurationListener.class);
    private TasksCompletionListener tasksCompletionListener = context.mock(TasksCompletionListener.class);
    private BuildCompletionListener buildCompletionListener = context.mock(BuildCompletionListener.class);
    private Closeable buildServices = context.mock(Closeable.class);
    public TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider();

    @Before
    public void setUp() {
        initscriptHandlerMock = context.mock(InitScriptHandler.class);
        settingsHandlerMock = context.mock(SettingsHandler.class);
        settingsMock = context.mock(SettingsInternal.class);
        taskExecuterMock = context.mock(TaskGraphExecuter.class);
        buildLoaderMock = context.mock(BuildLoader.class);
        buildConfigurerMock = context.mock(BuildConfigurer.class);
        gradleMock = context.mock(GradleInternal.class);
        buildBroadcaster = context.mock(BuildListener.class);
        buildExecuter = context.mock(BuildExecuter.class);
        boolean expectedSearchUpwards = false;

        File expectedRootDir = tmpDir.file("rootDir");
        File expectedCurrentDir = new File(expectedRootDir, "currentDir");

        expectedRootProjectDescriptor = new DefaultProjectDescriptor(null, "someName", new File("somedir"), new DefaultProjectDescriptorRegistry(),
                TestFiles.resolver(expectedRootDir));
        expectedRootProject = TestUtil.createRootProject(expectedRootDir);
        expectedDefaultProjectDescriptor = new DefaultProjectDescriptor(null, "default", new File("default"), new DefaultProjectDescriptorRegistry(),
                TestFiles.resolver(expectedCurrentDir));
        expectedCurrentProject = TestUtil.createRootProject(expectedCurrentDir);

        expectedStartParams = new StartParameter();
        expectedStartParams.setCurrentDir(expectedCurrentDir);
        expectedStartParams.setSearchUpwards(expectedSearchUpwards);
        expectedStartParams.setGradleUserHomeDir(tmpDir.createDir("gradleUserHome"));

        gradleLauncher = new DefaultGradleLauncher(gradleMock, initscriptHandlerMock, settingsHandlerMock,
                buildLoaderMock, buildConfigurerMock, buildBroadcaster, exceptionAnalyserMock, loggingManagerMock,
                modelListenerMock, tasksCompletionListener, buildExecuter, buildCompletionListener, buildServices);

        context.checking(new Expectations() {
            {
                allowing(settingsMock).getRootProject();
                will(returnValue(expectedRootProjectDescriptor));
                allowing(settingsMock).getDefaultProject();
                will(returnValue(expectedRootProjectDescriptor));
                allowing(settingsMock).getClassLoaderScope();
                will(returnValue(settingsClassLoaderScope));
                allowing(settingsClassLoaderScope).createSibling();
                will(returnValue(baseClassLoaderScope));
                allowing(baseClassLoaderScope).lock();
                will(returnValue(baseClassLoaderScope));
                allowing(gradleMock).getRootProject();
                will(returnValue(expectedRootProject));
                allowing(gradleMock).getDefaultProject();
                will(returnValue(expectedCurrentProject));
                allowing(gradleMock).getTaskGraph();
                will(returnValue(taskExecuterMock));
                allowing(gradleMock).getStartParameter();
                will(returnValue(expectedStartParams));
            }
        });
    }

    @Test
    public void testRun() {
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        expectDagBuilt();
        expectTasksRun();
        expectBuildListenerCallbacks();
        BuildResult buildResult = gradleLauncher.run();
        assertThat(buildResult.getGradle(), sameInstance((Object) gradleMock));
        assertThat(buildResult.getFailure(), nullValue());
    }

    @Test
    public void testGetBuildAnalysis() {
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        expectBuildListenerCallbacks();
        context.checking(new Expectations() {{
            one(buildLoaderMock).load(expectedRootProjectDescriptor, expectedDefaultProjectDescriptor, gradleMock, baseClassLoaderScope);
            one(buildConfigurerMock).configure(gradleMock);
        }});
        BuildResult buildResult = gradleLauncher.getBuildAnalysis();
        assertThat(buildResult.getFailure(), nullValue());
        assertThat(buildResult.getGradle(), sameInstance((Object) gradleMock));
    }

    @Test
    public void testGetBuildAnalysisWithFailure() {
        final RuntimeException exception = new RuntimeException();
        final RuntimeException transformedException = new RuntimeException();
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        context.checking(new Expectations() {{
            one(buildBroadcaster).buildStarted(gradleMock);
            one(buildLoaderMock).load(expectedRootProjectDescriptor, expectedDefaultProjectDescriptor, gradleMock, baseClassLoaderScope);
            will(throwException(exception));
            one(exceptionAnalyserMock).transform(exception);
            will(returnValue(transformedException));
            one(buildBroadcaster).buildFinished(with(any(BuildResult.class)));
        }});
        BuildResult buildResult = gradleLauncher.getBuildAnalysis();
        assertThat(buildResult.getGradle(), sameInstance((Object) gradleMock));
        assertThat((RuntimeException) buildResult.getFailure(), sameInstance(transformedException));
    }

    @Test
    public void testNotifiesListenerOfBuildAnalysisStages() {
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        expectBuildListenerCallbacks();
        context.checking(new Expectations() {{
            one(buildLoaderMock).load(expectedRootProjectDescriptor, expectedDefaultProjectDescriptor, gradleMock, baseClassLoaderScope);
            one(buildConfigurerMock).configure(gradleMock);
        }});

        gradleLauncher.getBuildAnalysis();
    }

    @Test
    public void testNotifiesListenerOfBuildStages() {
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        expectDagBuilt();
        expectTasksRun();
        expectBuildListenerCallbacks();

        gradleLauncher.run();
    }

    @Test
    public void testNotifiesListenerOnSettingsInitWithFailure() {
        final RuntimeException failure = new RuntimeException();
        final RuntimeException transformedException = new RuntimeException();
        expectLoggingStarted();
        expectInitScriptsExecuted();
        context.checking(new Expectations() {{
            one(buildBroadcaster).buildStarted(gradleMock);
            one(settingsHandlerMock).findAndLoadSettings(gradleMock);
            will(throwException(failure));
            one(exceptionAnalyserMock).transform(failure);
            will(returnValue(transformedException));
            one(buildBroadcaster).buildFinished(with(result(sameInstance(transformedException))));
        }});

        BuildResult buildResult = gradleLauncher.run();
        assertThat(buildResult.getFailure(), sameInstance((Throwable) transformedException));
    }

    @Test
    public void testNotifiesListenerOnBuildCompleteWithFailure() {
        final RuntimeException failure = new RuntimeException();
        final RuntimeException transformedException = new RuntimeException();
        expectLoggingStarted();
        expectInitScriptsExecuted();
        expectSettingsBuilt();
        expectDagBuilt();
        expectTasksRunWithFailure(failure);
        context.checking(new Expectations() {{
            one(buildBroadcaster).buildStarted(gradleMock);
            one(buildBroadcaster).projectsLoaded(gradleMock);
            one(buildBroadcaster).projectsEvaluated(gradleMock);
            one(modelListenerMock).onConfigure(gradleMock);
            one(exceptionAnalyserMock).transform(failure);
            will(returnValue(transformedException));
            one(buildBroadcaster).buildFinished(with(result(sameInstance(transformedException))));
        }});

        BuildResult buildResult = gradleLauncher.run();
        assertThat(buildResult.getFailure(), sameInstance((Throwable) transformedException));
    }

    @Test
    public void testCleansUpOnStop() throws IOException {
        context.checking(new Expectations() {{
            one(loggingManagerMock).stop();
            one(buildServices).close();
            one(buildCompletionListener).completed();
        }});

        gradleLauncher.stop();
    }

    private void expectLoggingStarted() {
        context.checking(new Expectations() {{
            one(loggingManagerMock).start();
        }});
    }

    private void expectInitScriptsExecuted() {
        context.checking(new Expectations() {{
            one(initscriptHandlerMock).executeScripts(gradleMock);
        }});
    }

    private void expectSettingsBuilt() {
        context.checking(new Expectations() {
            {
                one(settingsHandlerMock).findAndLoadSettings(gradleMock);
                will(returnValue(settingsMock));
                one(buildBroadcaster).settingsEvaluated(settingsMock);
            }
        });
    }

    private void expectBuildListenerCallbacks() {
        context.checking(new Expectations() {{
            one(buildBroadcaster).buildStarted(gradleMock);
            one(buildBroadcaster).projectsLoaded(gradleMock);
            one(buildBroadcaster).projectsEvaluated(gradleMock);
            one(buildBroadcaster).buildFinished(with(result(nullValue(Throwable.class))));
            one(modelListenerMock).onConfigure(gradleMock);
        }});
    }

    private void expectDagBuilt() {
        context.checking(new Expectations() {
            {
                one(buildLoaderMock).load(expectedRootProjectDescriptor, expectedDefaultProjectDescriptor, gradleMock, baseClassLoaderScope);
                one(buildConfigurerMock).configure(gradleMock);
                one(buildExecuter).select(gradleMock);
            }
        });
    }

    private void expectTasksRun() {
        context.checking(new Expectations() {
            {
                one(buildExecuter).execute();
                one(tasksCompletionListener).onTasksFinished(gradleMock);
            }
        });
    }

    private void expectTasksRunWithFailure(final Throwable failure) {
        context.checking(new Expectations() {
            {
                one(buildExecuter).execute();
                will(throwException(failure));
            }
        });
    }

    private Matcher<BuildResult> result(final Matcher<? extends Throwable> exceptionMatcher) {
        return new BaseMatcher<BuildResult>() {
            public void describeTo(Description description) {
                description.appendText("matching build result");
            }

            public boolean matches(Object actual) {
                BuildResult result = (BuildResult) actual;
                return (result.getGradle() == gradleMock) && exceptionMatcher.matches(result.getFailure());
            }
        };
    }
}
