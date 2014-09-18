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
package org.gradle.integtests.resolve.maven

import org.gradle.integtests.fixtures.AbstractHttpDependencyResolutionTest
import org.gradle.integtests.fixtures.executer.ProgressLoggingFixture
import org.gradle.test.fixtures.encoding.Identifier
import org.junit.Rule
import spock.lang.Unroll

class MavenHttpRepoResolveIntegrationTest extends AbstractHttpDependencyResolutionTest {

    @Rule ProgressLoggingFixture progressLogging = new ProgressLoggingFixture(executer, temporaryFolder)

    def "can resolve and cache dependencies from HTTP Maven repository"() {
        given:
        def projectB = mavenHttpRepo.module('group', 'projectB', '1.0').publish()
        def projectA = mavenHttpRepo.module('group', 'projectA').dependsOn('group', 'projectB', '1.0').publish()

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}
dependencies {
    compile 'group:projectA:1.0'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        projectA.pom.expectGet()
        projectA.artifact.expectGet()
        projectB.pom.expectGet()
        projectB.artifact.expectGet()

        and:
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('projectA-1.0.jar', 'projectB-1.0.jar')
        def snapshot = file('libs/projectA-1.0.jar').snapshot()

        and:
        progressLogging.downloadProgressLogged(projectA.pom.uri)
        progressLogging.downloadProgressLogged(projectA.artifact.uri)
        progressLogging.downloadProgressLogged(projectB.pom.uri)
        progressLogging.downloadProgressLogged(projectB.artifact.uri)

        when:
        server.resetExpectations()
        and:
        run 'retrieve'

        then:
        file('libs/projectA-1.0.jar').assertHasNotChangedSince(snapshot)
    }

    @Unroll
    def "can resolve with GAV containing #identifier characters"() {
        def value = identifier.safeForFileName().decorate("name")

        given:
        def projectB = mavenHttpRepo.module(value, value, value).publish()
        def projectA = mavenHttpRepo.module('group', 'projectA').dependsOn(value, value, value).publish()

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}
dependencies {
    compile 'group:projectA:1.0'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        projectA.pom.expectGet()
        projectA.artifact.expectGet()
        projectB.pom.expectGet()
        projectB.artifact.expectGet()

        and:
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('projectA-1.0.jar', "${value}-${value}.jar")

        and:
        progressLogging.downloadProgressLogged(projectA.pom.uri)
        progressLogging.downloadProgressLogged(projectA.artifact.uri)
        progressLogging.downloadProgressLogged(projectB.pom.uri)
        progressLogging.downloadProgressLogged(projectB.artifact.uri)

        where:
        identifier << Identifier.all
    }

    def "can resolve and cache artifact-only dependencies from an HTTP Maven repository"() {
        given:
        def projectA = mavenHttpRepo.module('group', 'projectA', '1.2')
        projectA.dependsOn('group', 'projectC', '1.2')
        projectA.publish()
        def projectB = mavenHttpRepo.module('group', 'projectB', '1.2')
        projectB.artifact(classifier: 'classy')
        projectB.dependsOn('group', 'projectC', '1.2')
        projectB.publish()

        and:
        buildFile << """
repositories {
    maven { url "${mavenHttpRepo.uri}" }
}
configurations {
    compile {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}
dependencies {
    compile 'group:projectA:1.2@jar'
    compile 'group:projectB:1.2:classy@jar'
}
task listJars << {
    assert configurations.compile.collect { it.name } == ['projectA-1.2.jar', 'projectB-1.2-classy.jar']
}
"""

        when:
        projectA.pom.expectGet()
        projectA.artifact.expectGet()
        projectB.pom.expectGet()
        projectB.artifact(classifier: 'classy').expectGet()

        then:
        succeeds('listJars')

        when:
        server.resetExpectations()
        // No extra calls for cached dependencies

        then:
        succeeds('listJars')
    }

