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

package org.gradle.tooling.internal.provider;

import org.gradle.initialization.GradleLauncherFactory;
import org.gradle.internal.service.ServiceRegistration;
import org.gradle.internal.service.scopes.GlobalScopeServices;
import org.gradle.launcher.exec.InProcessBuildActionExecuter;
import org.gradle.logging.LoggingServiceRegistry;
import org.gradle.tooling.internal.adapter.ProtocolToModelAdapter;

/**
 * Shared services for a tooling API provider connection.
 */
public class ConnectionScopeServices {
    private final LoggingServiceRegistry loggingServices;

    public ConnectionScopeServices(LoggingServiceRegistry loggingServices) {
        this.loggingServices = loggingServices;
    }

    void configure(ServiceRegistration serviceRegistration) {
        serviceRegistration.add(LoggingServiceRegistry.class, loggingServices);
        serviceRegistration.addProvider(new GlobalScopeServices(false));
    }

    ProviderConnection createProviderConnection(GradleLauncherFactory gradleLauncherFactory) {
        return new ProviderConnection(
                loggingServices,
                new InProcessBuildActionExecuter(gradleLauncherFactory),
                new PayloadSerializer(
                        new ClientSidePayloadClassLoaderRegistry(
                                new DefaultPayloadClassLoaderRegistry(
                                        new ModelClassLoaderFactory()),
                                new ClasspathInferer()))
        );
    }

    ProtocolToModelAdapter createProtocolToModelAdapter() {
        return new ProtocolToModelAdapter();
    }
}
