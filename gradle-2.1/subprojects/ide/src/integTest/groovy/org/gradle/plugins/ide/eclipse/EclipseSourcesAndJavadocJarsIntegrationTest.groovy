/*
 * Copyright 2014 the original author or authors.
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
package org.gradle.plugins.ide.eclipse

import org.gradle.plugins.ide.AbstractSourcesAndJavadocJarsIntegrationTest
import org.gradle.test.fixtures.server.http.HttpArtifact

class EclipseSourcesAndJavadocJarsIntegrationTest extends AbstractSourcesAndJavadocJarsIntegrationTest {
    @Override
    String getIdeTask() {
        return "eclipseClasspath"
    }

    void ideFileContainsSourcesAndJavadocEntry(String sourcesClassifier = "sources", String javadocClassifier = "javadoc") {
        def classpath = new EclipseClasspathFixture(testDirectory, executer.gradleUserHomeDir)
        def lib = classpath.libs[0]
        assert lib.sourcePath.endsWith("/module-1.0-${sourcesClassifier}.jar")
        assert lib.javadocLocation.endsWith("/module-1.0-${javadocClassifier}.jar!/")
    }

    void ideFileContainsNoSourcesAndJavadocEntry() {
        def classpath = new EclipseClasspathFixture(testDirectory, executer.gradleUserHomeDir)
        def lib = classpath.libs[0]
        lib.assertHasNoSource()
        lib.assertHasNoJavadoc()
    }

    @Override
    void expectBehaviorAfterBrokenMavenArtifact(HttpArtifact httpArtifact) {
        httpArtifact.expectHead()
    }

    @Override
    void expectBehaviorAfterBrokenIvyArtifact(HttpArtifact httpArtifact) {}
}