    def "can resolve and cache artifact-only dependencies with no pom from an HTTP Maven repository"() {
        given:
        def projectA = mavenHttpRepo.module('group', 'projectA', '1.2')
        projectA.dependsOn('group', 'projectC', '1.2')
        projectA.publish()
        def projectB = mavenHttpRepo.module('group', 'projectB', '1.2')
        projectB.artifact(classifier: 'classy')
        projectB.dependsOn('group', 'projectC', '1.2')
        projectB.publish()

        and:
        buildFile << """
repositories {
    maven { url "${mavenHttpRepo.uri}" }
}
configurations {
    compile {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}
dependencies {
    compile 'group:projectA:1.2@jar'
    compile 'group:projectB:1.2:classy@jar'
}
task listJars << {
    assert configurations.compile.collect { it.name } == ['projectA-1.2.jar', 'projectB-1.2-classy.jar']
}
"""

        when:
        projectA.pom.expectGetMissing()
        projectA.artifact.expectHead()
        projectA.artifact.expectGet()
        projectB.pom.expectGetMissing()
        projectB.artifact(classifier: 'classy').expectHead()
        projectB.artifact(classifier: 'classy').expectGet()

        then:
        succeeds('listJars')

        when:
        server.resetExpectations()
        // No extra calls for cached dependencies

        then:
        succeeds('listJars')
    }

    def "can resolve and cache dependencies from multiple HTTP Maven repositories"() {
        given:
        def repo1 = mavenHttpRepo("repo1")
        def repo2 = mavenHttpRepo("repo2")

        buildFile << """
repositories {
    maven { url '${repo1.uri}' }
    maven { url '${repo2.uri}' }
}
configurations {
    compile {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}
dependencies {
    compile 'group:projectA:1.0', 'group:projectB:1.0'
}
task listJars << {
    assert configurations.compile.collect { it.name } == ['projectA-1.0.jar', 'projectB-1.0.jar']
}
"""

        def projectA = repo1.module('group', 'projectA').publish()
        def missingProjectB = repo1.module('group', 'projectB')
        def projectB = repo2.module('group', 'projectB').publish()

        when:
        projectA.pom.expectGet()

        // Looks for POM and JAR in repo1 before looking in repo2 (jar is an attempt to handle publication without module descriptor)
        missingProjectB.pom.expectGetMissing()
        missingProjectB.artifact.expectHeadMissing()
        projectB.pom.expectGet()

        projectA.artifact.expectGet()
        projectB.artifact.expectGet()

        then:
        succeeds 'listJars'

        when:
        server.resetExpectations()
        // No server requests when all jars cached

        then:
        succeeds 'listJars'
    }

    def "uses artifactsUrl to resolve artifacts"() {
        given:
        def repo1 = mavenHttpRepo("repo1")
        def repo2 = mavenHttpRepo("repo2")

        buildFile << """
repositories {
    maven {
        url '${repo1.uri}'
        artifactUrls '${repo2.uri}'
    }
}
configurations { compile }
dependencies {
    compile 'group:projectA:1.0', 'group:projectB:1.0'
}
task listJars << {
    assert configurations.compile.collect { it.name } == ['projectA-1.0.jar', 'projectB-1.0.jar']
}
"""

        def projectA = repo1.module('group', 'projectA').publish()
        def projectB = repo1.module('group', 'projectB').publish()
        def projectBArtifacts = repo2.module('group', 'projectB').publish()

        when:
        projectA.pom.expectGet()
        projectB.pom.expectGet()

        projectA.artifact.expectGet()
        projectB.artifact.expectGetMissing()
        projectBArtifacts.artifact.expectGet()

        then:
        succeeds 'listJars'
    }

    def "can resolve and cache dependencies from HTTP Maven repository with invalid settings.xml"() {
        given:
        def projectB = mavenHttpRepo.module('group', 'projectB', '1.0').publish()
        def projectA = mavenHttpRepo.module('group', 'projectA').dependsOn('group', 'projectB', '1.0').publish()

        buildFile << """
    repositories {
        maven { url '${mavenHttpRepo.uri}' }
    }
    configurations {
        compile {
            resolutionStrategy.cacheChangingModulesFor(0, "seconds")
        }
    }
    dependencies {
        compile 'group:projectA:1.0'
    }

    task retrieve(type: Sync) {
        into 'libs'
        from configurations.compile
    }
    """

        def m2Home = file("M2_REPO")
        def settingsFile = m2Home.file("conf/settings.xml")
        settingsFile << "invalid content... blabla"

        when:
        projectA.pom.expectGet()
        projectA.artifact.expectGet()
        projectB.pom.expectGet()
        projectB.artifact.expectGet()

        and:
        executer.withEnvironmentVars(M2_HOME: m2Home.absolutePath)
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('projectA-1.0.jar', 'projectB-1.0.jar')
        def snapshot = file('libs/projectA-1.0.jar').snapshot()

        when:
        server.resetExpectations()
        and:
        run 'retrieve'

        then:
        file('libs/projectA-1.0.jar').assertHasNotChangedSince(snapshot)
    }
}