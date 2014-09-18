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

package org.gradle.tooling.internal.provider

import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.test.fixtures.file.TestNameTestDirectoryProvider
import org.gradle.util.TestClassLoader
import org.junit.Rule
import spock.lang.Specification

abstract class AbstractClassGraphSpec extends Specification {
    @Rule TestNameTestDirectoryProvider tmpDir = new TestNameTestDirectoryProvider()

    /**
     * Returns the classpath for the given classes.
     */
    List<File> originalClassPath(Class<?>... classes) {
        return classes.collect { ClasspathUtil.getClasspathForClass(it) }
    }

    /**
     * Makes a copy of the given classes and returns the classpath for these copies. Each class is added to its own classpath root.
     */
    List<File> isolatedClasses(Class<?>... classes) {
        return classes.collect {
            def name = it.name.replace('.', '/') + '.class'
            def classPathRoot = tmpDir.file(it.name)
            def classFile = classPathRoot.file(name)
            def resource = it.classLoader.getResource(name)
            classFile.parentFile.mkdirs()
            classFile.bytes = resource.bytes
            classPathRoot
        }
    }

    /**
     * Returns a URLClassLoader with the given classpath and root. Parent defaults to system ClassLoader.
     */
    URLClassLoader urlClassLoader(ClassLoader parent = ClassLoader.systemClassLoader.parent, List<File> classpath) {
        return new URLClassLoader(classpath.collect { it.toURI().toURL() } as URL[], parent)
    }

    /**
     * Returns a custom ClassLoader with the given classpath and root. Parent defaults to system ClassLoader.
     */
    ClassLoader customClassLoader(ClassLoader parent = ClassLoader.systemClassLoader.parent, List<File> classpath) {
        return new TestClassLoader(parent, classpath)
    }
}
