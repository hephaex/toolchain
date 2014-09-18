/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.internal.tasks.testing.testng

import org.gradle.api.internal.AsmBackedClassGenerator
import org.gradle.api.internal.ClassGeneratorBackedInstantiator
import org.gradle.api.internal.tasks.testing.filter.DefaultTestFilter
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.reflect.Instantiator
import org.gradle.internal.service.ServiceRegistry
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.TestUtil
import spock.lang.Shared
import spock.lang.Specification

public class TestNGTestFrameworkTest extends Specification {

    @Shared Instantiator instantiator = new ClassGeneratorBackedInstantiator(new AsmBackedClassGenerator(), new DirectInstantiator())

    private project = new ProjectBuilder().build()
    Test testTask = TestUtil.createTask(Test, project)

    void setup() {
        project.ext.sourceCompatibility = "1.7"
    }

    void "initializes"() {
        given:
        project.ext.sourceCompatibility = "1.4"

        when:
        def framework = createFramework()

        then:
        framework.options.annotations == TestNGOptions.JAVADOC_ANNOTATIONS
        framework.testTask == testTask
        framework.detector
    }

    void "initializes for newer java"() {
        expect:
        createFramework().options.annotations == TestNGOptions.JDK_ANNOTATIONS
    }

    void "creates test class processor"() {
        when:
        def framework = createFramework()
        def processor = framework.getProcessorFactory().create(Mock(ServiceRegistry))

        then:
        framework.options.testResources.is(testTask.testSrcDirs)
        processor instanceof TestNGTestClassProcessor
    }

    TestNGTestFramework createFramework() {
        new TestNGTestFramework(testTask, new DefaultTestFilter(), instantiator)
    }
}
