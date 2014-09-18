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

package org.gradle.internal

import org.apache.commons.lang.RandomStringUtils
import org.gradle.api.GradleException
import spock.lang.Specification

import static FileUtils.toSafeFileName
import static FileUtils.assertInWindowsPathLengthLimitation


class FileUtilsTest extends Specification {
    def "toSafeFileName encodes unsupported characters"() {
        expect:
        toSafeFileName(input) == output
        where:
        input         | output
        'Test_$1-2.3' | 'Test_$1-2.3'
        'with space'  | 'with#20space'
        'with #'      | 'with#20#23'
        'with /'      | 'with#20#2f'
        'with \\'     | 'with#20#5c'
        'with / \\ #' | 'with#20#2f#20#5c#20#23'
    }

    def "assertInWindowsPathLengthLimitation throws exception when path limit exceeded"(){
        when:
        File inputFile = new File(RandomStringUtils.randomAlphanumeric(10))
        then:
        inputFile == assertInWindowsPathLengthLimitation(inputFile);

        when:
        inputFile = new File(RandomStringUtils.randomAlphanumeric(261))
        assertInWindowsPathLengthLimitation(inputFile);
        then:
        def e = thrown(GradleException);
        e.message.contains("exceeds windows path limitation of 260 character.")
    }
}
