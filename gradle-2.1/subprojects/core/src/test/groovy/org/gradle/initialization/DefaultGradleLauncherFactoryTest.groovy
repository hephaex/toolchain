/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.initialization

import org.gradle.StartParameter
import org.gradle.internal.nativeplatform.services.NativeServices
import org.gradle.internal.service.DefaultServiceRegistry
import org.gradle.internal.service.ServiceRegistry
import org.gradle.internal.service.scopes.GlobalScopeServices
import org.gradle.logging.LoggingServiceRegistry
import spock.lang.Specification

class DefaultGradleLauncherFactoryTest extends Specification {
    final ServiceRegistry sharedServices = new DefaultServiceRegistry(LoggingServiceRegistry.newEmbeddableLogging(), NativeServices.getInstance()).addProvider(new GlobalScopeServices(false))
    final DefaultGradleLauncherFactory factory = new DefaultGradleLauncherFactory(sharedServices)

    def newInstanceWithStartParameterAndRequestMetaData() {
        StartParameter startParameter = new StartParameter()
        BuildCancellationToken cancellationToken = Mock()
        BuildRequestMetaData metaData = new DefaultBuildRequestMetaData(System.currentTimeMillis());

        expect:
        def launcher = factory.newInstance(startParameter, cancellationToken, metaData)
        launcher.gradle.parent == null
        launcher.gradle.startParameter == startParameter
        launcher.gradle.services.get(BuildRequestMetaData) == metaData
    }

    def newInstanceWithStartParameterWhenNoBuildRunning() {
        StartParameter startParameter = new StartParameter()
        BuildCancellationToken cancellationToken = Mock()

        expect:
        def launcher = factory.newInstance(startParameter, cancellationToken)
        launcher.gradle.parent == null
        launcher.gradle.services.get(BuildRequestMetaData) instanceof DefaultBuildRequestMetaData
    }

    def newInstanceWithStartParameterWhenBuildRunning() {
        StartParameter startParameter = new StartParameter()
        BuildCancellationToken cancellationToken = Mock()
        BuildClientMetaData clientMetaData = Mock()
        BuildRequestMetaData requestMetaData = new DefaultBuildRequestMetaData(clientMetaData, 90)
        DefaultGradleLauncher parent = factory.newInstance(startParameter, cancellationToken, requestMetaData);
        parent.buildListener.buildStarted(parent.gradle)

        expect:
        def launcher = factory.newInstance(startParameter, cancellationToken)
        launcher.gradle.parent == parent.gradle
        def request = launcher.gradle.services.get(BuildRequestMetaData)
        request instanceof DefaultBuildRequestMetaData
        request != requestMetaData
        request.client == clientMetaData
    }
}
