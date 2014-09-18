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

package org.gradle.buildinit.plugins

import org.gradle.buildinit.plugins.fixtures.WrapperTestFixture
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.DefaultTestExecutionResult
import org.gradle.integtests.fixtures.TestResources
import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.maven.M2Installation
import org.gradle.test.fixtures.server.http.MavenHttpModule
import org.gradle.test.fixtures.server.http.MavenHttpRepository
import org.gradle.test.fixtures.server.http.PomHttpArtifact
import org.gradle.test.fixtures.server.http.HttpServer
import org.gradle.util.SetSystemProperties
import org.junit.Rule
import spock.lang.Issue

import static org.gradle.util.TextUtil.toPlatformLineSeparators

class MavenConversionIntegrationTest extends AbstractIntegrationSpec {

    @Rule
    public final TestResources resources = new TestResources(temporaryFolder)

    @Rule
    public final SetSystemProperties systemProperties = new SetSystemProperties()

    @Rule
    public final HttpServer server = new HttpServer()

    def setup() {
        withLocalM2Installation()
    }

    def "multiModule"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        run 'clean', 'build'

        then: //smoke test the build artifacts
        file("webinar-api/build/libs/webinar-api-1.0-SNAPSHOT.jar").exists()
        file("webinar-impl/build/libs/webinar-impl-1.0-SNAPSHOT.jar").exists()
        file("webinar-war/build/libs/webinar-war-1.0-SNAPSHOT.war").exists()

        new DefaultTestExecutionResult(file("webinar-impl")).assertTestClassesExecuted('webinar.WebinarTest')

        when:
        run 'projects'

