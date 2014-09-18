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
package org.gradle.language.jvm.internal;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.language.base.FunctionalSourceSet;
import org.gradle.language.base.internal.AbstractLanguageSourceSet;
import org.gradle.language.jvm.ResourceSet;

import javax.inject.Inject;

public class DefaultResourceSet extends AbstractLanguageSourceSet implements ResourceSet {

    @Inject
    public DefaultResourceSet(String name, FunctionalSourceSet parent, FileResolver fileResolver) {
        super(name, parent, "resources", new DefaultSourceDirectorySet("source", fileResolver));
    }

    public DefaultResourceSet(String name, SourceDirectorySet source, FunctionalSourceSet parent) {
        super(name, parent, "resources", source);
    }
}
