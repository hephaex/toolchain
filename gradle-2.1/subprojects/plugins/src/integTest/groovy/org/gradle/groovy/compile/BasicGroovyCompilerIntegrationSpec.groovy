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
package org.gradle.groovy.compile

import com.google.common.collect.Ordering
import org.gradle.integtests.fixtures.MultiVersionIntegrationSpec
import org.gradle.integtests.fixtures.TargetVersions
import org.gradle.integtests.fixtures.TestResources
import org.gradle.integtests.fixtures.executer.ExecutionFailure
import org.gradle.integtests.fixtures.executer.ExecutionResult
import org.junit.Rule

@TargetVersions(['1.5.8', '1.6.9', '1.7.11', '1.8.8', '2.0.5', '2.1.9', '2.2.2', '2.3.6', '2.4.0-beta-2'])
abstract class BasicGroovyCompilerIntegrationSpec extends MultiVersionIntegrationSpec {
    @Rule
    TestResources resources = new TestResources(temporaryFolder)

    String groovyDependency = "org.codehaus.groovy:groovy-all:$version"

    String getGroovyVersionNumber() {
        version.split(":", 2)[0]
    }

    def setup() {
        // necessary for picking up some of the output/errorOutput when forked executer is used
        executer.withArgument("-i")
    }

    def "compileGoodCode"() {
        groovyDependency = "org.codehaus.groovy:$module:$version"

        expect:
        succeeds("compileGroovy")
        !errorOutput
        file("build/classes/main/Person.class").exists()
        file("build/classes/main/Address.class").exists()

        where:
        module << ["groovy-all", "groovy"]
    }

    def "groovyToolClassesAreNotVisible"() {
        if (versionLowerThan("2.0")) {
            return
        }

        groovyDependency = "org.codehaus.groovy:groovy:$version"

        expect:
        fails("compileGroovy")
        errorOutput.contains('unable to resolve class AntBuilder')

        when:
        buildFile << "dependencies { compile 'org.codehaus.groovy:groovy-ant:${version}' }"

        then:
        succeeds("compileGroovy")
        !errorOutput
        file("build/classes/main/Thing.class").exists()
    }

    def "compileBadCode"() {
        expect:
        fails("compileGroovy")
        // for some reasons, line breaks occur in different places when running this
        // test in different environments; hence we only check for short snippets
        compileErrorOutput.contains 'unable'
        compileErrorOutput.contains 'resolve'
        compileErrorOutput.contains 'Unknown1'
        compileErrorOutput.contains 'Unknown2'
        failure.assertHasCause(compilationFailureMessage)
    }

    def "compileBadJavaCode"() {
        expect:
        fails("compileGroovy")
        compileErrorOutput.contains 'illegal start of type'
        failure.assertHasCause(compilationFailureMessage)
    }

    def "canCompileAgainstGroovyClassThatDependsOnExternalClass"() {
        expect:
        succeeds("test")
    }

    def "canListSourceFiles"() {
        expect:
        succeeds("compileGroovy")
        output.contains(new File("src/main/groovy/compile/test/Person.groovy").toString())
        output.contains(new File("src/main/groovy/compile/test/Person2.groovy").toString())
        !errorOutput
    }

    def "configurationScriptNotSupported"() {
        if (!versionLowerThan("2.1")) {
            return
        }

        expect:
        fails("compileGroovy")
        failure.assertHasCause("Using a Groovy compiler configuration script requires Groovy 2.1+ but found Groovy $groovyVersionNumber")
    }

    def "useConfigurationScript"() {
        if (versionLowerThan("2.1")) {
            return
        }

        expect:
        fails("compileGroovy")
        compileErrorOutput.contains('Cannot find matching method java.lang.String#bar()')
    }

    def "failsBecauseOfMissingConfigFile"() {
        if (versionLowerThan("2.1")) {
            return
        }
        expect:
        fails("compileGroovy")
        failure.assertHasCause("File '${file('groovycompilerconfig.groovy')}' specified for property 'groovyOptions.configurationScript' does not exist.")
    }

    def "failsBecauseOfInvalidConfigFile"() {
        if (versionLowerThan("2.1")) {
            return
        }
        expect:
        fails("compileGroovy")
        failure.assertHasCause("Could not execute Groovy compiler configuration script: ${file('groovycompilerconfig.groovy')}")
    }

    protected ExecutionResult run(String... tasks) {
        configureGroovy()
        super.run(tasks)
    }

    protected ExecutionFailure runAndFail(String... tasks) {
        configureGroovy()
        super.runAndFail(tasks)
    }

    protected ExecutionResult succeeds(String... tasks) {
        configureGroovy()
        super.succeeds(tasks)
    }

    protected ExecutionFailure fails(String... tasks) {
        configureGroovy()
        super.fails(tasks)
    }

    private void configureGroovy() {
        buildFile << """
dependencies {
    compile '${groovyDependency.toString()}'
}

${compilerConfiguration()}
        """
    }

    abstract String compilerConfiguration()

    String getCompilationFailureMessage() {
        return "Compilation failed; see the compiler error output for details."
    }

    String getCompileErrorOutput() {
        return errorOutput
    }

    boolean versionLowerThan(String other) {
        compareToVersion(other) < 0
    }

    int compareToVersion(String other) {
        def versionParts = groovyVersionNumber.split("\\.") as List
        def otherParts = other.split("\\.") as List
        def ordering = Ordering.<Integer> natural().lexicographical()
        ordering.compare(versionParts, otherParts)
    }
}
