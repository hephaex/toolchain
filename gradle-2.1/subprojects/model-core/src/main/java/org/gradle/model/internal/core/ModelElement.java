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

package org.gradle.model.internal.core;

import org.gradle.model.internal.core.rule.describe.ModelRuleDescriptor;

public class ModelElement {

    private final ModelPath path;
    private final ModelPromise promise;
    private final ModelAdapter adapter;
    private final ModelRuleDescriptor creatorDescriptor;

    public ModelElement(ModelPath path, ModelPromise promise, ModelAdapter adapter, ModelRuleDescriptor creatorDescriptor) {
        this.path = path;
        this.promise = promise;
        this.adapter = adapter;
        this.creatorDescriptor = creatorDescriptor;
    }

    public ModelPath getPath() {
        return path;
    }

    public ModelAdapter getAdapter() {
        return adapter;
    }

    public ModelPromise getPromise() {
        return promise;
    }

    public ModelRuleDescriptor getCreatorDescriptor() {
        return creatorDescriptor;
    }

    @Override
    public String toString() {
        return "ModelElement{path=" + path + ", promise=" + promise + ", adapter=" + adapter + ", creatorDescriptor=" + creatorDescriptor + '}';
    }
}
