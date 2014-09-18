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

import org.gradle.test.fixtures.ivy.IvyDescriptorArtifact

class IvyPublishArtifactCustomizationIntegTest extends AbstractIvyPublishIntegTest {

    def module = ivyRepo.module("org.gradle.test", "ivyPublish", "2.4")

    public void "can publish custom artifacts"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact "customFile.txt"
                    artifact customDocsTask.outputFile
                    artifact customJar
                }
            }
""", """
        model {
            tasks.publishIvyPublicationToIvyRepository {
              dependsOn "customDocsTask"
            }
        }
""")

        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "ivyPublish-2.4.txt", "ivyPublish-2.4.html", "ivyPublish-2.4.jar")

        and:
        def ivy = module.parsedIvy
        ivy.expectArtifact('ivyPublish', 'txt').hasType("txt").hasConf(null)
        ivy.expectArtifact('ivyPublish', 'html').hasType("html").hasConf(null)
        ivy.expectArtifact('ivyPublish', 'jar').hasType("jar").hasConf(null)

        and:
        resolveArtifacts(module) == ["ivyPublish-2.4.html", "ivyPublish-2.4.jar", "ivyPublish-2.4.txt"]
    }

    def "can configure custom artifacts when creating"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    configurations {
                        foo {}
                        bar {}
                        "default" {
                            extend "foo"
                        }
                    }
                    artifact("customFile.txt") {
                        name "customFile"
                        classifier "classified"
                        conf "foo,bar"
                    }
                    artifact(customDocsTask.outputFile) {
                        name "docs"
                        extension "htm"
                        builtBy customDocsTask
                    }
                    artifact(customJar) {
                        extension "war"
                        type "web-archive"
                        conf "*"
                    }
                }
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "docs-2.4.htm", "customFile-2.4-classified.txt", "ivyPublish-2.4.war")

        and:
        def ivy = module.parsedIvy
        ivy.expectArtifact("ivyPublish", "war").hasType("web-archive").hasConf(["*"])
        ivy.expectArtifact("docs", "htm").hasType("html").hasConf(null)
        ivy.expectArtifact("customFile", "txt", "classified").hasType("txt").hasConf(["foo", "bar"])

        and:
        resolveArtifacts(module) == ["customFile-2.4-classified.txt", "docs-2.4.htm", "ivyPublish-2.4.war"]
    }

    def "can publish custom file artifacts with map notation"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    configurations {
                        foo {}
                        bar {}
                        "default" {
                            extend "foo"
                        }
                    }
                    artifact source: "customFile.txt", name: "customFile", classifier: "classified", conf: "foo,bar"
                    artifact source: customDocsTask.outputFile, name: "docs", extension: "htm", builtBy: customDocsTask
                    artifact source: customJar, extension: "war", type: "web-archive", conf: "*"
                }
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "docs-2.4.htm", "customFile-2.4-classified.txt", "ivyPublish-2.4.war")

        and:
        def ivy = module.parsedIvy
        ivy.expectArtifact("ivyPublish", "war").hasType("web-archive").hasConf(["*"])
        ivy.expectArtifact("docs", "htm").hasType("html").hasConf(null)
        ivy.expectArtifact("customFile", "txt", "classified").hasType("txt").hasConf(["foo", "bar"])

        and:
        resolveArtifacts(module) == ["customFile-2.4-classified.txt", "docs-2.4.htm", "ivyPublish-2.4.war"]
    }

    def "can set custom artifacts to override component artifacts"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    from components.java
                    artifacts = ["customFile.txt", customDocsTask.outputFile, customJar]
                }
            }
""", """
            model {
                tasks.publishIvyPublicationToIvyRepository {
                    dependsOn("customDocsTask")
                }
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "ivyPublish-2.4.txt", "ivyPublish-2.4.html", "ivyPublish-2.4.jar")
        module.parsedIvy.artifacts.collect({"${it.name}.${it.ext}"}) as Set == ["ivyPublish.txt", "ivyPublish.html", "ivyPublish.jar"] as Set
    }

    def "can configure custom artifacts post creation"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    configurations {
                        mod_conf {}
                        other {}
                    }
                    artifact source: "customFile.txt", name: "customFile"
                    artifact source: customDocsTask.outputFile, name: "docs", builtBy: customDocsTask
                    artifact source: customJar
                }
            }
""", """
            publishing.publications.ivy.artifacts.each {
                it.extension = "mod"
                it.conf = "mod_conf"
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "customFile-2.4.mod", "docs-2.4.mod", "ivyPublish-2.4.mod")

        for (IvyDescriptorArtifact artifact : module.parsedIvy.artifacts) {
            artifact.ext == "mod"
            artifact.conf == "mod-conf"
        }
    }

    def "can publish artifact with no extension"() {
        given:
        file("no-extension") << "some content"
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact source: 'no-extension', name: 'no-extension', type: 'ext-less'
                }
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "no-extension-2.4")
        module.parsedIvy.expectArtifact("no-extension").hasAttributes("", "ext-less", null)

        and:
        resolveArtifacts(module) == ["no-extension-2.4"]
    }

    def "can publish artifact with classifier"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact source: customJar, classifier: "classy"
                }
            }
""")
        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        module.assertArtifactsPublished("ivy-2.4.xml", "ivyPublish-2.4-classy.jar")
        module.parsedIvy.expectArtifact("ivyPublish").hasAttributes("jar", "jar", null, "classy")

        and:
        resolveArtifacts(module) == ["ivyPublish-2.4-classy.jar"]
    }

    def "can add custom configurations"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    configurations {
                        runtime {}
                        base {}
                        custom {
                            extend "runtime"
                            extend "base"
                        }
                    }
                }
            }
