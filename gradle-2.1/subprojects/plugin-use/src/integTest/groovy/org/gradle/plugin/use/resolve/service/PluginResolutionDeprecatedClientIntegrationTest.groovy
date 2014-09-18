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


package org.gradle.plugin.use.resolve.service

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.internal.hash.HashUtil
import org.gradle.plugin.use.internal.PluginUsePluginServiceRegistry
import org.gradle.plugin.use.resolve.service.internal.CachingPluginResolutionServiceClient
import org.gradle.test.fixtures.plugin.PluginBuilder
import org.gradle.test.fixtures.server.http.MavenHttpModule
import org.junit.Rule

import static org.gradle.util.Matchers.containsText

class PluginResolutionDeprecatedClientIntegrationTest extends AbstractIntegrationSpec {

    public static final String PLUGIN_ID_1 = "org.my.myplugin_1"
    public static final String PLUGIN_ID_2 = "org.my.myplugin_2"
    public static final String VERSION = "1.0"
    public static final String GROUP = "my"
    public static final String ARTIFACT_1 = "plugin_1"
    public static final String ARTIFACT_2 = "plugin_2"

    def pluginBuilder = new PluginBuilder(file("plugin"))

    @Rule
    PluginResolutionServiceTestServer service = new PluginResolutionServiceTestServer(executer, mavenRepo)
    private MavenHttpModule module1 = service.m2repo.module(GROUP, ARTIFACT_1, VERSION)
    private MavenHttpModule module2 = service.m2repo.module(GROUP, ARTIFACT_2, VERSION)

    def setup() {
        executer.requireOwnGradleUserHomeDir()
        executer.beforeExecute {
            it.withDeprecationChecksDisabled().withStackTraceChecksDisabled()
            moduleResolution()
        }

        publishPlugin()
        service.start()
    }

    def "deprecation message is displayed if protocol is deprecated"() {
        given:
        deprecateClient("FOO")

        expect:
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        reset()
        build()
        containsDeprecationMessage("FOO")

        // test that the right scoped cache was created.
        // would be nice to have a less fragile way to do this.
        new File(executer.gradleUserHomeDir, "caches/${executer.distribution.version.version}/${PluginUsePluginServiceRegistry.CACHE_NAME}/${CachingPluginResolutionServiceClient.CLIENT_STATUS_CACHE_NAME}.bin".toString()).exists()
    }

    def "deprecation message is output once per build"() {
        expect:
        deprecateClient("FOO")
        useBothPlugins()
        pluginQuery1()
        pluginQuery2()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        reset()
        build()
        containsDeprecationMessage("FOO")
    }

    def "changed deprecation message is detected across builds"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        reset()
        deprecateClient("BAR")
        usePlugin2() // use a different plugin, because the response for the previous will be cached
        pluginQuery2()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("BAR")
    }

    def "deprecation message disappears if revoked server side"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        reset()
        deprecateClient(null)
        usePlugin2() // use a different plugin, because the response for the previous will be cached
        pluginQuery2()
        build()
        containsNoDeprecationMessage()
    }

    def "deprecation displayed on error"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQueryError1()
        service.expectStatusQuery()
        failError()
        containsDeprecationMessage("FOO")

        usePlugin1()
        pluginQueryError1()
        failError()
        containsDeprecationMessage("FOO")
    }

    def "deprecation displayed on not found"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQueryNotFound1()
        service.expectStatusQuery()
        failPluginNotFound()
        containsDeprecationMessage("FOO")

        usePlugin1()
        pluginQueryNotFound1()
        failPluginNotFound()
        containsDeprecationMessage("FOO")
    }

    def "deprecation message is checked when running with refresh dependencies"() {
        expect:
        deprecateClient("FOO")
        service.statusChecksum("a")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        deprecateClient("BAR")
        service.statusChecksum("a")
        build()
        containsDeprecationMessage("FOO")

        args "--refresh-dependencies"
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("BAR")

        build()
        containsDeprecationMessage("BAR")
    }

    def "deprecation message is displayed when running with offline"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")

        args "--offline"
        build()
        containsDeprecationMessage("FOO")
    }

    def "error response from status does not fail build"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQuery404()
        build()
        output.contains("Received error response fetching client status from")

        service.expectStatusQuery404()
        build()
        output.contains("Received error response fetching client status from")
    }

    def "exception response from status does not fail build"() {
        expect:
        deprecateClient("FOO")
        usePlugin1()
        pluginQuery1()
        service.expectStatusQueryOutOfProtocol()
        build()
        output.contains("Exception thrown fetching client status")

        service.expectStatusQueryOutOfProtocol()
        build()
        output.contains("Exception thrown fetching client status")

        // Test that if the issue gets resolved, everything works as it should
        service.expectStatusQuery()
        build()
        containsDeprecationMessage("FOO")
    }

    void usePlugin1() {
        buildScript """
            plugins {
                id '$PLUGIN_ID_1' version '$VERSION'
            }
        """
    }

    void usePlugin2() {
        buildScript """
            plugins {
                id '$PLUGIN_ID_2' version '$VERSION'
            }
        """
    }

    void useBothPlugins() {
        buildScript """
            plugins {
                id '$PLUGIN_ID_1' version '$VERSION'
                id '$PLUGIN_ID_2' version '$VERSION'
            }
        """

    }

    void reset() {
        service.http.resetExpectations()
    }

    void build() {
        run "tasks"
    }

    void containsDeprecationMessage(String message) {
        assert effectiveOutput.count("Plugin resolution service client status service ${service.apiAddress}/${executer.distribution.version.version} reported that this client has been deprecated: $message") == 1
    }

    void containsNoDeprecationMessage() {
        effectiveOutput.count("this client has been deprecated") == 0
    }

    private String getEffectiveOutput() {
        (failure == null ? result : failure).output
    }

    void failPluginNotFound() {
        fails "tasks"
        failure.assertThatDescription(containsText("Plugin \\[id: '.+', version: '1.0'\\] was not found"))
    }

    void fail() {
        fails "tasks"
    }

    void failError() {
        fails "tasks"
        failure.assertThatDescription(containsText("Error resolving plugin"))
    }

    void publishPlugin() {
        pluginBuilder.addPlugin("project.ext.pluginApplied = true", PLUGIN_ID_1, "TestPlugin1")
        pluginBuilder.addPlugin("project.ext.pluginApplied = true", PLUGIN_ID_2, "TestPlugin2")
        pluginBuilder.publishTo(executer, module1.artifactFile)
        module1.artifactFile.copyTo(module2.artifactFile)
    }

    private void moduleResolution() {
        module1.allowAll()
        module2.allowAll()
    }

    void pluginQuery1() {
        service.expectPluginQuery(PLUGIN_ID_1, VERSION, GROUP, ARTIFACT_1, VERSION)
    }

    void pluginQuery2() {
        service.expectPluginQuery(PLUGIN_ID_2, VERSION, GROUP, ARTIFACT_2, VERSION)
    }


    void deprecateClient(String msg) {
        service.deprecateClient(msg)
        service.statusChecksum(msg == null ? null : HashUtil.createCompactMD5(msg))
    }

    void pluginQueryNotFound1() {
        service.expectNotFound(PLUGIN_ID_1, VERSION)
    }

    void pluginQueryNotFound2() {
        service.expectNotFound(PLUGIN_ID_1, VERSION)
    }

    void pluginQueryError1() {
        service.expectQueryAndReturnError(PLUGIN_ID_1, VERSION, 500) {
            errorCode = "INTERNAL_SERVER_ERROR"
            message = "Something went wrong"
        }
    }

}
