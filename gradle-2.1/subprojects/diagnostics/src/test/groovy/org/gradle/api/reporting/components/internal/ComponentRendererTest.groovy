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



package org.gradle.api.reporting.components.internal

import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.diagnostics.internal.text.DefaultTextReportBuilder
import org.gradle.language.base.LanguageSourceSet
import org.gradle.logging.TestStyledTextOutput
import org.gradle.runtime.base.BinarySpec
import org.gradle.runtime.base.ComponentSpec
import spock.lang.Specification

class ComponentRendererTest extends Specification {
    def project = Stub(Project) {
        toString() >> "<project>"
    }
    def resolver = Stub(FileResolver)
    def output = new TestStyledTextOutput()
    def builder = new DefaultTextReportBuilder(output)
    def renderer = new ComponentRenderer(resolver)

    def "renders component"() {
        def component = Stub(ComponentSpec)
        component.displayName >> "<component>"
        component.source >> new DefaultDomainObjectSet<LanguageSourceSet>(LanguageSourceSet)

        when:
        renderer.render(component, builder)

        then:
        output.value.startsWith("""{header}<component>
-----------{normal}
""")
    }

    def "renders component with no source sets"() {
        def component = Stub(ComponentSpec)
        component.source >> new DefaultDomainObjectSet<LanguageSourceSet>(LanguageSourceSet)

        when:
        renderer.render(component, builder)

        then:
        output.value.contains("No source sets")
    }

    def "renders component with no binaries"() {
        def component = Stub(ComponentSpec)
        component.binaries >> new DefaultDomainObjectSet<BinarySpec>(BinarySpec)

        when:
        renderer.render(component, builder)

        then:
        output.value.contains("No binaries")
    }

}
