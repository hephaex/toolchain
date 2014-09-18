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

package org.gradle.initialization

import org.gradle.internal.SystemProperties
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.util.GFileUtils.canonicalise

class LayoutCommandLineConverterTest extends Specification {

    def converter = new LayoutCommandLineConverter()
    @Rule TestNameTestDirectoryProvider temp = new TestNameTestDirectoryProvider()

    def convert(String... args) {
        converter.convert(Arrays.asList(args), new BuildLayoutParameters())
    }

    def "has reasonable defaults"() {
        expect:
        convert().projectDir == canonicalise(SystemProperties.getCurrentDir())
        convert().gradleUserHomeDir == canonicalise(BuildLayoutParameters.DEFAULT_GRADLE_USER_HOME)
        convert().searchUpwards
    }

    def "converts"() {
        expect:
        convert("-p", "foo").projectDir.name == "foo"
        convert("-g", "bar").gradleUserHomeDir.name == "bar"
        !convert("-u").searchUpwards
    }

    def "converts relatively to the target dir"() {
        given:
        def root = temp.createDir('root')
        def target = new BuildLayoutParameters().setProjectDir(root)

        when:
        converter.convert(['-p', 'projectDir', '-g', 'gradleDir'], target)

        then:
        target.gradleUserHomeDir == temp.file("root/gradleDir")
        target.projectDir == temp.file("root/projectDir")
    }

    def "converts absolute paths"() {
        given:
        def root = temp.createDir('root')
        def other = temp.createDir('other')
        def target = new BuildLayoutParameters().setProjectDir(root)

        when:
        converter.convert(['-p', other.file('projectDir').absolutePath, '-g', other.file('gradleDir').absolutePath], target)

        then:
        target.gradleUserHomeDir == temp.file("other/gradleDir")
        target.projectDir == temp.file("other/projectDir")
    }
}
