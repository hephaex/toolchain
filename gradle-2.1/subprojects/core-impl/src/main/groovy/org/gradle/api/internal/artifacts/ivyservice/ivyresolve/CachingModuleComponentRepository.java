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
package org.gradle.api.internal.artifacts.ivyservice.ivyresolve;

import org.gradle.api.Transformer;
import org.gradle.api.artifacts.ArtifactIdentifier;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ModuleVersionSelector;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.ModuleMetadataProcessor;
import org.gradle.api.internal.artifacts.configurations.dynamicversion.CachePolicy;
import org.gradle.api.internal.artifacts.ivyservice.BuildableArtifactResolveResult;
import org.gradle.api.internal.artifacts.ivyservice.BuildableArtifactSetResolveResult;
import org.gradle.api.internal.artifacts.ivyservice.ComponentUsage;
import org.gradle.api.internal.artifacts.ivyservice.dynamicversions.ModuleVersionsCache;
import org.gradle.api.internal.artifacts.ivyservice.modulecache.ModuleArtifactsCache;
import org.gradle.api.internal.artifacts.ivyservice.modulecache.ModuleMetaDataCache;
import org.gradle.api.internal.artifacts.metadata.*;
import org.gradle.api.internal.component.ArtifactType;
import org.gradle.internal.resource.cached.CachedArtifact;
import org.gradle.internal.resource.cached.CachedArtifactIndex;
import org.gradle.internal.resource.cached.ivy.ArtifactAtRepositoryKey;
import org.gradle.util.BuildCommencedTimeProvider;
import org.gradle.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.util.Set;

