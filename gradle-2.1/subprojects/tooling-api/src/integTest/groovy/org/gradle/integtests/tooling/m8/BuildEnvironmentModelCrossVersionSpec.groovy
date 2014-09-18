/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.integtests.tooling.m8

import org.gradle.integtests.tooling.fixture.TargetGradleVersion
import org.gradle.integtests.tooling.fixture.ToolingApiSpecification
import org.gradle.tooling.model.GradleProject
import org.gradle.tooling.model.build.BuildEnvironment

class BuildEnvironmentModelCrossVersionSpec extends ToolingApiSpecification {

    def "informs about build environment"() {
        when:
        BuildEnvironment model = withConnection { it.getModel(BuildEnvironment.class) }

        then:
        model.gradle.gradleVersion == targetDist.version.version
        model.java.javaHome
        !model.java.jvmArguments.empty
    }

    @TargetGradleVersion("<1.0-milestone-8")
    def "partial BuildEnvironment model for pre 1.0m8 providers"() {
        when:
        BuildEnvironment buildEnv = withConnection { it.getModel(BuildEnvironment.class) }

        then:
        buildEnv != null
        buildEnv.gradle.gradleVersion == targetDist.version.version
    }

    def "informs about java args as in the build script"() {
        given:
        toolingApi.isEmbedded = false //cannot be run in embedded mode

        file('build.gradle') <<
            "project.description = java.lang.management.ManagementFactory.runtimeMXBean.inputArguments.join('##')"

        when:
        BuildEnvironment env = withConnection { it.getModel(BuildEnvironment.class) }
        GradleProject project = withConnection { it.getModel(GradleProject.class) }

        then:
        def inputArgsInBuild = project.description.split('##') as List
        env.java.jvmArguments.each { inputArgsInBuild.contains(it) }
    }

    @TargetGradleVersion(">=1.0-milestone-8 <=1.0-milestone-9")
    def "informs about java home as in the build script for older versions"() {
        given:
        file('build.gradle') << """
        description = org.gradle.util.Jvm.current().javaHome.toString()
        """

        when:
        BuildEnvironment env = withConnection { it.getModel(BuildEnvironment.class) }
        GradleProject project = withConnection { it.getModel(GradleProject.class) }

        then:
        env.java.javaHome.toString() == project.description
    }

    @TargetGradleVersion(">1.0-milestone-9")
    def "informs about java home as in the build script"() {
        given:
        file('build.gradle') << """
        description = org.gradle.internal.jvm.Jvm.current().javaHome.toString()
        """

        when:
        BuildEnvironment env = withConnection { it.getModel(BuildEnvironment.class) }
        GradleProject project = withConnection { it.getModel(GradleProject.class) }

        then:
        env.java.javaHome.toString() == project.description
    }

    def "informs about gradle version as in the build script"() {
        given:
        file('build.gradle') << "description = GradleVersion.current().getVersion()"

        when:
        BuildEnvironment env = withConnection { it.getModel(BuildEnvironment.class) }
        GradleProject project = withConnection { it.getModel(GradleProject.class) }

        then:
        env.gradle.gradleVersion == project.description
    }
}
