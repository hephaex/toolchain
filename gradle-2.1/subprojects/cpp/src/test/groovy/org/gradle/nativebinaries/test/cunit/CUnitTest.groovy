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
package org.gradle.nativebinaries.test.cunit

import org.gradle.model.internal.core.ModelPath
import org.gradle.model.internal.core.ModelType
import org.gradle.nativebinaries.language.c.plugins.CPlugin
import org.gradle.nativebinaries.test.TestSuiteContainer
import org.gradle.nativebinaries.test.cunit.plugins.CUnitPlugin
import org.gradle.util.TestUtil
import spock.lang.Specification

class CUnitTest extends Specification {
    final def project = TestUtil.createRootProject();

    def "check the correct binary type are created for the test suite"() {
        given:
        project.plugins.apply(CPlugin)
        project.libraries.create("main")

        when:
        project.plugins.apply(CUnitPlugin)
        project.evaluate()

        then:
        def binaries = project.modelRegistry.get(ModelPath.path("testSuites"), ModelType.of(TestSuiteContainer)).getByName("mainTest").binaries
        binaries.collect({ it instanceof CUnitTestSuiteBinarySpec }) == [true] * binaries.size()
    }
}
