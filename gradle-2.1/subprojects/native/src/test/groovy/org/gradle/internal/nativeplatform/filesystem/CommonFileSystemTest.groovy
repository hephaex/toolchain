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
package org.gradle.internal.nativeplatform.filesystem

import org.gradle.internal.nativeplatform.services.NativeServices
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.junit.Rule
import spock.lang.Specification

class CommonFileSystemTest extends Specification {
    @Rule TestNameTestDirectoryProvider tmpDir

    def fs = NativeServices.instance.get(FileSystem)

    def "unix permissions cannot be read on non existing file"() {
        def file = tmpDir.file("someFile")

        when:
        fs.getUnixMode(file)

        then:
        FileException e = thrown()
        e.message == "Could not get file mode for '$file'."
    }

    def "unix permissions cannot be set on non existing file"() {
        def file = tmpDir.file("someFile")

        when:
        fs.chmod(file, 0644)

        then:
        FileException e = thrown()
        e.message == "Could not set file mode 644 on '$file'."
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "unix permissions on files can be changed and read"() {
        def f = tmpDir.createFile("someFile\u03B1.txt")

        when:
        fs.chmod(f, mode)

        then:
        fs.getUnixMode(f) == mode
        f.mode == mode

        where:
        mode << [0644, 0600, 0751]
    }

    @Requires(TestPrecondition.FILE_PERMISSIONS)
    def "unix permissions on directories can be changed and read"() {
        def d = tmpDir.createDir("someDir\u03B1")

        when:
        fs.chmod(d, mode)

        then:
        fs.getUnixMode(d) == mode
        d.mode == mode

        where:
        mode << [0755, 0700, 0722]
    }

    @Requires(TestPrecondition.NO_FILE_PERMISSIONS)
    def "unix permissions have default values on unsupported platforms"() {
        expect:
        fs.getUnixMode(tmpDir.createFile("someFile")) == FileSystem.DEFAULT_FILE_MODE
        fs.getUnixMode(tmpDir.createDir("someDir")) == FileSystem.DEFAULT_DIR_MODE
    }

    @Requires(TestPrecondition.NO_FILE_PERMISSIONS)
    def "setting unix permissions does nothing on unsupported platforms"() {
        expect:
        fs.chmod(tmpDir.createFile("someFile"), 0644)
    }

    @Requires(TestPrecondition.SYMLINKS)
    def "can create symlink on platforms that support symlinks"() {
        def target = tmpDir.createFile("target.txt")
        def link = tmpDir.file("link.txt")

        when:
        fs.createSymbolicLink(link, target)

        then:
        link.exists()
        link.readLink() == target.absolutePath
    }

    @Requires(TestPrecondition.NO_SYMLINKS)
    def "cannot create symlinks on platforms that do not support symlinks"() {
        def target = tmpDir.createFile("target.txt")
        def link = tmpDir.file("link.txt")

        when:
        fs.createSymbolicLink(link, target)

        then:
        thrown(FileException)
    }
}
