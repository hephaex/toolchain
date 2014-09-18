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
package org.gradle.integtests;

import org.gradle.integtests.fixtures.AbstractIntegrationTest;
import org.gradle.integtests.fixtures.ForkScalaCompileInDaemonModeFixture;
import org.junit.Rule;
import org.junit.Test;

public class ScalaProjectIntegrationTest extends AbstractIntegrationTest {
    @Rule
    public final ForkScalaCompileInDaemonModeFixture forkScalaCompileInDaemonModeFixture = new ForkScalaCompileInDaemonModeFixture(executer, testDirectoryProvider);

    @Test
    public void handlesJavaSourceOnly() {
        testFile("src/main/java/somepackage/SomeClass.java").writelns("public class SomeClass { }");
        testFile("build.gradle").write("apply plugin: 'scala'");
        testFile("settings.gradle").write("rootProject.name='javaOnly'");
        inTestDirectory().withTasks("build").run();
        testFile("build/libs/javaOnly.jar").assertExists();
    }
}
