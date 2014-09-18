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


package org.gradle.api.publish.ivy

public class IvyPublishCoordinatesIntegTest extends AbstractIvyPublishIntegTest {

    def "can publish single jar with specified coordinates"() {
        given:
        def module = ivyRepo.module('org.custom', 'custom', '2.2')

        and:
        settingsFile << "rootProject.name = 'root'"
        buildFile << """
            apply plugin: 'ivy-publish'
            apply plugin: 'java'

            group = 'group'
            version = '1.0'

            publishing {
                repositories {
                    ivy { url "${ivyRepo.uri}" }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                        organisation "org.custom"
                        module "custom"
                        revision "2.2"
                    }
                }
            }
        """

        when:
        succeeds 'publish'

        then:
        file('build/libs/root-1.0.jar').assertExists()

        and:
        module.assertPublishedAsJavaModule()
        module.moduleDir.file('custom-2.2.jar').assertIsCopyOf(file('build/libs/root-1.0.jar'))

        and:
        resolveArtifacts(module) == ['custom-2.2.jar']
    }

    def "can produce multiple separate publications for single project"() {
        given:
        def module = ivyRepo.module('org.custom', 'custom', '2.2')
        def apiModule = ivyRepo.module('org.custom', 'custom-api', '2')

        and:
        settingsFile << "rootProject.name = 'root'"
        buildFile << """
            apply plugin: 'ivy-publish'
            apply plugin: 'java'

            group = 'group'
            version = '1.0'

            task apiJar(type: Jar) {
                from sourceSets.main.output
                baseName "root-api"
                exclude "**/impl/**"
            }

            publishing {
                repositories {
                    ivy { url "${ivyRepo.uri}" }
                }
                publications {
                    ivy(IvyPublication) {
                        organisation "org.custom"
                        module "custom"
                        revision "2.2"
                        from components.java
                    }
                    ivyApi(IvyPublication) {
                        organisation "org.custom"
                        module "custom-api"
                        revision "2"
                        configurations {
                            runtime {}
                            "default" {
                                extend "runtime"
                            }
                        }
                        artifact(apiJar) {
                            conf "runtime"
                        }
                    }
                }
            }
        """

        when:
        succeeds 'publish'

        then:
        file('build/libs').assertHasDescendants("root-1.0.jar", "root-api-1.0.jar")

        and:
        module.assertPublishedAsJavaModule()
        module.moduleDir.file('custom-2.2.jar').assertIsCopyOf(file('build/libs/root-1.0.jar'))

        and:
        apiModule.assertPublishedAsJavaModule()
        apiModule.moduleDir.file('custom-api-2.jar').assertIsCopyOf(file('build/libs/root-api-1.0.jar'))

        and:
        resolveArtifacts(module) == ['custom-2.2.jar']
        resolveArtifacts(apiModule) == ['custom-api-2.jar']
    }

}
