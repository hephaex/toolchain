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

package org.gradle.integtests.samples

import org.gradle.integtests.fixtures.AbstractIntegrationTest
import org.gradle.integtests.fixtures.DefaultTestExecutionResult
import org.gradle.integtests.fixtures.ForkScalaCompileInDaemonModeFixture
import org.gradle.integtests.fixtures.Sample
import org.gradle.integtests.fixtures.executer.GradleContextualExecuter
import org.gradle.test.fixtures.file.TestFile
import org.junit.Rule
import org.junit.Test

import static org.hamcrest.Matchers.containsString

class SamplesMixedJavaAndScalaIntegrationTest extends AbstractIntegrationTest {

    @Rule public final Sample sample = new Sample(testDirectoryProvider, 'scala/mixedJavaAndScala')
    @Rule public final ForkScalaCompileInDaemonModeFixture forkScalaCompileInDaemonModeFixture = new ForkScalaCompileInDaemonModeFixture(executer, testDirectoryProvider)

    @Test
    public void canBuildJar() {
        TestFile projectDir = sample.dir

        // Build and test projects
        executer.inDirectory(projectDir).withTasks('clean', 'build').run()

        // Check tests have run
        def result = new DefaultTestExecutionResult(projectDir)
        result.assertTestClassesExecuted('org.gradle.sample.PersonTest')

        // Check contents of Jar
        TestFile jarContents = file('jar')
        projectDir.file("build/libs/mixedJavaAndScala-1.0.jar").unzipTo(jarContents)
        jarContents.assertHasDescendants(
                'META-INF/MANIFEST.MF',
                'org/gradle/sample/Person.class',
                'org/gradle/sample/impl/JavaPerson.class',
                'org/gradle/sample/impl/PersonImpl.class',
                'org/gradle/sample/impl/PersonList.class'
        )
    }

    @Test
    public void canBuildDocs() {
        if (GradleContextualExecuter.isDaemon()) {
            // don't load scala into the daemon as it exhausts permgen
            return
        }

        TestFile projectDir = sample.dir
        executer.inDirectory(projectDir).withTasks('clean', 'javadoc', 'scaladoc').run()

        TestFile javadocsDir = projectDir.file("build/docs/javadoc")
        javadocsDir.file("index.html").assertIsFile()
        javadocsDir.file("index.html").assertContents(containsString('mixedJavaAndScala 1.0 API'))
        javadocsDir.file("org/gradle/sample/Person.html").assertIsFile()
        javadocsDir.file("org/gradle/sample/impl/JavaPerson.html").assertIsFile()

        TestFile scaladocsDir = projectDir.file("build/docs/scaladoc")
        scaladocsDir.file("index.html").assertIsFile()
        scaladocsDir.file("index.html").assertContents(containsString('mixedJavaAndScala 1.0 API'))
        scaladocsDir.file("org/gradle/sample/impl/PersonImpl.html").assertIsFile()
        scaladocsDir.file("org/gradle/sample/impl/JavaPerson.html").assertIsFile()
        scaladocsDir.file("org/gradle/sample/impl/PersonList.html").assertIsFile()
    }

}