""")

        when:
        succeeds 'publish'

        then:
        module.assertPublished()
        def ivy = module.parsedIvy
        ivy.configurations.keySet() == ["base", "custom", "runtime"] as Set
        ivy.configurations["runtime"].extend == null
        ivy.configurations["base"].extend == null
        ivy.configurations["custom"].extend == ["runtime", "base"] as Set
    }

    def "reports failure publishing when validation fails"() {
        given:
        file("a-directory.dir").createDir()

        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact "a-directory.dir"
                }
            }
""")
        when:
        fails 'publish'

        then:
        failure.assertHasDescription("Execution failed for task ':publishIvyPublicationToIvyRepository'.")
        failure.assertHasCause("Failed to publish publication 'ivy' to repository 'ivy'")
        failure.assertHasCause("Invalid publication 'ivy': artifact file is a directory")
    }

    def "cannot publish when artifact does not exist"() {
        given:
        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact source: "no-exist", type: "jar"
                }
            }
""")
        when:
        fails 'publish'

        then:
        failure.assertHasDescription("Execution failed for task ':publishIvyPublicationToIvyRepository'.")
        failure.assertHasCause("Failed to publish publication 'ivy' to repository 'ivy'")
        failure.assertHasCause("Invalid publication 'ivy': artifact file does not exist: '${file('no-exist')}'")
    }

    def "reports failure to convert artifact notation"() {
        given:
        file("a-directory.dir").createDir()

        createBuildScripts("""
            publications {
                ivy(IvyPublication) {
                    artifact 12
                }
            }
""")
        when:
        fails 'publish'

        then:
        failure.assertHasCause("""Cannot convert the provided notation to an object of type IvyArtifact: 12.
The following types/formats are supported:
  - Instances of IvyArtifact.
  - Instances of AbstractArchiveTask.
  - Instances of PublishArtifact.
  - Maps containing a 'source' entry, e.g. [source: '/path/to/file', extension: 'zip'].
  - Anything that can be converted to a file, as per Project.file()""")
    }

    private createBuildScripts(def publications, def append = "") {
        file("customFile.txt") << "some content"
        settingsFile << "rootProject.name = 'ivyPublish'"
        buildFile << """
            apply plugin: 'java'
            apply plugin: 'ivy-publish'

            group = 'org.gradle.test'
            version = '2.4'

            task customDocsTask {
                ext.outputFile = file('customDocs.html')
                doLast {
                    outputFile << '<html/>'
                }
            }

            task customJar(type: Jar) {
                from file("customFile.txt")
                baseName "customJar"
            }

            publishing {
                repositories {
                    ivy { url "${ivyRepo.uri}" }
                }
                $publications
            }

            $append
        """
    }
}
