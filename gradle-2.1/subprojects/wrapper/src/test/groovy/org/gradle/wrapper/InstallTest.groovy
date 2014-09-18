/*
 * Copyright 2007 the original author or authors.
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

package org.gradle.wrapper

import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Before
import org.junit.Rule
import spock.lang.Specification

class InstallTest extends Specification {
    File testDir
    Install install
    boolean downloadCalled
    File zip
    TestFile distributionDir
    File zipStore
    TestFile gradleHomeDir
    TestFile zipDestination
    WrapperConfiguration configuration = new WrapperConfiguration()
    IDownload download = Mock()
    PathAssembler pathAssembler = Mock()
    PathAssembler.LocalDistribution localDistribution = Mock()
    @Rule
    public TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider();

    @Before public void setup() {
        downloadCalled = false
        testDir = tmpDir.testDirectory
        configuration.zipBase = PathAssembler.PROJECT_STRING
        configuration.zipPath = 'someZipPath'
        configuration.distributionBase = PathAssembler.GRADLE_USER_HOME_STRING
        configuration.distributionPath = 'someDistPath'
        configuration.distribution = new URI('http://server/gradle-0.9.zip')
        distributionDir = new TestFile(testDir, 'someDistPath')
        gradleHomeDir = new TestFile(distributionDir, 'gradle-0.9')
        zipStore = new File(testDir, 'zips');
        zipDestination = new TestFile(zipStore, 'gradle-0.9.zip')
        install = new Install(download, pathAssembler)
    }

    void createTestZip(File zipDestination) {
        TestFile explodedZipDir = tmpDir.createDir('explodedZip')
        TestFile gradleScript = explodedZipDir.file('gradle-0.9/bin/gradle')
        gradleScript.parentFile.createDir()
        gradleScript.write('something')
        explodedZipDir.zipTo(new TestFile(zipDestination))
    }

    def "installs distribution and reuses on subsequent access"() {
        given:
        _ * pathAssembler.getDistribution(configuration) >> localDistribution
        _ * localDistribution.distributionDir >> distributionDir
        _ * localDistribution.zipFile >> zipDestination

        when:
        def homeDir = install.createDist(configuration)

        then:
        homeDir == gradleHomeDir
        gradleHomeDir.assertIsDir()
        gradleHomeDir.file("bin/gradle").assertIsFile()
        zipDestination.assertIsFile()

        and:
        1 * download.download(configuration.distribution, _) >> { createTestZip(it[1]) }
        0 * download._

        when:
        homeDir = install.createDist(configuration)

        then:
        homeDir == gradleHomeDir

        and:
        0 * download._
    }

    def "recovers from download failure"() {
        def failure = new RuntimeException("broken")

        given:
        _ * pathAssembler.getDistribution(configuration) >> localDistribution
        _ * localDistribution.distributionDir >> distributionDir
        _ * localDistribution.zipFile >> zipDestination

        when:
        install.createDist(configuration)

        then:
        RuntimeException e = thrown()
        e == failure

        and:
        1 * download.download(configuration.distribution, _) >> {
            it[1].text = 'broken!'
            throw failure
        }
        0 * download._

        when:
        def homeDir = install.createDist(configuration)

        then:
        homeDir == gradleHomeDir

        and:
        1 * download.download(configuration.distribution, _) >> { createTestZip(it[1]) }
        0 * download._
    }
}
