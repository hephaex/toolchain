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
package org.gradle.api.plugins

import org.gradle.api.Project
import org.gradle.api.distribution.DistributionContainer
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.TestUtil
import spock.lang.Specification

class JavaLibraryDistributionPluginTest extends Specification {
    private final Project project = TestUtil.builder().withName("test-project").build()

    def "applies JavaPlugin and adds convention object with default values"() {
        when:
        project.apply(plugin: JavaLibraryDistributionPlugin)

        then:
        project.plugins.hasPlugin(JavaPlugin.class)
        project.extensions.getByType(DistributionContainer.class) != null
        project.plugins.hasPlugin(DistributionPlugin.class)
        project.distributions.main.baseName == project.name
    }

    def "adds distZip task to project"() {
        when:
        project.apply(plugin: JavaLibraryDistributionPlugin)

        then:
        def task = project.tasks.distZip
        task instanceof Zip
        task.archiveName == "test-project.zip"
    }
}
