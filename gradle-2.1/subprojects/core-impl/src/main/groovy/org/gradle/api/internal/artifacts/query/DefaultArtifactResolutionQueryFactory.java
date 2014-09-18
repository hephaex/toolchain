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
package org.gradle.api.internal.artifacts.query;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.query.ArtifactResolutionQuery;
import org.gradle.api.internal.artifacts.ModuleMetadataProcessor;
import org.gradle.api.internal.artifacts.configurations.ConfigurationContainerInternal;
import org.gradle.api.internal.artifacts.ivyservice.CacheLockingManager;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ResolveIvyFactory;
import org.gradle.api.internal.component.ComponentTypeRegistry;

public class DefaultArtifactResolutionQueryFactory implements ArtifactResolutionQueryFactory {
    private final ConfigurationContainerInternal configurationContainer;
    private final RepositoryHandler repositoryHandler;
    private final ResolveIvyFactory ivyFactory;
    private final ModuleMetadataProcessor metadataProcessor;
    private final CacheLockingManager cacheLockingManager;
    private final ComponentTypeRegistry componentTypeRegistry;

    public DefaultArtifactResolutionQueryFactory(ConfigurationContainerInternal configurationContainer, RepositoryHandler repositoryHandler,
                                                 ResolveIvyFactory ivyFactory, ModuleMetadataProcessor metadataProcessor,
                                                 CacheLockingManager cacheLockingManager, ComponentTypeRegistry componentTypeRegistry) {
        this.configurationContainer = configurationContainer;
        this.repositoryHandler = repositoryHandler;
        this.ivyFactory = ivyFactory;
        this.metadataProcessor = metadataProcessor;
        this.cacheLockingManager = cacheLockingManager;
        this.componentTypeRegistry = componentTypeRegistry;
    }

    public ArtifactResolutionQuery createArtifactResolutionQuery() {
        return new DefaultArtifactResolutionQuery(configurationContainer, repositoryHandler, ivyFactory, metadataProcessor, cacheLockingManager, componentTypeRegistry);
    }
}
