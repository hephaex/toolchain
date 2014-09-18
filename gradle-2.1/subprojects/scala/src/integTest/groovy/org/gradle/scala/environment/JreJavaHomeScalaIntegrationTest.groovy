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

package org.gradle.scala.environment

import org.gradle.integtests.fixtures.*
import org.gradle.integtests.fixtures.executer.GradleContextualExecuter
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import org.junit.Rule
import spock.lang.IgnoreIf
import spock.lang.Unroll

@TargetCoverage({ScalaCoverage.DEFAULT})
class JreJavaHomeScalaIntegrationTest extends AbstractIntegrationSpec {

    @Rule public final ForkScalaCompileInDaemonModeFixture forkScalaCompileInDaemonModeFixture = new ForkScalaCompileInDaemonModeFixture(executer, temporaryFolder)

    @IgnoreIf({ AvailableJavaHomes.bestJre == null})
    @Unroll
    def "scala java cross compilation works in forking mode = #forkMode when JAVA_HOME is set to JRE"() {
        if (GradleContextualExecuter.daemon && !(forkMode && !useAnt)) {
            // don't load up scala in process when testing with the daemon as it blows out permgen
            return
        }

        given:
        def jreJavaHome = AvailableJavaHomes.bestJre
        file("src/main/scala/org/test/JavaClazz.java") << """
                    package org.test;
                    public class JavaClazz {
                        public static void main(String... args){

                        }
                    }
                    """
        writeScalaTestSource("src/main/scala")
        file('build.gradle') << """
                    println "Used JRE: ${jreJavaHome.absolutePath.replace(File.separator, '/')}"
                    apply plugin:'scala'

                    repositories {
                        mavenCentral()
                    }

                    dependencies {
                        compile 'org.scala-lang:scala-library:2.11.1'
                    }

                    compileScala {
                        scalaCompileOptions.useAnt = ${useAnt}
                        scalaCompileOptions.fork = ${forkMode}
                    }
                    """
        when:
        executer.withEnvironmentVars("JAVA_HOME": jreJavaHome.absolutePath).withTasks("compileScala").run().output
        then:
        file("build/classes/main/org/test/JavaClazz.class").exists()
        file("build/classes/main/org/test/ScalaClazz.class").exists()

        where:
        forkMode | useAnt
//        false    | false
        false    | true
        true     | false
        true     | true
    }

    @Requires(TestPrecondition.WINDOWS)
    def "scala compilation works when gradle is started with no java_home defined"() {
        given:
        writeScalaTestSource("src/main/scala");
        file('build.gradle') << """
                    apply plugin:'scala'

                    repositories {
                        mavenCentral()
                    }

                    dependencies {
                        compile 'org.scala-lang:scala-library:2.11.1'
                    }
                    """
        def envVars = System.getenv().findAll { !(it.key in ['GRADLE_OPTS', 'JAVA_HOME', 'Path']) }
        envVars.put("Path", "C:\\Windows\\System32")
        when:
        executer.withEnvironmentVars(envVars).withTasks("compileScala").run()
        then:
        file("build/classes/main/org/test/ScalaClazz.class").exists()
    }

    private writeScalaTestSource(String srcDir) {
        file(srcDir, 'org/test/ScalaClazz.scala') << """
        package org.test{
            object ScalaClazz {
                def main(args: Array[String]) {
                    println("Hello, world!")
                }
            }
        }
        """
    }
}
