/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.groovy

import org.gradle.integtests.fixtures.MultiVersionIntegrationSpec
import org.gradle.integtests.fixtures.TargetVersions
import spock.lang.Issue

@TargetVersions(['1.6.9', '1.7.11', '1.8.8', '2.0.5', '2.2.2', '2.3.6', '2.4.0-beta-2'])
class GroovyDocIntegrationTest extends MultiVersionIntegrationSpec {

    @Issue("https://issues.gradle.org//browse/GRADLE-3116")
    def "can run groovydoc"() {
        when:
        buildFile << """
            apply plugin: "groovy"

            repositories {
                mavenCentral()
            }

            dependencies {
                compile "org.codehaus.groovy:${module}:${version}"
            }
        """

        file("src/main/groovy/pkg/Thing.groovy") << """
            package pkg

            class Thing {}
        """

        then:
        succeeds "groovydoc"

        and:
        file('build/docs/groovydoc/index.html').file

        where:
        module << ['groovy', 'groovy-all']
    }

}
