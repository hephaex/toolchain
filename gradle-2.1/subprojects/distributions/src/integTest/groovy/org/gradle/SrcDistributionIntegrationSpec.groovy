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

package org.gradle

import org.apache.tools.ant.taskdefs.Expand
import org.gradle.test.fixtures.file.TestFile
import org.gradle.util.AntUtil
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition

class SrcDistributionIntegrationSpec extends DistributionIntegrationSpec {

    @Override
    String getDistributionLabel() {
        "src"
    }

    @Requires(TestPrecondition.NOT_WINDOWS)
    def sourceZipContents() {
        given:
        TestFile contentsDir = unpackDistribution()

        expect:
        !contentsDir.file(".git").exists()

        when:
        executer.with {
            withDeprecationChecksDisabled()
            inDirectory(contentsDir)
            usingExecutable('gradlew')
            withTasks('binZip')
        }.run()

        then:
        File binZip = contentsDir.file("build/distributions").listFiles().find() { it.name.endsWith("-bin.zip") }
        binZip.exists()

        when:
        Expand unpack = new Expand()
        unpack.src = binZip
        unpack.dest = contentsDir.file('build/distributions/unzip')
        AntUtil.execute(unpack)

        then:
        TestFile unpackedRoot = new TestFile(contentsDir.file('build/distributions/unzip').listFiles().first())
        unpackedRoot.file("bin/gradle").exists()
    }

}