        then:
        output.contains(toPlatformLineSeparators("""
Root project 'webinar-parent'
+--- Project ':webinar-api' - Webinar APIs
+--- Project ':webinar-impl' - Webinar implementation
\\--- Project ':webinar-war' - Webinar web application
"""))
    }

    def "multiModuleWithNestedParent"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        run 'clean', 'build'

        then: //smoke test the build artifacts
        file("webinar-api/build/libs/webinar-api-1.0-SNAPSHOT.jar").exists()
        file("webinar-impl/build/libs/webinar-impl-1.0-SNAPSHOT.jar").exists()
        file("webinar-war/build/libs/webinar-war-1.0-SNAPSHOT.war").exists()

        new DefaultTestExecutionResult(file("webinar-impl")).assertTestClassesExecuted('webinar.WebinarTest')
    }

    def "flatmultimodule"() {
        when:
        executer.inDirectory(file("webinar-parent"))
        run 'init'

        then:
        gradleFilesGenerated(file("webinar-parent"))

        when:
        executer.inDirectory(file("webinar-parent"))
        run 'clean', 'build'

        then: //smoke test the build artifacts
        file("webinar-api/build/libs/webinar-api-1.0-SNAPSHOT.jar").exists()
        file("webinar-impl/build/libs/webinar-impl-1.0-SNAPSHOT.jar").exists()
        file("webinar-war/build/libs/webinar-war-1.0-SNAPSHOT.war").exists()

        new DefaultTestExecutionResult(file("webinar-impl")).assertTestClassesExecuted('webinar.WebinarTest')

        when:
        executer.inDirectory(file("webinar-parent"))
        run 'projects'

        then:
        output.contains(toPlatformLineSeparators("""
Root project 'webinar-parent'
+--- Project ':webinar-api' - Webinar APIs
+--- Project ':webinar-impl' - Webinar implementation
\\--- Project ':webinar-war' - Webinar web application
"""))
    }

    def "singleModule"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        //TODO this build should fail because the TestNG test is failing
        //however the plugin does not generate testNG for single module project atm (bug)
        //def failure = runAndFail('clean', 'build')  //assert if fails for the right reason
        run 'clean', 'build'
        then:
        file("build/libs/util-2.5.jar").exists()
    }

    def "testjar"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        run 'clean', 'build'

        then:
        file("build/libs/testjar-2.5.jar").exists()
        file("build/libs/testjar-2.5-tests.jar").exists()
    }

    def "enforcerplugin"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        and:
        buildFile.text.contains("""configurations.all {
it.exclude group: 'org.apache.maven'
it.exclude group: 'org.apache.maven', module: 'badArtifact'
it.exclude group: '*', module: 'badArtifact'
}""")
        when:
        run 'clean', 'build'

        then:
        file("build/libs/enforcerExample-1.0.jar").exists()
    }

    def "providedNotWar"() {
        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        run 'clean', 'build'

        then:
        file("build/libs/myThing-0.0.1-SNAPSHOT.jar").exists()
    }

    def "provides decent error message when POM is invalid"() {
        setup:
        def pom = file("pom.xml")
        pom << "<project>someInvalid pom content</project>"

        when:
        fails 'init'

        then:
        failure.assertHasCause("Could not convert Maven POM $pom to a Gradle build.")
    }

    def "mavenExtensions"() {
        when:
        run 'init'
        then:
        gradleFilesGenerated()

        when:
        run 'clean', 'build'

        then:
        file("build/libs/testApp-1.0.jar").exists()
    }

    @Issue("GRADLE-2820")
    def "remoteparent"() {
        setup:
        withSharedResources()
        def repo = mavenHttpServer()
        //update pom with test repo url
        file("pom.xml").text = file("pom.xml").text.replaceAll('LOCAL_MAVEN_REPO_URL', repo.getUri().toString())

        expectParentPomRequest(repo)

        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        libRequest(repo, "commons-lang", "commons-lang", "2.6")
        run 'clean', 'build'

        then:
        file("build/libs/util-2.5.jar").exists()
    }

    @Issue("GRADLE-2872")
    def "expandProperties"() {
        setup:
        withSharedResources()
        executer.withArgument("-DCOMMONS_LANG_VERSION=2.6")

        when:
        run 'init'
        then:
        gradleFilesGenerated()

        when:
        run('clean', 'build')

        then:
        file("build/libs/util-3.2.1.jar").exists()
    }

    @Issue("GRADLE-2819")
    def "multiModuleWithRemoteParent"() {
        setup:
        withSharedResources()
        def repo = mavenHttpServer()
        //update pom with test repo url
        file("pom.xml").text = file("pom.xml").text.replaceAll('LOCAL_MAVEN_REPO_URL', repo.getUri().toString())

        expectParentPomRequest(repo)

        when:
        run 'init'

        then:
        gradleFilesGenerated()

        when:
        libRequest(repo, "commons-lang", "commons-lang", 2.6)
        libRequest(repo, "junit", "junit", 4.10)
        libRequest(repo, "org.hamcrest", "hamcrest-core", 1.1)

        run 'clean', 'build'

        then: //smoke test the build artifacts
        file("webinar-api/build/libs/webinar-api-1.0-SNAPSHOT.jar").exists()
        file("webinar-impl/build/libs/webinar-impl-1.0-SNAPSHOT.jar").exists()
        file("webinar-war/build/libs/webinar-war-1.0-SNAPSHOT.war").exists()

        new DefaultTestExecutionResult(file("webinar-impl")).assertTestClassesExecuted('webinar.WebinarTest')

        when:
        run 'projects'

        then:
        output.contains(toPlatformLineSeparators("""
Root project 'webinar-parent'
+--- Project ':util-parent'
+--- Project ':webinar-api' - Webinar APIs
+--- Project ':webinar-impl' - Webinar implementation
\\--- Project ':webinar-war' - Webinar web application
"""))
    }

    void gradleFilesGenerated(TestFile parentFolder = file(".")) {
        assert parentFolder.file("build.gradle").exists()
        assert parentFolder.file("settings.gradle").exists()
        new WrapperTestFixture(parentFolder).generated()
    }

    def libRequest(MavenHttpRepository repo, String group, String name, Object version) {
        MavenHttpModule module = repo.module(group, name, version)
        module.allowAll()
    }

    def expectModule(MavenHttpRepository repo, String group, String name, String version) {
        MavenHttpModule module1 = repo.module(group, name, version).publish()
        module1.allowAll()
    }

    def withSharedResources() {
        resources.maybeCopy('MavenConversionIntegrationTest/sharedResources')
    }

    M2Installation withLocalM2Installation() {
        M2Installation m2Installation = new M2Installation(testDirectory)
        m2Installation.generateUserSettingsFile(mavenLocal("local_m2"))
        using m2Installation
        m2Installation
    }

    PomHttpArtifact expectParentPomRequest(MavenHttpRepository repo) {
        MavenHttpModule module = repo.module('util.util.parent', 'util-parent', '3')
        module.pom.expectGet();
        module.pom.sha1.expectGet();
        module.pom.md5.expectGet();
        module.pom
    }

    MavenHttpRepository mavenHttpServer() {
        server.start()
        new MavenHttpRepository(server, '/maven', maven(file("maven_repo")));
    }
}