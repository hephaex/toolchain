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

package org.gradle.integtests.resolve.maven

import org.gradle.integtests.resolve.ComponentMetadataRulesChangingModulesIntegrationTest
import org.gradle.test.fixtures.server.http.MavenHttpRepository

class MavenComponentMetadataRulesChangingModulesIntegrationTest extends ComponentMetadataRulesChangingModulesIntegrationTest {
    MavenHttpRepository getRepo() {
        mavenHttpRepo
    }

    String getRepoDeclaration() {
"""
repositories {
    maven {
        url "$repo.uri"
    }
}
"""
    }

    def setup() {
        moduleA.rootMetaData.allowGetOrHead()
    }

    def "snapshot dependencies have changing flag initialized to true"() {
        def moduleB = repo.module("org.test", "moduleB", "1.0-SNAPSHOT").publish()
        moduleB.allowAll()

        buildFile <<
"""
$repoDeclaration
configurations {
    modules
}
dependencies {
    modules "org.test:moduleB:1.0-SNAPSHOT"
    components {
        eachComponent { details ->
            file(details.id.name).text = details.changing
        }
    }
}
task resolve << {
    configurations.modules.files
}
"""

        when:
        run("resolve")

        then:
        file("moduleB").text == "true"
    }
}