public class CachingModuleComponentRepository implements ModuleComponentRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachingModuleComponentRepository.class);

    private final ModuleVersionsCache moduleVersionsCache;
    private final ModuleMetaDataCache moduleMetaDataCache;
    private final ModuleArtifactsCache moduleArtifactsCache;
    private final CachedArtifactIndex artifactAtRepositoryCachedResolutionIndex;

    private final CachePolicy cachePolicy;

    private final ModuleComponentRepository delegate;
    private final BuildCommencedTimeProvider timeProvider;
    private final ModuleMetadataProcessor metadataProcessor;
    private LocateInCacheRepositoryAccess locateInCacheRepositoryAccess = new LocateInCacheRepositoryAccess();
    private ResolveAndCacheRepositoryAccess resolveAndCacheRepositoryAccess = new ResolveAndCacheRepositoryAccess();

    public CachingModuleComponentRepository(ModuleComponentRepository delegate, ModuleVersionsCache moduleVersionsCache, ModuleMetaDataCache moduleMetaDataCache,
                                            ModuleArtifactsCache moduleArtifactsCache, CachedArtifactIndex artifactAtRepositoryCachedResolutionIndex,
                                            CachePolicy cachePolicy, BuildCommencedTimeProvider timeProvider,
                                            ModuleMetadataProcessor metadataProcessor) {
        this.delegate = delegate;
        this.moduleMetaDataCache = moduleMetaDataCache;
        this.moduleVersionsCache = moduleVersionsCache;
        this.moduleArtifactsCache = moduleArtifactsCache;
        this.artifactAtRepositoryCachedResolutionIndex = artifactAtRepositoryCachedResolutionIndex;
        this.timeProvider = timeProvider;
        this.cachePolicy = cachePolicy;
        this.metadataProcessor = metadataProcessor;
    }

    public String getId() {
        return delegate.getId();
    }

    public String getName() {
        return delegate.getName();
    }

    @Override
    public String toString() {
        return "Caching " + delegate.toString();
    }

    public ModuleComponentRepositoryAccess getLocalAccess() {
        return locateInCacheRepositoryAccess;
    }

    public ModuleComponentRepositoryAccess getRemoteAccess() {
        return resolveAndCacheRepositoryAccess;
    }

    private DefaultModuleIdentifier getCacheKey(ModuleVersionSelector requested) {
        return new DefaultModuleIdentifier(requested.getGroup(), requested.getName());
    }

    private class LocateInCacheRepositoryAccess implements ModuleComponentRepositoryAccess {

        public void listModuleVersions(DependencyMetaData dependency, BuildableModuleVersionSelectionResolveResult result) {
            // First try to determine the artifacts in-memory (e.g using the metadata): don't use the cache in this case
            delegate.getLocalAccess().listModuleVersions(dependency, result);
            if (result.hasResult()) {
                return;
            }

            listModuleVersionsFromCache(dependency, result);
        }

        private void listModuleVersionsFromCache(DependencyMetaData dependency, BuildableModuleVersionSelectionResolveResult result) {
            ModuleVersionSelector requested = dependency.getRequested();
            final ModuleIdentifier moduleId = getCacheKey(requested);
            ModuleVersionsCache.CachedModuleVersionList cachedModuleVersionList = moduleVersionsCache.getCachedModuleResolution(delegate, moduleId);
            if (cachedModuleVersionList != null) {
                ModuleVersionListing versionList = cachedModuleVersionList.getModuleVersions();
                Set<ModuleVersionIdentifier> versions = CollectionUtils.collect(versionList.getVersions(), new Transformer<ModuleVersionIdentifier, Versioned>() {
                    public ModuleVersionIdentifier transform(Versioned original) {
                        return new DefaultModuleVersionIdentifier(moduleId, original.getVersion());
                    }
                });
                if (cachePolicy.mustRefreshVersionList(moduleId, versions, cachedModuleVersionList.getAgeMillis())) {
                    LOGGER.debug("Version listing in dynamic revision cache is expired: will perform fresh resolve of '{}' in '{}'", requested, delegate.getName());
                } else {
                    if (cachedModuleVersionList.getAgeMillis() == 0) {
                        // Verified since the start of this build, assume still missing
                        result.listed(versionList);
                    } else {
                        // Was missing last time we checked
                        result.probablyListed(versionList);
                    }
                }
            }
        }

        public void resolveComponentMetaData(DependencyMetaData dependency, ModuleComponentIdentifier moduleComponentIdentifier, BuildableModuleVersionMetaDataResolveResult result) {
            // First try to determine the artifacts in-memory (e.g using the metadata): don't use the cache in this case
            delegate.getLocalAccess().resolveComponentMetaData(dependency, moduleComponentIdentifier, result);
            if (result.hasResult()) {
                return;
            }

            resolveComponentMetaDataFromCache(dependency, moduleComponentIdentifier, result);
        }

        private void resolveComponentMetaDataFromCache(DependencyMetaData dependency, ModuleComponentIdentifier moduleComponentIdentifier, BuildableModuleVersionMetaDataResolveResult result) {
            ModuleMetaDataCache.CachedMetaData cachedMetaData = moduleMetaDataCache.getCachedModuleDescriptor(delegate, moduleComponentIdentifier);
            if (cachedMetaData == null) {
                return;
            }
            if (cachedMetaData.isMissing()) {
                if (cachePolicy.mustRefreshMissingModule(moduleComponentIdentifier, cachedMetaData.getAgeMillis())) {
                    LOGGER.debug("Cached meta-data for missing module is expired: will perform fresh resolve of '{}' in '{}'", moduleComponentIdentifier, delegate.getName());
                    return;
                }
                LOGGER.debug("Detected non-existence of module '{}' in resolver cache '{}'", moduleComponentIdentifier, delegate.getName());
                if (cachedMetaData.getAgeMillis() == 0) {
                    // Verified since the start of this build, assume still missing
                    result.missing();
                } else {
                    // Was missing last time we checked
                    result.probablyMissing();
                }
                return;
            }
            MutableModuleVersionMetaData metaData = cachedMetaData.getMetaData();
            metadataProcessor.process(metaData);
            if (dependency.isChanging() || metaData.isChanging()) {
                if (cachePolicy.mustRefreshChangingModule(moduleComponentIdentifier, cachedMetaData.getModuleVersion(), cachedMetaData.getAgeMillis())) {
                    LOGGER.debug("Cached meta-data for changing module is expired: will perform fresh resolve of '{}' in '{}'", moduleComponentIdentifier, delegate.getName());
                    return;
                }
                LOGGER.debug("Found cached version of changing module '{}' in '{}'", moduleComponentIdentifier, delegate.getName());
            } else {
                if (cachePolicy.mustRefreshModule(moduleComponentIdentifier, cachedMetaData.getModuleVersion(), cachedMetaData.getAgeMillis())) {
                    LOGGER.debug("Cached meta-data for module must be refreshed: will perform fresh resolve of '{}' in '{}'", moduleComponentIdentifier, delegate.getName());
                    return;
                }
            }

            LOGGER.debug("Using cached module metadata for module '{}' in '{}'", moduleComponentIdentifier, delegate.getName());
            result.resolved(metaData, new CachingModuleSource(cachedMetaData.getDescriptorHash(), metaData.isChanging(), cachedMetaData.getModuleSource()));
        }

        public void resolveModuleArtifacts(ComponentMetaData component, ArtifactType artifactType, BuildableArtifactSetResolveResult result) {
            final CachingModuleSource cachedModuleSource = (CachingModuleSource) component.getSource();

            // First try to determine the artifacts in-memory (e.g using the metadata): don't use the cache in this case
            delegate.getLocalAccess().resolveModuleArtifacts(component.withSource(cachedModuleSource.getDelegate()), artifactType, result);
            if (result.hasResult()) {
                return;
            }

            resolveModuleArtifactsFromCache(cacheKey(artifactType), component, result, cachedModuleSource);
        }

        public void resolveModuleArtifacts(ComponentMetaData component, ComponentUsage componentUsage, BuildableArtifactSetResolveResult result) {
            final CachingModuleSource cachedModuleSource = (CachingModuleSource) component.getSource();

            // First try to determine the artifacts in-memory (e.g using the metadata): don't use the cache in this case
            delegate.getLocalAccess().resolveModuleArtifacts(component.withSource(cachedModuleSource.getDelegate()), componentUsage, result);
            if (result.hasResult()) {
                return;
            }

            resolveModuleArtifactsFromCache(cacheKey(componentUsage), component, result, cachedModuleSource);
        }

        private void resolveModuleArtifactsFromCache(String contextId, ComponentMetaData component, BuildableArtifactSetResolveResult result, CachingModuleSource cachedModuleSource) {
            ModuleArtifactsCache.CachedArtifacts cachedModuleArtifacts = moduleArtifactsCache.getCachedArtifacts(delegate, component.getId(), contextId);
            BigInteger moduleDescriptorHash = cachedModuleSource.getDescriptorHash();

            if (cachedModuleArtifacts != null) {
                if (!cachePolicy.mustRefreshModuleArtifacts(component.getId(), null, cachedModuleArtifacts.getAgeMillis(),
                        cachedModuleSource.isChangingModule(), moduleDescriptorHash.equals(cachedModuleArtifacts.getDescriptorHash()))) {
                    Set<ModuleVersionArtifactMetaData> artifactMetaDataSet = CollectionUtils.collect(cachedModuleArtifacts.getArtifacts(), new ArtifactIdToMetaData());
                    result.resolved(artifactMetaDataSet);
                    return;
                }

                LOGGER.debug("Artifact listing has expired: will perform fresh resolve of '{}' for '{}' in '{}'", contextId, component.getId(), delegate.getName());
            }
        }

        public void resolveArtifact(ComponentArtifactMetaData artifact, ModuleSource moduleSource, BuildableArtifactResolveResult result) {
            final CachingModuleSource cachedModuleSource = (CachingModuleSource) moduleSource;

            // First try to determine the artifacts in-memory (e.g using the metadata): don't use the cache in this case
            delegate.getLocalAccess().resolveArtifact(artifact, cachedModuleSource.getDelegate(), result);
            if (result.hasResult()) {
                return;
            }

            resolveArtifactFromCache(artifact, cachedModuleSource, result);
        }

        private void resolveArtifactFromCache(ComponentArtifactMetaData artifact, CachingModuleSource moduleSource, BuildableArtifactResolveResult result) {
            CachedArtifact cached = artifactAtRepositoryCachedResolutionIndex.lookup(artifactCacheKey(artifact));
            final BigInteger descriptorHash = moduleSource.getDescriptorHash();
            if (cached != null) {
                long age = timeProvider.getCurrentTime() - cached.getCachedAt();
                final boolean isChangingModule = moduleSource.isChangingModule();
                ArtifactIdentifier artifactIdentifier = ((ModuleVersionArtifactMetaData) artifact).toArtifactIdentifier();
                if (cached.isMissing()) {
                    if (!cachePolicy.mustRefreshArtifact(artifactIdentifier, null, age, isChangingModule, descriptorHash.equals(cached.getDescriptorHash()))) {
                        LOGGER.debug("Detected non-existence of artifact '{}' in resolver cache", artifact);
                        result.notFound(artifact.getId());
                    }
                } else {
                    File cachedArtifactFile = cached.getCachedFile();
                    if (!cachePolicy.mustRefreshArtifact(artifactIdentifier, cachedArtifactFile, age, isChangingModule, descriptorHash.equals(cached.getDescriptorHash()))) {
                        LOGGER.debug("Found artifact '{}' in resolver cache: {}", artifact, cachedArtifactFile);
                        result.resolved(cachedArtifactFile);
                    }
                }
            }
        }
    }

    private class ResolveAndCacheRepositoryAccess implements ModuleComponentRepositoryAccess {
        public void listModuleVersions(DependencyMetaData dependency, BuildableModuleVersionSelectionResolveResult result) {
            delegate.getRemoteAccess().listModuleVersions(dependency, result);
            switch (result.getState()) {
                case Listed:
                    ModuleIdentifier moduleId = getCacheKey(dependency.getRequested());
                    ModuleVersionListing versionList = result.getVersions();
                    moduleVersionsCache.cacheModuleVersionList(delegate, moduleId, versionList);
                    break;
                case Failed:
                    break;
                default:
                    throw new IllegalStateException("Unexpected state on listModuleVersions: " + result.getState());
            }
        }

        public void resolveComponentMetaData(DependencyMetaData dependency, ModuleComponentIdentifier moduleComponentIdentifier, BuildableModuleVersionMetaDataResolveResult result) {
            DependencyMetaData forced = dependency.withChanging();
            delegate.getRemoteAccess().resolveComponentMetaData(forced, moduleComponentIdentifier, result);
            switch (result.getState()) {
                case Missing:
                    moduleMetaDataCache.cacheMissing(delegate, moduleComponentIdentifier);
                    break;
                case Resolved:
                    MutableModuleVersionMetaData metaData = result.getMetaData();
                    ModuleSource moduleSource = result.getModuleSource();
                    ModuleMetaDataCache.CachedMetaData cachedMetaData = moduleMetaDataCache.cacheMetaData(delegate, metaData, moduleSource);
                    metadataProcessor.process(metaData);
                    result.setModuleSource(new CachingModuleSource(cachedMetaData.getDescriptorHash(), dependency.isChanging() || metaData.isChanging(), moduleSource));
                    break;
                case Failed:
                    break;
                default:
                    throw new IllegalStateException("Unexpected resolve state: " + result.getState());
            }
        }

        public void resolveModuleArtifacts(ComponentMetaData component, ArtifactType artifactType, BuildableArtifactSetResolveResult result) {
            final CachingModuleSource moduleSource = (CachingModuleSource) component.getSource();
            delegate.getRemoteAccess().resolveModuleArtifacts(component.withSource(moduleSource.getDelegate()), artifactType, result);

            maybeCache(component, result, moduleSource, cacheKey(artifactType));
        }

        public void resolveModuleArtifacts(ComponentMetaData component, ComponentUsage componentUsage, BuildableArtifactSetResolveResult result) {
            final CachingModuleSource moduleSource = (CachingModuleSource) component.getSource();
            delegate.getRemoteAccess().resolveModuleArtifacts(component.withSource(moduleSource.getDelegate()), componentUsage, result);

            maybeCache(component, result, moduleSource, cacheKey(componentUsage));
        }

        private void maybeCache(ComponentMetaData component, BuildableArtifactSetResolveResult result, CachingModuleSource moduleSource, String contextId) {
            if (result.getFailure() == null) {
                Set<ModuleVersionArtifactIdentifier> artifactIdentifierSet = CollectionUtils.collect(result.getArtifacts(), new ArtifactMetaDataToId());
                moduleArtifactsCache.cacheArtifacts(delegate, component.getId(), contextId, moduleSource.getDescriptorHash(), artifactIdentifierSet);
            }
        }

        public void resolveArtifact(ComponentArtifactMetaData artifact, ModuleSource moduleSource, BuildableArtifactResolveResult result) {
            final CachingModuleSource cachingModuleSource = (CachingModuleSource) moduleSource;

            delegate.getRemoteAccess().resolveArtifact(artifact, cachingModuleSource.getDelegate(), result);
            LOGGER.debug("Downloaded artifact '{}' from resolver: {}", artifact, delegate.getName());

            ArtifactResolveException failure = result.getFailure();
            if (failure == null) {
                artifactAtRepositoryCachedResolutionIndex.store(artifactCacheKey(artifact), result.getFile(), cachingModuleSource.getDescriptorHash());
            } else if (failure instanceof ArtifactNotFoundException) {
                artifactAtRepositoryCachedResolutionIndex.storeMissing(artifactCacheKey(artifact), cachingModuleSource.getDescriptorHash());
            }
        }
    }

    private String cacheKey(ArtifactType artifactType) {
        return "artifacts:" + artifactType.name();
    }

    private String cacheKey(ComponentUsage context) {
        return "configuration:" + context.getConfigurationName();
    }

    private ArtifactAtRepositoryKey artifactCacheKey(ComponentArtifactMetaData artifact) {
        // TODO:ADAM - Don't assume this
        ModuleVersionArtifactMetaData moduleVersionArtifactMetaData = (ModuleVersionArtifactMetaData) artifact;
        return new ArtifactAtRepositoryKey(delegate.getId(), moduleVersionArtifactMetaData.getId());
    }

    static class CachingModuleSource implements ModuleSource {
        private final BigInteger descriptorHash;
        private final boolean changingModule;
        private final ModuleSource delegate;

        public CachingModuleSource(BigInteger descriptorHash, boolean changingModule, ModuleSource delegate) {
            this.delegate = delegate;
            this.descriptorHash = descriptorHash;
            this.changingModule = changingModule;
        }

        public BigInteger getDescriptorHash() {
            return descriptorHash;
        }

        public boolean isChangingModule() {
            return changingModule;
        }

        public ModuleSource getDelegate() {
            return delegate;
        }
    }

    static class ArtifactIdToMetaData implements Transformer<ModuleVersionArtifactMetaData, ModuleVersionArtifactIdentifier> {
        public ModuleVersionArtifactMetaData transform(ModuleVersionArtifactIdentifier original) {
            return new DefaultModuleVersionArtifactMetaData(original);
        }
    }

    static class ArtifactMetaDataToId implements Transformer<ModuleVersionArtifactIdentifier, ComponentArtifactMetaData> {
        public ModuleVersionArtifactIdentifier transform(ComponentArtifactMetaData original) {
            return ((ModuleVersionArtifactMetaData)original).getId();
        }
    }
}
