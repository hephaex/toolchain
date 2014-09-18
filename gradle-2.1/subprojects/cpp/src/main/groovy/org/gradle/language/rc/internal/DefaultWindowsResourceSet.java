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
package org.gradle.language.rc.internal;

import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.language.base.FunctionalSourceSet;
import org.gradle.language.internal.AbstractHeaderExportingSourceSet;
import org.gradle.language.rc.WindowsResourceSet;

public class DefaultWindowsResourceSet extends AbstractHeaderExportingSourceSet implements WindowsResourceSet {
    public DefaultWindowsResourceSet(String name, FunctionalSourceSet parent, FileResolver fileResolver) {
        super(name, parent, fileResolver, "windows resources", new DefaultSourceDirectorySet("source", fileResolver));
     }
}
