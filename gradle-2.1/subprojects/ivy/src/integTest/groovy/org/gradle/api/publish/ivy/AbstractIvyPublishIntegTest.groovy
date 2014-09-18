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

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.test.fixtures.ivy.IvyFileModule

class AbstractIvyPublishIntegTest extends AbstractIntegrationSpec {

    protected def resolveArtifacts(IvyFileModule module) {
        doResolveArtifacts("group: '${sq(module.organisation)}', name: '${sq(module.module)}', version: '${sq(module.revision)}'")
    }

    protected def resolveArtifacts(IvyFileModule module, def configuration) {
        doResolveArtifacts("group: '${sq(module.organisation)}', name: '${sq(module.module)}', version: '${sq(module.revision)}', configuration: '${sq(configuration)}'")
    }

    protected def resolveArtifactsWithStatus(IvyFileModule module, def status) {
        doResolveArtifacts("group: '${sq(module.organisation)}', name: '${sq(module.module)}', version: '${sq(module.revision)}'", status)
    }

    private def doResolveArtifacts(def dependency, def status=null) {
        // Replace the existing buildfile with one for resolving the published module
        settingsFile.text = "rootProject.name = 'resolve'"
        buildFile.text = """
            configurations {
                resolve
            }
            repositories {
                ivy { url "${ivyRepo.uri}" }
                mavenCentral()
            }
            dependencies {
                resolve $dependency
            }

            task resolveArtifacts(type: Sync) {
                from configurations.resolve
                into "artifacts"
            }
        """

        if (status != null) {
            buildFile.text = buildFile.text + """

                dependencies.components.eachComponent { ComponentMetadataDetails details, IvyModuleDescriptor ivyModule ->
                    details.statusScheme = [ '${sq(status)}' ]
                }
            """
        }

        run "resolveArtifacts"
        def artifactsList = file("artifacts").exists() ? file("artifacts").list() : []
        return artifactsList.sort()
    }

    String sq(String input) {
        return escapeForSingleQuoting(input)
    }

    String escapeForSingleQuoting(String input) {
        return input.replace('\\', '\\\\').replace('\'', '\\\'')
    }
}
