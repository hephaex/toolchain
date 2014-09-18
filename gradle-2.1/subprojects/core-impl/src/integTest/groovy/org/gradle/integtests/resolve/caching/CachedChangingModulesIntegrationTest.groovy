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

package org.gradle.integtests.resolve.caching

import org.gradle.integtests.fixtures.AbstractHttpDependencyResolutionTest

public class CachedChangingModulesIntegrationTest extends AbstractHttpDependencyResolutionTest {

    def "can cache and refresh unique versioned maven artifacts with a classifier"() {
        given:
        def repo = mavenHttpRepo("repo")
        def module = repo.module("group", "projectA", "1.0-SNAPSHOT")
        def sourceArtifact = module.artifact(classifier: "source")

        module.publish()
        buildFile << """
        repositories {
            maven {
                name 'repo'
                url '${repo.uri}'
            }
        }
        configurations {
            compile
        }

        configurations.all {
            resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
        }

        dependencies {
            compile 'group:projectA:1.0-SNAPSHOT:source'
        }

        task retrieve(type: Sync) {
            into 'libs'
            from configurations.compile
        }
        """

        when:
        module.pom.expectGet()
        sourceArtifact.expectGet()
        module.metaData.expectGet()

        then:
        run 'retrieve'

        when:
        server.resetExpectations()
        module.metaData.expectGet()
        sourceArtifact.expectHead()
        module.pom.expectHead()
        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()

        module.metaData.expectGet()
        module.pom.sha1.expectGet()
        module.pom.expectHead()
        module.pom.expectGet()
        sourceArtifact.expectHead()
        sourceArtifact.expectGet()
        sourceArtifact.sha1.expectGet()
        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()
        then:
        executer.withArgument("--offline")
        run 'retrieve'
    }

    def "can cache and refresh non unique versioned maven artifacts with a classifier"() {
        given:
        def repo = mavenHttpRepo("repo")
        def module = repo.module("group", "projectA", "1.0-SNAPSHOT").withNonUniqueSnapshots()
        def sourceArtifact = module.artifact(classifier: "source")

        module.publish()
        buildFile << """
        repositories {
            maven {
                name 'repo'
                url '${repo.uri}'
            }
        }
        configurations {
            compile
        }

        configurations.all {
            resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
        }

        dependencies {
            compile 'group:projectA:1.0-SNAPSHOT:source'
        }

        task retrieve(type: Sync) {
            into 'libs'
            from configurations.compile
        }
        """

        when:
        module.pom.expectGet()
        sourceArtifact.expectGet()
        module.metaData.expectGetMissing()

        then:
        run 'retrieve'

        when:
        server.resetExpectations()
        module.metaData.expectGetMissing()
        sourceArtifact.expectHead()
        module.pom.expectHead()
        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()

        module.metaData.expectGetMissing()
        module.pom.sha1.expectGet()
        module.pom.expectHead()
        module.pom.expectGet()
        sourceArtifact.expectHead()
        sourceArtifact.expectGet()
        sourceArtifact.sha1.expectGet()
        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()
        then:
        executer.withArgument("--offline")
        run 'retrieve'
    }

    def "can cache and refresh ivy changing artifacts with a classifier"() {
        given:
        def repo = ivyHttpRepo("repo")
        def module = repo.module("group", "projectA", "1.0")
        module.artifact(classifier: "source")

        module.publish()
        buildFile << """
          repositories {
              ivy {
                  name 'repo'
                  url '${repo.uri}'
              }
          }
          configurations {
              compile
          }

          configurations.all {
              resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
          }

          dependencies {
                compile group: "group", name: "projectA", version: "1.0", classifier: "source", changing: true
          }

          task retrieve(type: Sync) {
              into 'libs'
              from configurations.compile
          }
          """
        when:
        module.ivy.expectGet()
        module.getArtifact(classifier: "source").expectGet()

        then:
        run 'retrieve'

        when:
        server.resetExpectations()
        module.ivy.expectHead()
        module.getArtifact(classifier: 'source').expectHead()
        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()
        module.ivy.expectHead()
        module.getArtifact(classifier: 'source').expectHead()

        module.ivy.sha1.expectGet()
        module.ivy.expectGet()
        module.getArtifact(classifier: 'source').expectGet()
        module.getArtifact(classifier: 'source').sha1.expectGet()

        then:
        run 'retrieve'

        when:
        module.publishWithChangedContent()
        server.resetExpectations()
        then:
        executer.withArgument("--offline")
        run 'retrieve'
    }
}
