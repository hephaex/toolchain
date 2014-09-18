/*
 * Copyright 2012 the original author or authors.
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
package org.gradle.api.internal.file;

import org.gradle.internal.nativeplatform.filesystem.FileSystem;
import org.gradle.internal.nativeplatform.services.NativeServices;

import java.io.File;

public class TestFiles {
    private static final FileSystem FILE_SYSTEM = NativeServices.getInstance().get(FileSystem.class);
    private static final DefaultFileLookup FILE_LOOKUP = new DefaultFileLookup(FILE_SYSTEM);

    public static FileLookup fileLookup() {
        return FILE_LOOKUP;
    }

    public static FileSystem fileSystem() {
        return FILE_SYSTEM;
    }

    /**
     * Returns a resolver with no base directory.
     */
    public static FileResolver resolver() {
        return FILE_LOOKUP.getFileResolver();
    }

    /**
     * Returns a resolver with the given base directory.
     */
    public static FileResolver resolver(File baseDir) {
        return FILE_LOOKUP.getFileResolver(baseDir);
    }
}
