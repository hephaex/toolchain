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

package org.gradle.integtests.tooling.r18

import org.gradle.integtests.tooling.fixture.TargetGradleVersion
import org.gradle.integtests.tooling.fixture.ToolingApiSpecification
import org.gradle.integtests.tooling.fixture.ToolingApiVersion
import org.gradle.tooling.model.GradleProject
import org.gradle.tooling.model.UnsupportedMethodException
import org.gradle.tooling.model.eclipse.EclipseProject
import org.gradle.tooling.model.idea.IdeaProject

@ToolingApiVersion('>=1.8')
@TargetGradleVersion('>=1.8')
class BuildScriptModelCrossVersionSpec extends ToolingApiSpecification {
    def "GradleProject provides details about the project's build script"() {
        when:
        buildFile << '//empty'
        GradleProject project = withConnection { it.getModel(GradleProject.class) }

        then:
        project.buildScript.sourceFile == buildFile

        when:
        def custom = file('gradle/my-project.gradle') << '//empty'
        file('settings.gradle') << "rootProject.buildFileName = 'gradle/my-project.gradle'"
        project = withConnection { it.getModel(GradleProject.class) }

        then:
        project.buildScript.sourceFile == custom
    }

    @TargetGradleVersion('<1.8 >=1.0-milestone-8')
    def "gives reasonable error message when target Gradle version does not provide build script details"() {
        when:
        GradleProject project = withConnection { it.getModel(GradleProject.class) }
        project.buildScript

        then:
        UnsupportedMethodException e = thrown()
        e.message.startsWith('Unsupported method: GradleProject.getBuildScript().')

        when:
        EclipseProject eclipseProject = withConnection { it.getModel(EclipseProject.class) }
        eclipseProject.gradleProject.buildScript

        then:
        e = thrown()
        e.message.startsWith('Unsupported method: GradleProject.getBuildScript().')

        when:
        IdeaProject ideaProject = withConnection { it.getModel(IdeaProject.class) }
        ideaProject.modules.each { it.gradleProject.buildScript }

        then:
        e = thrown()
        e.message.startsWith('Unsupported method: GradleProject.getBuildScript().')
    }
}
