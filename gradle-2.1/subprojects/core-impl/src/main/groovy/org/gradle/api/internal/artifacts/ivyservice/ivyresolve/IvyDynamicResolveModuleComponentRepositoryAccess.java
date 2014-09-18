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
package org.gradle.api.internal.artifacts.ivyservice.ivyresolve;

import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.internal.artifacts.metadata.DependencyMetaData;
import org.gradle.api.internal.artifacts.metadata.MutableModuleVersionMetaData;

import java.util.ArrayList;
import java.util.List;

class IvyDynamicResolveModuleComponentRepositoryAccess extends BaseModuleComponentRepositoryAccess {

    public static ModuleComponentRepository wrap(ModuleComponentRepository delegate) {
        final ModuleComponentRepositoryAccess localAccess = new IvyDynamicResolveModuleComponentRepositoryAccess(delegate.getLocalAccess());
        final ModuleComponentRepositoryAccess remoteAccess = new IvyDynamicResolveModuleComponentRepositoryAccess(delegate.getRemoteAccess());
        return new BaseModuleComponentRepository(delegate) {
            @Override
            public ModuleComponentRepositoryAccess getLocalAccess() {
                return localAccess;
            }

            @Override
            public ModuleComponentRepositoryAccess getRemoteAccess() {
                return remoteAccess;
            }
        };
    }

    IvyDynamicResolveModuleComponentRepositoryAccess(ModuleComponentRepositoryAccess delegate) {
        super(delegate);
    }

    public void resolveComponentMetaData(DependencyMetaData dependency, ModuleComponentIdentifier moduleComponentIdentifier, BuildableModuleVersionMetaDataResolveResult result) {
        super.resolveComponentMetaData(dependency, moduleComponentIdentifier, result);
        if (result.getState() == BuildableModuleVersionMetaDataResolveResult.State.Resolved) {
            transformDependencies(result);
        }
    }

    private void transformDependencies(BuildableModuleVersionMetaDataResolveResult result) {
        MutableModuleVersionMetaData metaData = result.getMetaData();
        List<DependencyMetaData> transformed = new ArrayList<DependencyMetaData>();
        for (DependencyMetaData dependency : metaData.getDependencies()) {
            transformed.add(dependency.withRequestedVersion(dependency.getDescriptor().getDynamicConstraintDependencyRevisionId().getRevision()));
        }
        metaData.setDependencies(transformed);
    }
}
