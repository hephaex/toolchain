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

package org.gradle.plugin.use.resolve.service.internal;

import org.gradle.StartParameter;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ResolveException;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.internal.artifacts.DependencyResolutionServices;
import org.gradle.api.internal.artifacts.configurations.ConfigurationContainerInternal;
import org.gradle.api.internal.artifacts.configurations.ConfigurationInternal;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.StartParameterResolutionOverride;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.LatestVersionMatcher;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.SubVersionMatcher;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionMatcher;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionRangeMatcher;
import org.gradle.api.internal.initialization.ClassLoaderScope;
import org.gradle.api.specs.Specs;
import org.gradle.internal.Factories;
import org.gradle.internal.Factory;
import org.gradle.internal.classpath.ClassPath;
import org.gradle.internal.classpath.DefaultClassPath;
import org.gradle.internal.exceptions.Contextual;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.plugin.use.internal.InvalidPluginRequestException;
import org.gradle.plugin.use.internal.PluginRequest;
import org.gradle.plugin.use.resolve.internal.*;

import java.io.File;
import java.util.Set;

public class PluginResolutionServiceResolver implements PluginResolver {

    public static final String OVERRIDE_URL_PROPERTY = PluginResolutionServiceResolver.class.getName() + ".repo.override";
    private static final String DEFAULT_API_URL = "https://plugins.gradle.org/api/gradle";

    private static final VersionMatcher RANGE_MATCHER = new VersionRangeMatcher(null);
    private static final VersionMatcher SUB_MATCHER = new SubVersionMatcher(null);
    private static final VersionMatcher LATEST_MATCHER = new LatestVersionMatcher();

    private final PluginResolutionServiceClient portalClient;
    private final Instantiator instantiator;
    private final StartParameter startParameter;
    private final Factory<DependencyResolutionServices> dependencyResolutionServicesFactory;
    private final ClassLoaderScope parentScope;

    public PluginResolutionServiceResolver(
            PluginResolutionServiceClient portalClient,
            Instantiator instantiator,
            StartParameter startParameter,
            ClassLoaderScope parentScope, Factory<DependencyResolutionServices> dependencyResolutionServicesFactory
    ) {
        this.portalClient = portalClient;
        this.instantiator = instantiator;
        this.startParameter = startParameter;
        this.parentScope = parentScope;
        this.dependencyResolutionServicesFactory = dependencyResolutionServicesFactory;
    }

    private static String getUrl() {
        return System.getProperty(OVERRIDE_URL_PROPERTY, DEFAULT_API_URL);
    }

    public void resolve(PluginRequest pluginRequest, PluginResolutionResult result) throws InvalidPluginRequestException {
        if (pluginRequest.getVersion() == null) {
            result.notFound(getDescription(), "plugin dependency must include a version number for this source");
        } else {
            if (pluginRequest.getVersion().endsWith("-SNAPSHOT")) {
                result.notFound(getDescription(), "snapshot plugin versions are not supported");
            } else if (isDynamicVersion(pluginRequest.getVersion())) {
                result.notFound(getDescription(), "dynamic plugin versions are not supported");
            } else {
                HttpPluginResolutionServiceClient.Response<PluginUseMetaData> response = portalClient.queryPluginMetadata(getUrl(), startParameter.isRefreshDependencies(), pluginRequest);
                if (response.isError()) {
                    ErrorResponse errorResponse = response.getErrorResponse();
                    if (response.getStatusCode() == 404) {
                        result.notFound(getDescription(), errorResponse.message);
                    } else {
                        throw new GradleException(String.format("Plugin resolution service returned HTTP %d with message '%s' (url: %s)", response.getStatusCode(), errorResponse.message, response.getUrl()));
                    }
                } else {
                    PluginUseMetaData metaData = response.getResponse();
                    if (metaData.legacy) {
                        handleLegacy(metaData, result);
                    } else {
                        ClassPath classPath = resolvePluginDependencies(metaData);
                        PluginResolution resolution = new ClassPathPluginResolution(instantiator, pluginRequest.getId(), parentScope, Factories.constant(classPath));
                        result.found(getDescription(), resolution);
                    }
                }
            }
        }
    }

    private void handleLegacy(final PluginUseMetaData metadata, PluginResolutionResult result) {
        result.foundLegacy(getDescription(), new Action<LegacyPluginResolveContext>() {
            public void execute(LegacyPluginResolveContext context) {
                context.add(metadata.id, metadata.implementation.get("repo"), metadata.implementation.get("gav"));
            }
        });
    }

    private boolean isDynamicVersion(String version) {
        return RANGE_MATCHER.canHandle(version) || SUB_MATCHER.canHandle(version) || LATEST_MATCHER.canHandle(version);
    }

    private ClassPath resolvePluginDependencies(final PluginUseMetaData metadata) {
        DependencyResolutionServices resolution = dependencyResolutionServicesFactory.create();

        RepositoryHandler repositories = resolution.getResolveRepositoryHandler();
        final String repoUrl = metadata.implementation.get("repo");
        repositories.maven(new Action<MavenArtifactRepository>() {
            public void execute(MavenArtifactRepository mavenArtifactRepository) {
                mavenArtifactRepository.setUrl(repoUrl);
            }
        });

        Dependency dependency = resolution.getDependencyHandler().create(metadata.implementation.get("gav"));

        ConfigurationContainerInternal configurations = resolution.getConfigurationContainer();
        ConfigurationInternal configuration = configurations.detachedConfiguration(dependency);

        // honor start parameters when resolving plugin dependencies
        StartParameterResolutionOverride resolutionOverride = new StartParameterResolutionOverride(startParameter);
        resolutionOverride.addResolutionRules(configuration.getResolutionStrategy().getResolutionRules());

        try {
            Set<File> files = configuration.getResolvedConfiguration().getFiles(Specs.satisfyAll());
            return new DefaultClassPath(files);
        } catch (ResolveException e) {
            throw new DependencyResolutionException("Failed to resolve all plugin dependencies from " + repoUrl, e.getCause());
        }
    }

    public String getDescription() {
        return "Gradle Central Plugin Repository";
    }

    @Contextual
    public static class DependencyResolutionException extends GradleException {
        public DependencyResolutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
