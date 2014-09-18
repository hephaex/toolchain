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

package org.gradle.nativebinaries.toolchain.internal;

import org.gradle.api.GradleException;
import org.gradle.internal.text.TreeFormatter;
import org.gradle.language.base.internal.compile.CompileSpec;
import org.gradle.language.base.internal.compile.Compiler;
import org.gradle.util.TreeVisitor;

public class UnavailablePlatformToolChain implements PlatformToolChain {
    private final ToolSearchResult failure;

    public UnavailablePlatformToolChain(ToolSearchResult failure) {
        this.failure = failure;
    }

    public boolean isAvailable() {
        return false;
    }

    public void explain(TreeVisitor<? super String> visitor) {
        failure.explain(visitor);
    }

    private RuntimeException failure() {
        TreeFormatter formatter = new TreeFormatter();
        this.explain(formatter);
        return new GradleException(formatter.toString());
    }

    public <T extends CompileSpec> Compiler<T> newCompiler(T spec) {
        throw failure();
    }
}
