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

package org.gradle.plugin.use

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.util.GradleVersion
import spock.lang.Unroll

import static org.gradle.plugin.internal.PluginId.*
import static org.gradle.plugin.use.internal.PluginUseScriptBlockTransformer.*
import static org.hamcrest.Matchers.containsString

class PluginUseDslIntegrationSpec extends AbstractIntegrationSpec {

    def "can use plugins block in project build scripts"() {
        when:
        buildScript """
          plugins {
            id "noop"
            id "noop" version "1.0"
          }
        """

        then:
        succeeds "help"
    }

    def "buildscript blocks are allowed before plugin statements"() {
        when:
        buildScript """
            buildscript {}
            plugins {}
        """

        then:
        succeeds "tasks"
    }

    def "buildscript blocks are not allowed after plugin blocks"() {
        when:
        buildScript """
            plugins {}
            buildscript {}
        """

        then:
        fails "tasks"

        and:
        failure.assertHasLineNumber 3
        failure.assertHasFileName("Build file '${buildFile}'")
        failure.assertThatCause(containsString("all buildscript {} blocks must appear before any plugins {} blocks"))
        includesLinkToUserguide()
    }

    void includesLinkToUserguide() {
        assert failure.assertThatCause(containsString("http://gradle.org/docs/${GradleVersion.current().getVersion()}/userguide/plugins.html#sec:plugins_block"))
    }

    def "build logic cannot precede plugins block"() {
        when:
        buildScript """
            someThing()
            plugins {}
        """

        then:
        fails "tasks"

        and:
        failure.assertHasLineNumber 3
        failure.assertHasFileName("Build file '${buildFile}'")
        failure.assertThatCause(containsString("only buildscript {} and other plugins {} script blocks are allowed before plugins {} blocks, no other statements are allowed"))
        includesLinkToUserguide()
    }

    def "build logic cannot precede any plugins block"() {
        when:
        buildScript """
            plugins {}
            someThing()
            plugins {}
        """

        then:
        fails "tasks"

        and:
        failure.assertHasLineNumber 4
        failure.assertHasFileName("Build file '${buildFile}'")
        failure.assertThatCause(containsString("only buildscript {} and other plugins {} script blocks are allowed before plugins {} blocks, no other statements are allowed"))
        includesLinkToUserguide()
    }

    def "settings scripts cannot plugin blocks"() {
        when:
        settingsFile << "plugins {}"

        then:
        fails "help"

        failure.assertHasLineNumber 1
        failure.assertHasFileName("Settings file '$settingsFile.absolutePath'")
        failure.assertThatCause(containsString("Only Project build scripts can contain plugins {} blocks"))
        includesLinkToUserguide()
    }

    def "init scripts cannot have plugin blocks"() {
        def initScript = file("init.gradle")

        when:
        initScript << "plugins {}"

        then:
        args "-I", initScript.absolutePath
        fails "help"

        failure.assertHasLineNumber 1
        failure.assertHasFileName("Initialization script '$initScript.absolutePath'")
        failure.assertThatCause(containsString("Only Project build scripts can contain plugins {} blocks"))
        includesLinkToUserguide()
    }

    def "script plugins cannot have plugin blocks"() {
        def scriptPlugin = file("plugin.gradle")

        when:
        scriptPlugin << "plugins {}"
        buildScript "apply from: 'plugin.gradle'"

        then:
        fails "help"

        failure.assertHasLineNumber 1
        failure.assertHasFileName("Script '$scriptPlugin.absolutePath'")
        failure.assertThatCause(containsString("Only Project build scripts can contain plugins {} blocks"))
        includesLinkToUserguide()
    }

    def "script plugins applied to arbitrary objects cannot have plugin blocks"() {
        def scriptPlugin = file("plugin.gradle")

        when:
        scriptPlugin << "plugins {}"
        buildScript "task foo; apply from: 'plugin.gradle', to: foo"

        then:
        fails "help"

        failure.assertHasLineNumber 1
        failure.assertHasFileName("Script '$scriptPlugin.absolutePath'")
        failure.assertThatCause(containsString("Only Project build scripts can contain plugins {} blocks"))
        includesLinkToUserguide()
    }

    @Unroll
    def "illegal syntax in plugins block - #code"() {
        when:
        buildScript("""plugins {\n$code\n}""")

        then:
        fails "help"
        failure.assertHasLineNumber lineNumber
        failure.assertHasFileName("Build file '${buildFile}'")
        failure.assertThatCause(containsString(msg))
        includesLinkToUserguide()

        where:
        lineNumber | code                                   | msg
        2          | "a"                                    | BASE_MESSAGE
        2          | "def a = null"                         | BASE_MESSAGE
        2          | "def a = id('foo')"                    | BASE_MESSAGE
        2          | "delegate.id('a')"                     | BASE_MESSAGE
        2          | "id()"                                 | INVALID_ARGUMENT_LIST
        2          | "id(1)"                                | INVALID_ARGUMENT_LIST
        2          | "id(System.getProperty('foo'))"        | INVALID_ARGUMENT_LIST
        2          | "id('a' + 'b')"                        | INVALID_ARGUMENT_LIST
        2          | "id(\"\${'foo'}\")"                    | INVALID_ARGUMENT_LIST
        2          | "version('foo')"                       | BASE_MESSAGE
        2          | "id('foo').version(1)"                 | INVALID_ARGUMENT_LIST
        2          | "id 'foo' version 1"                   | INVALID_ARGUMENT_LIST
        2          | "id 'foo' bah '1'"                     | VERSION_MESSAGE
        2          | "foo 'foo' version '1'"                | BASE_MESSAGE
        3          | "id('foo')\nfoo 'bar'"                 | BASE_MESSAGE
        2          | "if (true) id 'foo'"                   | BASE_MESSAGE
        2          | "id 'foo';version 'bar'"               | BASE_MESSAGE
        2          | "id('foo').\"\${'version'}\" 'bar'"    | BASE_MESSAGE
        2          | "id ' '"                               | invalidPluginIdCharMessage(' ' as char)
        2          | "id '\$'"                              | invalidPluginIdCharMessage('$' as char)
        2          | "id ''"                                | INVALID_ARGUMENT_LIST
        2          | "id 'foo' version ''"                  | INVALID_ARGUMENT_LIST
        2          | "id null"                              | INVALID_ARGUMENT_LIST
        2          | "id 'foo' version null"                | INVALID_ARGUMENT_LIST
        2          | "id '.foo'"                            | ID_SEPARATOR_ON_START_OR_END
        2          | "id 'foo.'"                            | ID_SEPARATOR_ON_START_OR_END
        2          | "id '.'"                               | ID_SEPARATOR_ON_START_OR_END
        2          | "id 'foo..bar'"                        | DOUBLE_SEPARATOR
        2          | "file('foo')" /* script api */         | BASE_MESSAGE
        2          | "getVersion()" /* script target api */ | BASE_MESSAGE
    }

    @Unroll
    def "allowed syntax in plugins block - #code"() {
        when:
        buildScript("""plugins {\n$code\n}""")

        then:
        succeeds "help"

        where:
        code << [
                "id('noop')",
                "id 'noop'",
                "id('noop').version('bar')",
                "id 'noop' version 'bar'",
                "id('noop').\nversion 'bar'",
                "id('noop');id('noop')",
                "id('noop')\nid('noop')",
                "id('noop').version('bar');id('noop').version('foo')",
                "id('noop').version('bar')\nid('noop').version('foo')",
        ]
    }

}
