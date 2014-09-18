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

package org.gradle.plugin.use.resolve.internal

import org.gradle.api.Plugin
import org.gradle.api.internal.DocumentationRegistry
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.api.plugins.UnknownPluginException
import org.gradle.groovy.scripts.StringScriptSource
import org.gradle.plugin.use.internal.DefaultPluginRequest
import org.gradle.plugin.use.internal.InvalidPluginRequestException
import org.gradle.plugin.use.internal.PluginRequest
import spock.lang.Specification

class CorePluginResolverTest extends Specification {

    static class MyPlugin implements Plugin {
        @Override
        void apply(Object target) {
        }
    }

    def docRegistry = Mock(DocumentationRegistry)
    def pluginRegistry = Mock(PluginRegistry)
    def result = Mock(PluginResolutionResult)

    def resolver = new CorePluginResolver(docRegistry, pluginRegistry)

    PluginRequest request(String id, String version = null) {
        new DefaultPluginRequest(id, version, 1, new StringScriptSource("test", "test"))
    }

    def "non core plugins are ignored"() {
        when:
        resolver.resolve(request("foo.bar"), result)

        then:
        1 * result.notFound(resolver.getDescription(), "plugin is not in 'org.gradle' namespace")
    }

    def "can resolve unqualified"() {
        when:
        resolver.resolve(request("foo"), result)

        then:
        1 * pluginRegistry.getTypeForId("foo") >> MyPlugin
        1 * result.found(resolver.getDescription(), { it instanceof SimplePluginResolution && it.resolve() == MyPlugin })
    }

    def "can resolve qualified"() {
        when:
        resolver.resolve(request("${resolver.CORE_PLUGIN_NAMESPACE}.foo"), result)

        then:
        1 * pluginRegistry.getTypeForId("foo") >> MyPlugin
        1 * result.found(resolver.getDescription(), { it instanceof SimplePluginResolution && it.resolve() == MyPlugin })
    }

    def "cannot have version number"() {
        when:
        resolver.resolve(request("foo", "1.0"), result)

        then:
        1 * pluginRegistry.getTypeForId("foo") >> MyPlugin

        and:
        thrown InvalidPluginRequestException
    }

    def "ignores plugin with version that isn't found in registry"() {
        when:
        resolver.resolve(request("org.gradle.foo", "1.0"), result)

        then:
        1 * pluginRegistry.getTypeForId("foo") >> { throw new UnknownPluginException("foo") }
        1 * result.notFound(resolver.getDescription(), { it.contains("not a core plugin") })
    }

}
