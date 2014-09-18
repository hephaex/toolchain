/*
 * Copyright 2009 the original author or authors.
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

package org.gradle.api.internal.file

import org.gradle.internal.typeconversion.UnsupportedNotationException
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.junit.Rule
import spock.lang.Issue
import spock.lang.Specification

import static org.gradle.util.TextUtil.toPlatformLineSeparators

class FileOrUriNotationParserTest extends Specification {

    @Rule public TestNameTestDirectoryProvider folder = new TestNameTestDirectoryProvider();

    def "with File returns this File"() {
        setup:
        def testFile = folder.createFile("test1")
        when:
        def object = parse(testFile)
        then:
        object instanceof File
        testFile == object
    }

    def "with file path as String"() {
        setup:
        def testFile = folder.createFile("test1")
        when:
        def object = parse(testFile.getAbsolutePath())
        then:
        object instanceof File
        testFile.getAbsolutePath() == object.getAbsolutePath()
    }

    def "with file URI"() {
        setup:
        def testFileURI = folder.createFile("test1").toURI()
        when:
        def object = parse(testFileURI)
        then:
        object instanceof File
        object.toURI() == testFileURI
    }

    def "with URI as CharSequence"() {
        setup:
        def uriString = folder.createFile("test1").toURI().toString()
        when:
        def object = parse(uriString)
        then:
        object instanceof File
        object.toURI().toString() == uriString
    }

    def "with URL"() {
        setup:
        def testFileURL = folder.createFile("test1").toURI().toURL()
        when:
        def object = parse(testFileURL)
        then:
        object instanceof File
        object.toURI().toURL() == testFileURL
    }

    def "with non File URI URI instance is returned"() {
        setup:
        def unsupportedURI = URI.create("http://gradle.org")
        when:
        def parsed = parse(unsupportedURI)
        then:
        parsed instanceof URI
    }

    def "with non File URI String URI is returned"() {
        setup:
        def unsupportedURIString = "http://gradle.org"
        when:
        def parsed = parse(unsupportedURIString)
        then:
        parsed instanceof URI
    }

    @Issue("GRADLE-2072")
    def "parsing unknown types causes UnsupportedNotationException"() {
        when:
        parse(12)

        then:
        UnsupportedNotationException e = thrown()
        e.message == toPlatformLineSeparators("""Cannot convert the provided notation to a File or URI: 12.
The following types/formats are supported:
  - A String or CharSequence path, e.g 'src/main/java' or '/usr/include'
  - A String or CharSequence URI, e.g 'file:/usr/include'
  - A File instance.
  - A URI or URL instance.""")
    }

    def parse(def value) {
        return FileOrUriNotationParser.create(TestFiles.fileSystem()).parseNotation(value)
    }
}
