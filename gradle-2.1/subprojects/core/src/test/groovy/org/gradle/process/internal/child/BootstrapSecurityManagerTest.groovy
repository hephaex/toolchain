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

package org.gradle.process.internal.child

import org.gradle.util.RedirectStdIn
import org.gradle.util.SetSystemProperties
import org.junit.Rule
import spock.lang.Specification

import java.security.AllPermission
import java.security.Permission

class BootstrapSecurityManagerTest extends Specification {
    @Rule SetSystemProperties systemProperties
    @Rule RedirectStdIn stdIn

    def cleanup() {
        System.securityManager = null
    }

    def "reads classpath from System.in and sets up system classpath on first permission check"() {
        def entry1 = new File("a.jar")
        def entry2 = new File("b.jar")
        TestClassLoader cl = Mock()

        given:
        System.in = createStdInContent(entry1, entry2)

        when:
        def securityManager = new BootstrapSecurityManager(cl)

        then:
        0 * cl._

        when:
        securityManager.checkPermission(new AllPermission())

        then:
        1 * cl.addURL(entry1.toURI().toURL())
        1 * cl.addURL(entry2.toURI().toURL())
        0 * cl._
        System.getProperty("java.class.path") == [entry1.absolutePath, entry2.absolutePath].join(File.pathSeparator)

        when:
        securityManager.checkPermission(new AllPermission())

        then:
        0 * cl._
        System.getProperty("java.class.path") == [entry1.absolutePath, entry2.absolutePath].join(File.pathSeparator)
    }

    def "installs custom SecurityManager"() {
        System.setProperty("org.gradle.security.manager", TestSecurityManager.class.name)
        URLClassLoader cl = new URLClassLoader([] as URL[], getClass().classLoader)

        given:
        System.in = createStdInContent()

        when:
        def securityManager = new BootstrapSecurityManager(cl)
        securityManager.checkPermission(new AllPermission())

        then:
        System.securityManager instanceof TestSecurityManager
    }

    def createStdInContent(File... classpath) {
        def out = new ByteArrayOutputStream()
        def dataOut = new DataOutputStream(new EncodedStream.EncodedOutput(out))
        dataOut.writeInt(classpath.length)
        classpath.each { dataOut.writeUTF(it.absolutePath) }
        return new ByteArrayInputStream(out.toByteArray())
    }

    static class TestClassLoader extends URLClassLoader {
        TestClassLoader(URL[] urls) {
            super(urls)
        }

        @Override
        void addURL(URL url) {
        }
    }

    static class TestSecurityManager extends SecurityManager {
        @Override
        void checkPermission(Permission permission) {
        }
    }
}
