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

class IvyPublishMultiProjectIntegTest extends AbstractIvyPublishIntegTest {
    def project1 = ivyRepo.module("org.gradle.test", "project1", "1.0")
    def project2 = ivyRepo.module("org.gradle.test", "project2", "2.0")
    def project3 = ivyRepo.module("org.gradle.test", "project3", "3.0")

    def "project dependencies are correctly bound to published project"() {
        createBuildScripts("")

        when:
        run "publish"

        then:
        project1.assertPublishedAsJavaModule()
        project1.parsedIvy.assertDependsOn("org.gradle.test:project2:2.0@runtime", "org.gradle.test:project3:3.0@runtime")

        project2.assertPublishedAsJavaModule()
        project2.parsedIvy.assertDependsOn("org.gradle.test:project3:3.0@runtime")

        project3.assertPublishedAsJavaModule()
        project3.parsedIvy.dependencies.isEmpty()

        and:
        resolveArtifacts(project1) == ['project1-1.0.jar', 'project2-2.0.jar', 'project3-3.0.jar']
    }

    def "project dependencies reference publication identity of dependent project"() {
        def project3 = ivyRepo.module("changed.org", "changed-module", "changed")

        createBuildScripts("""
project(":project3") {
    publishing {
        publications.ivy {
            organisation "changed.org"
            module "changed-module"
            revision "changed"
        }
    }
}
""")

        when:
        run "publish"

        then:
        project1.assertPublishedAsJavaModule()
        project1.parsedIvy.assertDependsOn("org.gradle.test:project2:2.0@runtime", "changed.org:changed-module:changed@runtime")

        project2.assertPublishedAsJavaModule()
        project2.parsedIvy.assertDependsOn("changed.org:changed-module:changed@runtime")

        project3.assertPublishedAsJavaModule()
        project3.parsedIvy.dependencies.isEmpty()

        and:
        resolveArtifacts(project1) == ['changed-module-changed.jar', 'project1-1.0.jar', 'project2-2.0.jar']
    }

    def "reports failure when project dependency references a project with multiple publications"() {
        createBuildScripts("""
project(":project3") {
    publishing {
        publications {
            extraIvy(IvyPublication) {
                from components.java
                organisation "extra.org"
                module "extra-module"
                revision "extra"
            }
        }
    }
}
""")

        when:
        fails "publish"

        then:
        failure.assertHasDescription "A problem occurred configuring project ':project1'."
        failure.assertHasCause "Publishing is not yet able to resolve a dependency on a project with multiple different publications."
    }

    def "ivy-publish plugin does not take archivesBaseName into account"() {
        createBuildScripts("""
project(":project2") {
    archivesBaseName = "changed"
}
        """)

        when:
        run "publish"

        then:
        project1.assertPublishedAsJavaModule()
        project1.parsedIvy.assertDependsOn("org.gradle.test:project2:2.0@runtime", "org.gradle.test:project3:3.0@runtime")

        // published with the correct coordinates, even though artifact has different name
        project2.assertPublishedAsJavaModule()
        project2.parsedIvy.assertDependsOn("org.gradle.test:project3:3.0@runtime")

        project3.assertPublishedAsJavaModule()
        project3.parsedIvy.dependencies.isEmpty()
    }

    def "ivy-publish plugin uses target project name for project dependency when target project does not have ivy-publish plugin applied"() {
        given:
        settingsFile << """
include "project1", "project2"
        """

        buildFile << """
allprojects {
    group = "org.gradle.test"
    version = "1.0"
}

project(":project1") {
    apply plugin: "java"
    apply plugin: "ivy-publish"

    dependencies {
        compile project(":project2")
    }

    publishing {
        repositories {
            ivy { url "${ivyRepo.uri}" }
        }
        publications {
            ivy(IvyPublication) {
                from components.java
            }
        }
    }
}
project(":project2") {
    apply plugin: 'java'
    archivesBaseName = "changed"
}
        """

        when:
        run "publish"

        then:
        project1.assertPublishedAsJavaModule()
        project1.parsedIvy.assertDependsOn("org.gradle.test:project2:1.0@runtime")
    }



    private void createBuildScripts(String append = "") {
        settingsFile << """
include "project1", "project2", "project3"
        """

        buildFile << """
allprojects {
    group = "org.gradle.test"
    version = "3.0"
}

subprojects {
    apply plugin: "java"
    apply plugin: "ivy-publish"

    publishing {
        repositories {
            ivy { url "${ivyRepo.uri}" }
        }
        publications {
            ivy(IvyPublication) {
                from components.java
            }
        }
    }
}

project(":project1") {
    version = "1.0"
    dependencies {
        compile project(":project2")
        compile project(":project3")
    }
}
project(":project2") {
    version = "2.0"
    dependencies {
        compile project(":project3")
    }
}

$append
        """
    }
}
