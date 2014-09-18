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

package org.gradle.api.publish.ivy

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.integtests.fixtures.executer.ProgressLoggingFixture
import org.gradle.internal.jvm.Jvm
import org.gradle.test.fixtures.file.TestFile
import org.gradle.test.fixtures.server.http.IvyHttpModule
import org.gradle.test.fixtures.server.http.IvyHttpRepository
import org.gradle.test.fixtures.server.http.HttpServer
import org.gradle.util.GradleVersion
import org.hamcrest.Matchers
import org.junit.Rule
import org.mortbay.jetty.HttpStatus
import spock.lang.Unroll

import static org.gradle.test.matchers.UserAgentMatcher.matchesNameAndVersion

public class IvyPublishHttpIntegTest extends AbstractIntegrationSpec {
    private static final String BAD_CREDENTIALS = '''
credentials {
    username 'testuser'
    password 'bad'
}
'''
    @Rule ProgressLoggingFixture progressLogging = new ProgressLoggingFixture(executer, temporaryFolder)
    @Rule HttpServer server = new HttpServer()

    private IvyHttpModule module
    private IvyHttpRepository ivyHttpRepo

    def setup() {
        ivyHttpRepo = new IvyHttpRepository(server, ivyRepo)
        module = ivyHttpRepo.module("org.gradle", "publish", "2")
        server.expectUserAgent(matchesNameAndVersion("Gradle", GradleVersion.current().getVersion()))
    }

