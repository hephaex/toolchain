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
import org.gradle.test.fixtures.server.http.MavenHttpModule
import spock.lang.Ignore
import spock.lang.Issue

class MavenSnapshotResolveIntegrationTest extends AbstractHttpDependencyResolutionTest {

    def "can find and cache snapshots in multiple Maven HTTP repositories"() {
        def repo1 = mavenHttpRepo("repo1")
        def repo2 = mavenHttpRepo("repo2")

        given:
        buildFile << """
repositories {
    maven { url "${repo1.uri}" }
    maven { url "${repo2.uri}" }
}

configurations { compile }

dependencies {
    compile "org.gradle.integtests.resolve:projectA:1.0-SNAPSHOT"
    compile "org.gradle.integtests.resolve:projectB:1.0-SNAPSHOT"
    compile "org.gradle.integtests.resolve:nonunique:1.0-SNAPSHOT"
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        and: "snapshot modules are published"
        def repo1ProjectA = repo1.module("org.gradle.integtests.resolve", "projectA", "1.0-SNAPSHOT").publish()
        def repo1ProjectB = repo1.module("org.gradle.integtests.resolve", "projectB", "1.0-SNAPSHOT")
        def repo2ProjectB = repo2.module("org.gradle.integtests.resolve", "projectB", "1.0-SNAPSHOT").publish()
        def repo1NonUnique = repo1.module("org.gradle.integtests.resolve", "nonunique", "1.0-SNAPSHOT").withNonUniqueSnapshots()
        def repo2NonUnique = repo2.module("org.gradle.integtests.resolve", "nonunique", "1.0-SNAPSHOT").withNonUniqueSnapshots().publish()

        when: "Server provides projectA from repo1"
        expectModuleServed(repo1ProjectA)

        and: "Server provides projectB from repo2"
        expectModuleMissing(repo1ProjectB)
        expectModuleServed(repo2ProjectB)

        and: "Server provides nonunique snapshot from repo2"
        expectModuleMissing(repo1NonUnique)
        expectModuleServed(repo2NonUnique)

        and: "We resolve dependencies"
        run 'retrieve'

        then: "Snapshots are downloaded"
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-1.0-SNAPSHOT.jar', 'nonunique-1.0-SNAPSHOT.jar')
        def snapshotA = file('libs/projectA-1.0-SNAPSHOT.jar').snapshot()
        def snapshotNonUnique = file('libs/nonunique-1.0-SNAPSHOT.jar').snapshot()

        when: "We resolve with snapshots cached: no server requests"
        server.resetExpectations()
        def result = run('retrieve')

        then: "Everything is up to date"
        result.assertTaskSkipped(':retrieve')
        file('libs/projectA-1.0-SNAPSHOT.jar').assertHasNotChangedSince(snapshotA);
        file('libs/nonunique-1.0-SNAPSHOT.jar').assertHasNotChangedSince(snapshotNonUnique);
    }

    def "can find and cache snapshots in Maven HTTP repository with additional artifact urls"() {
        def repo1 = mavenHttpRepo("repo1")
        def repo2 = mavenHttpRepo("repo2")

        given:
        buildFile << """
repositories {
    maven {
        url "${repo1.uri}"
        artifactUrls "${repo2.uri}"
    }
}

configurations { compile }

dependencies {
    compile "org.gradle.integtests.resolve:projectA:1.0-SNAPSHOT"
    compile "org.gradle.integtests.resolve:projectB:1.0-SNAPSHOT"
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        and: "snapshot modules are published"
        def projectA = repo1.module("org.gradle.integtests.resolve", "projectA", "1.0-SNAPSHOT").publish()
        def repo1ProjectB = repo1.module("org.gradle.integtests.resolve", "projectB", "1.0-SNAPSHOT").publish()
        def repo2ProjectB = repo2.module("org.gradle.integtests.resolve", "projectB", "1.0-SNAPSHOT").publish()

        when: "Server provides projectA from repo1"
        expectModuleServed(projectA)

        and: "Server provides projectB with artifact in repo2"
        repo1ProjectB.metaData.expectGet()
        repo1ProjectB.pom.expectGet()
        repo1ProjectB.artifact.expectGetMissing()
        repo2ProjectB.artifact.expectGet()

        and: "We resolve dependencies"
        run 'retrieve'

        then: "Snapshots are downloaded"
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-1.0-SNAPSHOT.jar')
        def snapshotA = file('libs/projectA-1.0-SNAPSHOT.jar').snapshot()
        def snapshotB = file('libs/projectB-1.0-SNAPSHOT.jar').snapshot()

        when: "We resolve with snapshots cached: no server requests"
        server.resetExpectations()
        def result = run('retrieve')

        then: "Everything is up to date"
        result.assertTaskSkipped(':retrieve')
        file('libs/projectA-1.0-SNAPSHOT.jar').assertHasNotChangedSince(snapshotA);
        file('libs/projectB-1.0-SNAPSHOT.jar').assertHasNotChangedSince(snapshotB);
    }

    def "can find and cache snapshots in Maven HTTP repository with artifact classifier"() {
        def repo1 = mavenHttpRepo("repo1")

        given:
        buildFile << """
repositories {
    maven {
        url "${repo1.uri}"
    }
}

configurations { compile }

dependencies {
    compile "org.gradle.integtests.resolve:projectA:1.0-SNAPSHOT:tests"
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        and:
        def projectA = repo1.module("org.gradle.integtests.resolve", "projectA", "1.0-SNAPSHOT")
        def classifierArtifact = projectA.artifact(classifier: "tests")
        projectA.publish()

        when:
        projectA.metaData.expectGet()
        projectA.pom.expectGet()
        classifierArtifact.expectGet()

        and:
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT-tests.jar')
        def snapshotA = file('libs/projectA-1.0-SNAPSHOT-tests.jar').snapshot()

        when:
        server.resetExpectations()
        run 'retrieve'

        then: "Everything is up to date"
        skipped ':retrieve'
        file('libs/projectA-1.0-SNAPSHOT-tests.jar').assertHasNotChangedSince(snapshotA);
    }

    def "will detect changed snapshot artifacts when pom has not changed"() {
        buildFile << """
repositories {
    maven { url "${mavenHttpRepo.uri}" }
}

configurations { compile }
configurations.compile.resolutionStrategy.cacheChangingModulesFor 0, 'seconds'

dependencies { 
    compile "org.gradle.integtests.resolve:unique:1.0-SNAPSHOT"
    compile "org.gradle.integtests.resolve:nonunique:1.0-SNAPSHOT"
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when: "snapshot modules are published"
        def uniqueVersionModule = mavenHttpRepo.module("org.gradle.integtests.resolve", "unique", "1.0-SNAPSHOT").publish()
        def nonUniqueVersionModule = mavenHttpRepo.module("org.gradle.integtests.resolve", "nonunique", "1.0-SNAPSHOT").withNonUniqueSnapshots().publish()

        and: "Server handles requests"
        expectModuleServed(uniqueVersionModule)
        expectModuleServed(nonUniqueVersionModule)

        and: "We resolve dependencies"
        run 'retrieve'

        then: "Snapshots are downloaded"
        file('libs').assertHasDescendants('unique-1.0-SNAPSHOT.jar', 'nonunique-1.0-SNAPSHOT.jar')
        def uniqueJarSnapshot = file('libs/unique-1.0-SNAPSHOT.jar').assertIsCopyOf(uniqueVersionModule.artifactFile).snapshot()
        def nonUniqueJarSnapshot = file('libs/nonunique-1.0-SNAPSHOT.jar').assertIsCopyOf(nonUniqueVersionModule.artifactFile).snapshot()
        server.resetExpectations()

        when: "Change the snapshot artifacts directly: do not change the pom"
        uniqueVersionModule.artifactFile << 'more content'
        uniqueVersionModule.backingModule.sha1File(uniqueVersionModule.artifactFile)
        nonUniqueVersionModule.artifactFile << 'more content'
        nonUniqueVersionModule.backingModule.sha1File(nonUniqueVersionModule.artifactFile)

        and: "No server requests"
        expectChangedArtifactServed(uniqueVersionModule)
        expectChangedArtifactServed(nonUniqueVersionModule)

        and: "Resolve dependencies again"
        run 'retrieve'

        then:
        file('libs/unique-1.0-SNAPSHOT.jar').assertIsCopyOf(uniqueVersionModule.artifactFile).assertHasChangedSince(uniqueJarSnapshot)
        file('libs/nonunique-1.0-SNAPSHOT.jar').assertIsCopyOf(nonUniqueVersionModule.artifactFile).assertHasChangedSince(nonUniqueJarSnapshot)
    }

    def "uses cached snapshots from a Maven HTTP repository until the snapshot timeout is reached"() {
        given:
        buildFile << """
repositories {
    maven { url "${mavenHttpRepo.uri}" }
}

configurations { compile }

if (project.hasProperty('noTimeout')) {
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
}

dependencies {
    compile "org.gradle.integtests.resolve:unique:1.0-SNAPSHOT"
    compile "org.gradle.integtests.resolve:nonunique:1.0-SNAPSHOT"
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when: "snapshot modules are published"
        def uniqueVersionModule = mavenHttpRepo.module("org.gradle.integtests.resolve", "unique", "1.0-SNAPSHOT").publish()
        def nonUniqueVersionModule = mavenHttpRepo.module("org.gradle.integtests.resolve", "nonunique", "1.0-SNAPSHOT").withNonUniqueSnapshots().publish()

        and: "Server handles requests"
        expectModuleServed(uniqueVersionModule)
        expectModuleServed(nonUniqueVersionModule)

        and: "We resolve dependencies"
        run 'retrieve'

        then: "Snapshots are downloaded"
        file('libs').assertHasDescendants('unique-1.0-SNAPSHOT.jar', 'nonunique-1.0-SNAPSHOT.jar')
        def uniqueJarSnapshot = file('libs/unique-1.0-SNAPSHOT.jar').assertIsCopyOf(uniqueVersionModule.artifactFile).snapshot()
        def nonUniqueJarSnapshot = file('libs/nonunique-1.0-SNAPSHOT.jar').assertIsCopyOf(nonUniqueVersionModule.artifactFile).snapshot()

        when: "Republish the snapshots"
        uniqueVersionModule.publishWithChangedContent()
        nonUniqueVersionModule.publishWithChangedContent()

        and: "No server requests"
        server.resetExpectations()

        and: "Resolve dependencies again, with cached versions"
        run 'retrieve'

        then:
        file('libs/unique-1.0-SNAPSHOT.jar').assertHasNotChangedSince(uniqueJarSnapshot)
        file('libs/nonunique-1.0-SNAPSHOT.jar').assertHasNotChangedSince(nonUniqueJarSnapshot)

        when: "Server handles requests"
        expectChangedModuleServed(uniqueVersionModule)
        expectChangedModuleServed(nonUniqueVersionModule)

        and: "Resolve dependencies with cache expired"
        executer.withArguments("-PnoTimeout")
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('unique-1.0-SNAPSHOT.jar', 'nonunique-1.0-SNAPSHOT.jar')
        file('libs/unique-1.0-SNAPSHOT.jar').assertIsCopyOf(uniqueVersionModule.artifactFile).assertHasChangedSince(uniqueJarSnapshot)
        file('libs/nonunique-1.0-SNAPSHOT.jar').assertIsCopyOf(nonUniqueVersionModule.artifactFile).assertHasChangedSince(nonUniqueJarSnapshot);
    }

    def "does not download snapshot artifacts after expiry when snapshot has not changed"() {
        buildFile << """
repositories {
    maven { url "${mavenHttpRepo.uri}" }
}

configurations { compile }

configurations.all {
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}

dependencies {
    compile "org.gradle.integtests.resolve:testproject:1.0-SNAPSHOT"
}

task retrieve(type: Sync) {
    into 'build'
    from configurations.compile
}
"""

        when: "Publish the first snapshot"
        def module = mavenHttpRepo.module("org.gradle.integtests.resolve", "testproject", "1.0-SNAPSHOT").publish()

        and: "Server handles requests"
        expectModuleServed(module)

        and:
        run 'retrieve'

        then:
        file('build').assertHasDescendants('testproject-1.0-SNAPSHOT.jar')
        def snapshot = file('build/testproject-1.0-SNAPSHOT.jar').assertIsCopyOf(module.artifactFile).snapshot()

        when: "Server handles requests"
        server.resetExpectations()
        expectChangedProbe(module)

        // Retrieve again with zero timeout should check for updated snapshot
        and:
        def result = run 'retrieve'

        then:
        result.assertTaskSkipped(':retrieve')
        file('build/testproject-1.0-SNAPSHOT.jar').assertHasNotChangedSince(snapshot);
    }

    def "does not download snapshot artifacts more than once per build"() {
        given:
        def module = mavenHttpRepo.module("org.gradle.integtests.resolve", "testproject", "1.0-SNAPSHOT").publish()

        and:
        settingsFile << "include 'a', 'b'"
        buildFile << """
allprojects {
    repositories {
        maven { url "${mavenHttpRepo.uri}" }
    }

    configurations { compile }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }

    dependencies {
        compile "org.gradle.integtests.resolve:testproject:1.0-SNAPSHOT"
    }

    task retrieve(type: Sync) {
        into 'build'
        from configurations.compile
    }
}

//imposing an artificial order so that the parallel build retrieves sequentially, GRADLE-2788
retrieve.dependsOn ":a:retrieve"
tasks.getByPath(":a:retrieve").dependsOn ":b:retrieve"
"""
        when: "Module is requested once"
        expectModuleServed(module)

        then:
        run 'retrieve'

        and:
        file('build').assertHasDescendants('testproject-1.0-SNAPSHOT.jar')
        file('a/build').assertHasDescendants('testproject-1.0-SNAPSHOT.jar')
        file('b/build').assertHasDescendants('testproject-1.0-SNAPSHOT.jar')
    }

    @Ignore //TODO SF need to rework this test. First step might be turning off in-memory metadata caching for this test.
    def "can update snapshot artifact during build even if it is locked earlier in build"() {
        given:
        def module = mavenHttpRepo("/repo", maven("repo1")).module("org.gradle.integtests.resolve", "testproject", "1.0-SNAPSHOT").withNonUniqueSnapshots().publish()
        def module2 = mavenHttpRepo("/repo", maven("repo2")).module("org.gradle.integtests.resolve", "testproject", "1.0-SNAPSHOT").withNonUniqueSnapshots().publish()
        module2.pomFile << '    ' // ensure it's a different length to the first one
        module2.backingModule.sha1File(module2.pomFile)
        module2.artifactFile << module2.artifactFile.bytes // ensure it's a different length to the first one
        module2.backingModule.sha1File(module2.artifactFile)
        and:
        settingsFile << "include 'first', 'second'"
        buildFile << """
def fileLocks = [:]
subprojects {
    repositories {
        maven { url "http://localhost:${server.port}/repo" }
    }

    configurations { compile }

    configurations.all {
        resolutionStrategy.resolutionRules.eachArtifact({ artifact ->
            artifact.refresh()
        } as Action)
    }

    dependencies {
        compile "org.gradle.integtests.resolve:testproject:1.0-SNAPSHOT"
    }

    task lock << {
        configurations.compile.each { file ->
            println "locking " + file
            def lockFile = new RandomAccessFile(file.canonicalPath, 'r')
            fileLocks[file] = lockFile
        }
    }

    task retrieve(type: Sync) {
        into 'build'
        from configurations.compile
    }
    retrieve.dependsOn 'lock'
}
project('second') {
    lock.dependsOn ':first:lock'
    retrieve.dependsOn ':first:retrieve'

    task cleanup << {
        fileLocks.each { key, value ->
            println "unlocking " + key
            value.close()
        }
    }
    cleanup.dependsOn 'retrieve'
}
"""
        when: "Module is requested once"
        module.metaData.expectGet()
        module.pom.expectGet()
        module.artifact.expectGet()

        module2.artifact.expectHead()
        module2.artifact.sha1.expectGet()
        module2.artifact.expectGet()

        then:
        run 'cleanup'

        and:
        file('first/build/testproject-1.0-SNAPSHOT.jar').assertIsCopyOf(module.artifactFile)
        file('second/build/testproject-1.0-SNAPSHOT.jar').assertIsCopyOf(module2.artifactFile)
    }

    def "avoid redownload unchanged artifact when no checksum available"() {
        given:
        buildFile << """
            repositories {
                maven { url "${mavenHttpRepo.uri}" }
            }

            configurations { compile }

            configurations.all {
                resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
            }

            dependencies {
                compile group: "group", name: "projectA", version: "1.1-SNAPSHOT"
            }

            task retrieve(type: Copy) {
                into 'build'
                from configurations.compile
            }
        """

        and:
        def module = mavenHttpRepo.module("group", "projectA", "1.1-SNAPSHOT").withNonUniqueSnapshots().publish()
        // Set the last modified to something that's not going to be anything “else”.
        // There are lots of dates floating around in a resolution and we want to make
        // sure we use this.
        module.artifactFile.setLastModified(2000)
        module.pom.file.setLastModified(6000)
        def artifact = module.artifact

        when:
        expectModuleServed(module)

        run "retrieve"

        then:
        def downloadedJarFile = file("build/projectA-1.1-SNAPSHOT.jar")
        downloadedJarFile.assertIsCopyOf(module.artifactFile)
        def initialDownloadJarFileSnapshot = downloadedJarFile.snapshot()

        when:
        server.resetExpectations()
        expectChangedProbe(module)

        run "retrieve"

        then:
        downloadedJarFile.assertHasNotChangedSince(initialDownloadJarFileSnapshot)

        when:
        module.publishWithChangedContent()
        server.resetExpectations()
        module.metaData.expectGet()
        module.pom.expectHead()
        module.pom.sha1.expectGetMissing()
        module.pom.expectGet()
        artifact.expectHead()
        artifact.sha1.expectGetMissing()
        artifact.expectGet()

        run "retrieve"

        then:
        downloadedJarFile.assertHasChangedSince(initialDownloadJarFileSnapshot)
        downloadedJarFile.assertIsCopyOf(module.artifactFile)
    }

    @Issue("GRADLE-3017")
    def "resolves changed metadata in snapshot dependency"() {
        given:
        def projectB1 = mavenHttpRepo.module('group', 'projectB', '1.0').publish()
        def projectB2 = mavenHttpRepo.module('group', 'projectB', '2.0').publish()
        def projectA = mavenHttpRepo.module('group', 'projectA', "1.0-SNAPSHOT").dependsOn('group', 'projectB', '1.0').publish()

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile {
        if (project.hasProperty('bypassCache')) {
            resolutionStrategy.cacheChangingModulesFor(0, "seconds")
        }
    }
}
dependencies {
    compile 'group:projectA:1.0-SNAPSHOT'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        projectA.pom.expectGet()
        projectA.metaData.expectGet()
        projectA.artifact.expectGet()
        projectB1.pom.expectGet()
        projectB1.artifact.expectGet()

        and:
        run 'retrieve'

        then:
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-1.0.jar')

        when: "Project A is published with changed dependencies"
        server.resetExpectations()
        projectA = projectA.dependsOn('group', 'projectB', '2.0').publish()

        and: "Resolve with caching"
        run 'retrieve'

        then: "Gets original ProjectA metadata from cache"
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-1.0.jar')

        when: "Resolve without cache"
        projectA.metaData.expectGet()
        projectA.pom.expectHead()
        projectA.pom.sha1.expectGet()
        projectA.pom.expectGet()
        projectA.artifact.expectHead()
        projectB2.pom.expectGet()
        projectB2.artifact.expectGet()

        and:
        executer.withArguments("-PbypassCache")
        run 'retrieve'

        then: "Gets updated metadata"
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-2.0.jar')

        when: "Resolve with caching"
        server.resetExpectations()
        run 'retrieve'

        then: "Gets updated metadata from cache"
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar', 'projectB-2.0.jar')
    }

    def "reports and recovers from missing snapshot"() {
        given:
        def projectA = mavenHttpRepo.module('group', 'projectA', "1.0-SNAPSHOT")

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile
}
dependencies {
    compile 'group:projectA:1.0-SNAPSHOT'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        projectA.metaData.expectGetMissing()
        projectA.pom.expectGetMissing()
        projectA.artifact.expectHeadMissing()

        then:
        fails 'retrieve'

        and:
        failure.assertHasCause("""Could not find group:projectA:1.0-SNAPSHOT.
Searched in the following locations:
    ${projectA.metaData.uri}
    ${projectA.pom.uri}
    ${projectA.artifact.uri}
""")

        when:
        server.resetExpectations()
        projectA.publish()
        projectA.metaData.expectGet()
        projectA.pom.expectGet()
        projectA.artifact.expectGet()

        then:
        succeeds 'retrieve'
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar')
    }

    def "reports missing snapshot artifact"() {
        given:
        def projectA = mavenHttpRepo.module('group', 'projectA', "1.0-SNAPSHOT").publish()

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile
}
dependencies {
    compile 'group:projectA:1.0-SNAPSHOT'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        projectA.metaData.expectGet()
        projectA.pom.expectGet()
        projectA.artifact.expectGetMissing()

        then:
        fails 'retrieve'

        and:
        failure.assertHasCause("""Artifact 'group:projectA:1.0-SNAPSHOT:projectA.jar' not found.
Searched in the following locations:
    ${projectA.artifact.uri}""")

        when:
        server.resetExpectations()

        then:
        fails 'retrieve'

        and:
        failure.assertHasCause("Artifact 'group:projectA:1.0-SNAPSHOT:projectA.jar' not found.")
    }

    def "reports and recovers from broken maven-metadata.xml"() {
        given:
        def projectA = mavenHttpRepo.module('group', 'projectA', "1.0-SNAPSHOT").publish()

        buildFile << """
repositories {
    maven { url '${mavenHttpRepo.uri}' }
}
configurations {
    compile
}
dependencies {
    compile 'group:projectA:1.0-SNAPSHOT'
}

task retrieve(type: Sync) {
    into 'libs'
    from configurations.compile
}
"""

        when:
        def metaData = projectA.metaData
        metaData.expectGetBroken()

        then:
        fails 'retrieve'

        and:
        failure.assertHasCause('Could not resolve group:projectA:1.0-SNAPSHOT.')
        failure.assertHasCause("Unable to load Maven meta-data from ${metaData.uri}.")
        failure.assertHasCause("Could not GET '${metaData.uri}'. Received status code 500 from server")

        when:
        server.resetExpectations()
        metaData.expectGet()
        projectA.pom.expectGet()
        projectA.artifact.expectGet()

        then:
        succeeds 'retrieve'
        file('libs').assertHasDescendants('projectA-1.0-SNAPSHOT.jar')
    }

    private expectModuleServed(MavenHttpModule module) {
        module.metaData.expectGet()
        module.pom.expectGet()
        module.artifact.expectGet()
    }

    private expectChangedModuleServed(MavenHttpModule module) {
        module.metaData.expectGet()
        module.pom.expectHead()
        module.pom.sha1.expectGet()
        module.pom.expectGet()
        module.artifact.expectHead()
        module.artifact.sha1.expectGet()
        module.artifact.expectGet()
    }

    private expectChangedArtifactServed(MavenHttpModule module) {
        module.metaData.expectGet()
        module.pom.expectHead()
        def artifact = module.artifact
        artifact.expectHead()
        artifact.sha1.expectGet()
        artifact.expectGet()
    }

    private expectChangedProbe(MavenHttpModule module) {
        module.metaData.expectGet()
        module.pom.expectHead()
        module.artifact.expectHead()
    }

    private expectModuleMissing(MavenHttpModule module) {
        module.metaData.expectGetMissing()
        module.pom.expectGetMissing()
        module.artifact.expectHeadMissing()
    }
}
