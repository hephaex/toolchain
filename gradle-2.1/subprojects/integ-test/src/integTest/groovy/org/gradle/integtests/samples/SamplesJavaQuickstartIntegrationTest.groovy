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
import org.gradle.integtests.fixtures.Sample
import org.gradle.test.fixtures.file.TestFile
import org.junit.Rule
import org.junit.Test

import java.util.jar.Manifest

import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertThat

class SamplesJavaQuickstartIntegrationTest extends  AbstractIntegrationTest {

    @Rule public final Sample sample = new Sample(testDirectoryProvider, 'java/quickstart')

    @Test
    public void canBuildAndUploadJar() {
        TestFile javaprojectDir = sample.dir

        // Build and test projects
        executer.inDirectory(javaprojectDir).withTasks('clean', 'build', 'uploadArchives').run()

        // Check tests have run
        def result = new DefaultTestExecutionResult(javaprojectDir)
        result.assertTestClassesExecuted('org.gradle.PersonTest')

        // Check jar exists
        javaprojectDir.file("build/libs/quickstart-1.0.jar").assertIsFile()

        // Check jar uploaded
        javaprojectDir.file('repos/quickstart-1.0.jar').assertIsFile()

        // Check contents of Jar
        TestFile jarContents = file('jar')
        javaprojectDir.file('repos/quickstart-1.0.jar').unzipTo(jarContents)
        jarContents.assertHasDescendants(
                'META-INF/MANIFEST.MF',
                'org/gradle/Person.class',
                'org/gradle/resource.xml'
        )

        // Check contents of manifest
        Manifest manifest = new Manifest()
        jarContents.file('META-INF/MANIFEST.MF').withInputStream { manifest.read(it) }
        assertThat(manifest.mainAttributes.size(), equalTo(3))
        assertThat(manifest.mainAttributes.getValue('Manifest-Version'), equalTo('1.0'))
        assertThat(manifest.mainAttributes.getValue('Implementation-Title'), equalTo('Gradle Quickstart'))
        assertThat(manifest.mainAttributes.getValue('Implementation-Version'), equalTo('1.0'))
    }
}
