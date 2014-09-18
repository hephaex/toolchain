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
package org.gradle.api.internal.artifacts.ivyservice.ivyresolve;

import org.gradle.api.internal.artifacts.ivyservice.DefaultResourceAwareResolveResult;
import org.gradle.api.internal.artifacts.ivyservice.ModuleVersionResolveException;

import java.util.Collection;

public class DefaultBuildableModuleVersionSelectionResolveResult extends DefaultResourceAwareResolveResult implements BuildableModuleVersionSelectionResolveResult {
    private State state = State.Unknown;
    private ModuleVersionResolveException failure;
    private ModuleVersionListing versions;

    private void reset(State state) {
        this.state = state;
        versions = null;
        failure = null;
    }

    public State getState() {
        return state;
    }

    public boolean hasResult() {
        return state != State.Unknown;
    }

    public ModuleVersionListing getVersions() throws ModuleVersionResolveException {
        assertHasResult();
        return versions;
    }

    public ModuleVersionResolveException getFailure() {
        assertHasResult();
        return failure;
    }

    public void listed(ModuleVersionListing versions) {
        reset(State.Listed);
        this.versions = versions;
    }

    public void listed(Collection<String> versions) {
        DefaultModuleVersionListing listing = new DefaultModuleVersionListing();
        for (String version : versions) {
            listing.add(version);
        }
        listed(listing);
    }

    public void probablyListed(ModuleVersionListing versions) {
        reset(State.ProbablyListed);
        this.versions = versions;
    }

    public void failed(ModuleVersionResolveException failure) {
        reset(State.Failed);
        this.failure = failure;
    }

    private void assertHasResult() {
        if (!hasResult()) {
            throw new IllegalStateException("No result has been specified.");
        }
    }
}
