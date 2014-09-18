/*
 * Copyright 2007-2008 the original author or authors.
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

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.Sample
import org.gradle.test.fixtures.file.TestFile
import org.junit.Rule

class SamplesWebProjectIntegrationTest extends AbstractIntegrationSpec {
    static final String WEB_PROJECT_NAME = 'customized'

    @Rule public final Sample sample = new Sample(temporaryFolder, 'webApplication/customized')

    def "can build war"() {
        when:
        sample sample
        run 'clean', 'assemble'
        
        then:
        TestFile tmpDir = file('unjar')
        sample.dir.file("build/libs/customized-1.0.war").unzipTo(tmpDir)
        tmpDir.assertHasDescendants(
                'root.txt',
                'META-INF/MANIFEST.MF',
                'WEB-INF/classes/org/gradle/HelloServlet.class',
                'WEB-INF/classes/org/gradle/MyClass.class',
                'WEB-INF/lib/compile-1.0.jar',
                'WEB-INF/lib/compile-transitive-1.0.jar',
                'WEB-INF/lib/runtime-1.0.jar',
                'WEB-INF/lib/additional-1.0.jar',
                'WEB-INF/lib/otherLib-1.0.jar',
                'WEB-INF/additional.xml',
                'WEB-INF/webapp.xml',
                'WEB-INF/web.xml',
                'webapp.html')
    }

    def "can execute servlet"() {
        given:
        // Inject some int test stuff
        sample.dir.file('build.gradle') << """
def portFinder = org.gradle.util.AvailablePortFinder.createPrivate()

httpPort = portFinder.nextAvailable
stopPort = portFinder.nextAvailable
println "http port = \$httpPort, stop port = \$stopPort"

[jettyRun, jettyRunWar]*.daemon = true

task runTest(dependsOn: jettyRun) << {
    callServlet()
}

task runWarTest(dependsOn: jettyRunWar) << {
    callServlet()
}

private void callServlet() {
    URL url = new URL("http://localhost:\$httpPort/customized/hello")
    println url.text
    jettyStop.execute()
}

"""

        when:
        sample sample
        run 'runTest'

        then:
        output.contains('Hello Gradle')

        when:
        sample sample
        run 'runWarTest'

        then:
        output.contains('Hello Gradle')
    }
}
