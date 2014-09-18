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

package org.gradle.api.internal.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware
import org.gradle.api.plugins.PluginContainer
import org.gradle.model.InvalidModelRuleDeclarationException
import org.gradle.model.Model
import org.gradle.model.RuleSource
import org.gradle.model.internal.core.ModelCreator
import org.gradle.model.internal.inspect.ModelRuleInspector
import org.gradle.model.internal.registry.ModelRegistry
import org.gradle.model.internal.registry.ModelRegistryScope
import spock.lang.Specification

class PluginModelRuleExtractorTest extends Specification {

    def inspector = new ModelRuleInspector()
    def extractor = new PluginModelRuleExtractor(inspector)
    def registry = Mock(ModelRegistry)

    class ModelAwareTarget implements PluginAware, ModelRegistryScope {
        @Override
        PluginContainer getPlugins() {
            throw new UnsupportedOperationException()
        }

        @Override
        void apply(Closure closure) {
            throw new UnsupportedOperationException()
        }

        @Override
        void apply(Map<String, ?> options) {
            throw new UnsupportedOperationException()
        }

        @Override
        ModelRegistry getModelRegistry() {
            registry
        }
    }

    static class NotModelAwareTarget implements PluginAware {
        @Override
        PluginContainer getPlugins() {
            throw new UnsupportedOperationException()
        }

        @Override
        void apply(Closure closure) {
            throw new UnsupportedOperationException()
        }

        @Override
        void apply(Map<String, ?> options) {
            throw new UnsupportedOperationException()
        }

    }

    static class NoSources implements Plugin<Project> {
        @Override
        void apply(Project target) {

        }
    }

    PluginApplication application(Plugin plugin, PluginAware pluginAware) {
        new PluginApplication(plugin, pluginAware)
    }

    def "no op when plugin has no sources"() {
        when:
        extractor.execute(application(new NoSources(), new ModelAwareTarget()))

        then:
        0 * registry._
    }

    static class Thing {}

    static class HasSource implements Plugin<Project> {
        @Override
        void apply(Project target) {

        }

        @RuleSource
        static class Rules {
            @Model
            static Thing thing() { new Thing() }
        }
    }

    def "registers sources"() {
        when:
        extractor.execute(application(new HasSource(), new ModelAwareTarget()))

        then:
        1 * registry.create(_ as ModelCreator)
    }

    def "target is not model capable"() {
        when:
        extractor.execute(application(new HasSource(), new NotModelAwareTarget()))

        then:
        thrown(UnsupportedOperationException)
    }

    static class HasInvalidSource implements Plugin<Project> {
        @Override
        void apply(Project target) {

        }

        @RuleSource
        class Rules {
            @Model
            static Thing thing() { new Thing() }
        }

    }

    def "error extracting rules"() {
        when:
        extractor.execute(application(new HasInvalidSource(), new ModelAwareTarget()))

        then:
        thrown(InvalidModelRuleDeclarationException)
    }
}