    def "can publish to unauthenticated HTTP repository"() {
        given:
        server.start()
        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'

            version = '2'
            group = 'org.gradle'

            publishing {
                repositories {
                    ivy { url "${ivyHttpRepo.uri}" }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        module.jar.expectPut()
        module.jar.sha1.expectPut()
        module.ivy.expectPut(HttpStatus.ORDINAL_201_Created)
        module.ivy.sha1.expectPut(HttpStatus.ORDINAL_201_Created)

        when:
        succeeds 'publish'

        then:
        module.assertIvyAndJarFilePublished()
        module.jarFile.assertIsCopyOf(file('build/libs/publish-2.jar'))

        and:
        progressLogging.uploadProgressLogged(module.ivy.uri)
        progressLogging.uploadProgressLogged(module.jar.uri)
    }

    @Unroll
    def "can publish to authenticated repository using #authScheme auth"() {
        given:
        server.start()

        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'

            version = '2'
            group = 'org.gradle'

            publishing {
                repositories {
                    ivy {
                        credentials {
                            username 'testuser'
                            password 'password'
                        }
                        url "${ivyHttpRepo.uri}"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        server.authenticationScheme = authScheme
        module.jar.expectPut('testuser', 'password')
        module.jar.sha1.expectPut('testuser', 'password')
        module.ivy.expectPut('testuser', 'password')
        module.ivy.sha1.expectPut('testuser', 'password')

        when:
        run 'publish'

        then:
        module.assertIvyAndJarFilePublished()
        module.jarFile.assertIsCopyOf(file('build/libs/publish-2.jar'))

        and:
        progressLogging.uploadProgressLogged(module.ivy.uri)
        progressLogging.uploadProgressLogged(module.jar.uri)

        where:
        authScheme << [HttpServer.AuthScheme.BASIC, HttpServer.AuthScheme.DIGEST]
    }

    @Unroll
    def "reports failure publishing with #credsName credentials to authenticated repository using #authScheme auth"() {
        given:
        server.start()

        and:
        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'
            version = '2'
            group = 'org.gradle'
            publishing {
                repositories {
                    ivy {
                        $creds
                        url "${ivyHttpRepo.uri}"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        server.authenticationScheme = authScheme
        server.allowPut('/repo/org.gradle/publish/2/publish-2.jar', 'testuser', 'password')

        when:
        fails 'publish'

        then:
        failure.assertHasDescription('Execution failed for task \':publishIvyPublicationToIvyRepository\'.')
        failure.assertHasCause('Failed to publish publication \'ivy\' to repository \'ivy\'')
        failure.assertThatCause(Matchers.containsString('Received status code 401 from server: Unauthorized'))

        where:
        authScheme                   | credsName | creds
        HttpServer.AuthScheme.BASIC  | 'empty'   | ''
        HttpServer.AuthScheme.DIGEST | 'empty'   | ''
        HttpServer.AuthScheme.BASIC  | 'bad'     | BAD_CREDENTIALS
        HttpServer.AuthScheme.DIGEST | 'bad'     | BAD_CREDENTIALS
    }

    def "reports failure publishing to HTTP repository"() {
        given:
        server.start()
        def repositoryUrl = "http://localhost:${server.port}"

        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'
            version = '2'
            group = 'org.gradle'
            publishing {
                repositories {
                    ivy {
                        url "${ivyHttpRepo.uri}"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        server.addBroken("/")

        when:
        fails 'publish'

        then:
        failure.assertHasDescription('Execution failed for task \':publishIvyPublicationToIvyRepository\'.')
        failure.assertHasCause('Failed to publish publication \'ivy\' to repository \'ivy\'')
        failure.assertThatCause(Matchers.containsString('Received status code 500 from server: broken'))

        when:
        server.stop()

        then:
        fails 'publish'

        and:
        failure.assertHasDescription('Execution failed for task \':publishIvyPublicationToIvyRepository\'.')
        failure.assertHasCause('Failed to publish publication \'ivy\' to repository \'ivy\'')
        failure.assertHasCause("org.apache.http.conn.HttpHostConnectException: Connection to ${repositoryUrl} refused")
    }

    def "uses first configured pattern for publication"() {
        given:
        server.start()

        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'

            version = '2'
            group = 'org.gradle'
            publishing {
                repositories {
                    ivy {
                        artifactPattern "${ivyHttpRepo.artifactPattern}"
                        artifactPattern "http://localhost:${server.port}/alternative/[module]/[artifact]-[revision].[ext]"
                        ivyPattern "${ivyHttpRepo.ivyPattern}"
                        ivyPattern "http://localhost:${server.port}/secondary-ivy/[module]/ivy-[revision].xml"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        module.jar.expectPut()
        module.jar.sha1.expectPut()
        module.ivy.expectPut()
        module.ivy.sha1.expectPut()

        when:
        run 'publish'

        then:
        module.assertIvyAndJarFilePublished()
        module.jarFile.assertIsCopyOf(file('build/libs/publish-2.jar'))
    }

    public void "can publish large artifact (tools.jar) to authenticated repository"() {
        given:
        server.start()
        def toolsJar = Jvm.current().toolsJar

        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'ivy-publish'

            version = '2'
            group = 'org.gradle'

            publishing {
                repositories {
                    ivy {
                        credentials {
                            username 'testuser'
                            password 'password'
                        }
                        url "${ivyHttpRepo.uri}"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        configurations {
                            runtime {
                                artifact('${toolsJar.toURI()}') {
                                    name 'publish'
                                }
                            }
                        }
                    }
                }
            }
        """

        and:
        module.jar.expectPut('testuser', 'password')
        module.jar.sha1.expectPut('testuser', 'password')
        module.ivy.expectPut('testuser', 'password')
        module.ivy.sha1.expectPut('testuser', 'password')

        when:
        run 'publish'

        then:
        module.ivyFile.assertIsFile()
        module.jarFile.assertIsCopyOf(new TestFile(toolsJar))
    }

    public void "does not upload meta-data file if artifact upload fails"() {
        given:
        server.start()

        settingsFile << 'rootProject.name = "publish"'
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'

            version = '2'
            group = 'org.gradle'

            publishing {
                repositories {
                    ivy {
                        url "${ivyHttpRepo.uri}"
                    }
                }
                publications {
                    ivy(IvyPublication) {
                        from components.java
                    }
                }
            }
        """

        and:
        module.jar.expectPut(HttpStatus.ORDINAL_500_Internal_Server_Error)

        when:
        fails ':publish'

        then:
        module.jarFile.assertExists()
        module.ivyFile.assertDoesNotExist()
    }
}