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
package org.gradle.integtests.tooling.fixture

import org.gradle.integtests.fixtures.executer.GradleDistribution
import org.gradle.integtests.fixtures.executer.IntegrationTestBuildContext
import org.gradle.integtests.fixtures.executer.UnderDevelopmentGradleDistribution
import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.tooling.GradleConnector
import org.gradle.util.GradleVersion
import org.gradle.util.SetSystemProperties
import org.junit.Rule
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * A spec that executes tests against all compatible versions of tooling API consumer and testDirectoryProvider, including the current Gradle version under test.
 *
 * <p>A test class or test method can be annotated with the following annotations to specify which versions the test is compatible with:
 * </p>
 *
 * <ul>
 *     <li>{@link ToolingApiVersion} - specifies the tooling API consumer versions that the test is compatible with.
 *     <li>{@link TargetGradleVersion} - specifies the tooling API testDirectoryProvider versions that the test is compatible with.
 * </ul>
 */
@RunWith(ToolingApiCompatibilitySuiteRunner)
@ToolingApiVersion('>=1.2')
@TargetGradleVersion('>=1.0-milestone-8')
abstract class ToolingApiSpecification extends Specification {
    static final Logger LOGGER = LoggerFactory.getLogger(ToolingApiSpecification)
    @Rule
    public final SetSystemProperties sysProperties = new SetSystemProperties()
    @Rule
    public final TestNameTestDirectoryProvider temporaryFolder = new TestNameTestDirectoryProvider()
    final GradleDistribution dist = new UnderDevelopmentGradleDistribution()
    final IntegrationTestBuildContext buildContext = new IntegrationTestBuildContext()
    final ToolingApi toolingApi = new ToolingApi(dist, temporaryFolder)
    private static final ThreadLocal<GradleDistribution> VERSION = new ThreadLocal<GradleDistribution>()

    static void selectTargetDist(GradleDistribution version) {
        VERSION.set(version)
    }

    static GradleDistribution getTargetDist() {
        VERSION.get()
    }

    void setup() {
        def consumerGradle = GradleVersion.current()
        def target = GradleVersion.version(VERSION.get().version.version)
        LOGGER.info(" Using Tooling API consumer ${consumerGradle}, provider ${target}")
        this.toolingApi.withConnector {
            if (consumerGradle.version != target.version) {
                LOGGER.info("Overriding daemon tooling API provider to use installation: " + target);
                it.useInstallation(new File(getTargetDist().gradleHomeDir.absolutePath))
                it.embedded(false)
            }
        }
    }

    public void withConnector(Closure cl) {
        toolingApi.withConnector(cl)
    }

    public <T> T withConnection(Closure<T> cl) {
        toolingApi.withConnection(cl)
    }

    public <T> T withConnection(GradleConnector connector, Closure<T> cl) {
        toolingApi.withConnection(connector, cl)
    }

    public ConfigurableOperation withModel(Class modelType, Closure cl = {}) {
        withConnection {
            def model = it.model(modelType)
            cl(model)
            new ConfigurableOperation(model).buildModel()
        }
    }

    public ConfigurableOperation withBuild(Closure cl = {}) {
        withConnection {
            def build = it.newBuild()
            cl(build)
            def out = new ConfigurableOperation(build)
            build.run()
            out
        }
    }

    def connector() {
        toolingApi.connector()
    }

    TestFile getProjectDir() {
        temporaryFolder.testDirectory
    }

    TestFile getBuildFile() {
        file("build.gradle")
    }

    TestFile getSettingsFile() {
        file("settings.gradle")
    }

    TestFile file(Object... path) {
        projectDir.file(path)
    }

    /**
     * Returns the set of implicit task names expected for a non-root project for the target Gradle version.
     */
    Set<String> getImplicitTasks() {
        if (GradleVersion.version(targetDist.version.baseVersion.version) >= GradleVersion.version("2.1")) {
            return ['components', 'dependencies', 'dependencyInsight', 'help', 'projects', 'properties', 'tasks']
        } else {
            return ['dependencies', 'dependencyInsight', 'help', 'projects', 'properties', 'tasks']
        }
    }

    /**
     * Returns the set of implicit selector names expected for a non-root project for the target Gradle version.
     *
     * <p>Note that in some versions the handling of implicit selectors was broken, so this method may return a different value
     * to {@link #getImplicitTasks()}.
     */
    Set<String> getImplicitSelectors() {
        if (GradleVersion.version(targetDist.version.baseVersion.version) <= GradleVersion.version("2.0")) {
            // Implicit tasks were ignored
            return []
        }
        return getImplicitTasks()
    }

    /**
     * Returns the set of implicit task names expected for a root project for the target Gradle version.
     */
    Set<String> getRootProjectImplicitTasks() {
        def targetVersion = GradleVersion.version(targetDist.version.baseVersion.version)
        if (targetVersion == GradleVersion.version("1.6")) {
            return implicitTasks + ['setupBuild']
        }
        return implicitTasks + ['init', 'wrapper']
    }

    /**
     * Returns the set of implicit selector names expected for a root project for the target Gradle version.
     *
     * <p>Note that in some versions the handling of implicit selectors was broken, so this method may return a different value
     * to {@link #getRootProjectImplicitTasks()}.
     */
    Set<String> getRootProjectImplicitSelectors() {
        def targetVersion = GradleVersion.version(targetDist.version.baseVersion.version)
        if (targetVersion == GradleVersion.version("1.6")) {
            // Implicit tasks were ignored, and setupBuild was added as a regular task
            return ['setupBuild']
        }
        if (targetVersion <= GradleVersion.version("2.0")) {
            // Implicit tasks were ignored
            return []
        }
        return rootProjectImplicitTasks
    }
}
