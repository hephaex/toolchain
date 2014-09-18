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

package org.gradle.plugins.ide.idea

import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier
import org.gradle.api.internal.artifacts.ivyservice.CacheLayout
import org.gradle.integtests.fixtures.TestResources
import org.gradle.internal.os.OperatingSystem
import org.gradle.plugins.ide.AbstractIdeIntegrationTest
import org.gradle.test.fixtures.file.TestFile
import org.junit.Rule
import org.junit.Test

import java.util.regex.Pattern

class IdeaIntegrationTest extends AbstractIdeIntegrationTest {
    @Rule
    public final TestResources testResources = new TestResources(testDirectoryProvider)

    @Test
    void mergesImlCorrectly() {
        def buildFile = file("master/build.gradle")
        buildFile << """
apply plugin: 'java'
apply plugin: 'idea'
"""

        //given
        executer.usingBuildScript(buildFile).withTasks('idea').run()
        def fileContent = getFile([:], 'master/master.iml').text

        executer.usingBuildScript(buildFile).withTasks('idea').run()
        def contentAfterMerge = getFile([:], 'master/master.iml').text

        //then
        assert fileContent == contentAfterMerge
    }

    @Test
    void canCreateAndDeleteMetaData() {
        executer.withTasks('idea').run()

        assertHasExpectedContents('root.ipr')
        assertHasExpectedContents('root.iws')
        assertHasExpectedContents('root.iml')
        assertHasExpectedContents('api/api.iml')
        assertHasExpectedContents('webservice/webservice.iml')

        executer.withTasks('cleanIdea').run()
    }

    @Test
    void worksWithAnEmptyProject() {
        executer.withTasks('idea').run()

        assertHasExpectedContents('root.ipr')
        assertHasExpectedContents('root.iml')
    }

    @Test
    void worksWithASubProjectThatDoesNotHaveTheIdeaPluginApplied() {
        executer.withTasks('idea').run()

        assertHasExpectedContents('root.ipr')
    }

    @Test
    void worksWithNonStandardLayout() {
        executer.inDirectory(testDirectory.file('root')).withTasks('idea').run()

        assertHasExpectedContents('root/root.ipr')
        assertHasExpectedContents('root/root.iml')
        assertHasExpectedContents('top-level.iml')
    }

    @Test
    void overwritesExistingDependencies() {
        executer.withTasks('idea').run()

        assertHasExpectedContents('root.iml')
    }

    @Test
    void addsScalaFacetAndCompilerLibraries() {
        executer.withTasks('idea').run()

        hasProjectLibrary('root.ipr', 'scala-compiler-2.9.2', ['scala-compiler-2.9.2.jar', 'scala-library-2.9.2.jar'], [], [])
        hasProjectLibrary('root.ipr', 'scala-compiler-2.10.0', ['scala-compiler-2.10.0.jar', 'scala-library-2.10.0.jar', 'scala-reflect-2.10.0.jar'], [], [])
        hasScalaFacet('project1/project1.iml', 'scala-compiler-2.9.2')
        hasScalaFacet('project2/project2.iml', 'scala-compiler-2.10.0')
        hasScalaFacet('project3/project3.iml', 'scala-compiler-2.9.2')
    }

    @Test
    void outputDirsDefaultToToIdeaDefaults() {
        runIdeaTask("apply plugin: 'java'; apply plugin: 'idea'")

        def module = parseImlFile("root")
        assert module.component.@"inherit-compiler-output" == "true"
    }

    @Test
    void canHandleCircularModuleDependencies() {
        def repoDir = file("repo")
        def artifact1 = maven(repoDir).module("myGroup", "myArtifact1").dependsOn("myArtifact2").publish().artifactFile
        def artifact2 = maven(repoDir).module("myGroup", "myArtifact2").dependsOn("myArtifact1").publish().artifactFile

        runIdeaTask """
apply plugin: "java"
apply plugin: "idea"

repositories {
    maven { url "${repoDir.toURI()}" }
}

dependencies {
    compile "myGroup:myArtifact1:1.0"
}
        """

        def module = parseImlFile("root")
        def libs = module.component.orderEntry.library
        assert libs.size() == 2
        assert libs.CLASSES.root*.@url*.text().collect { new File(it).name } as Set == [artifact1.name + "!", artifact2.name + "!"] as Set
    }

    @Test
    void libraryReferenceSubstitutesPathVariable() {
        def repoDir = file("repo")
        def artifact1 = maven(repoDir).module("myGroup", "myArtifact1").publish().artifactFile

        runIdeaTask """
    apply plugin: "java"
    apply plugin: "idea"

    repositories {
        maven { url "${repoDir.toURI()}" }
    }

    idea {
       pathVariables("GRADLE_REPO": file("repo"))
    }

    dependencies {
        compile "myGroup:myArtifact1:1.0"
    }
            """

        def module = parseImlFile("root")
        def libs = module.component.orderEntry.library
        assert libs.size() == 1
        assert libs.CLASSES.root*.@url*.text().collect { new File(it).name } as Set == [artifact1.name + "!"] as Set
        assert libs.CLASSES.root*.@url*.text().findAll(){ it.contains("\$GRADLE_REPO\$") }.size() == 1
        assert libs.CLASSES.root*.@url*.text().collect { it.replace("\$GRADLE_REPO\$", relPath(repoDir))} as Set == ["jar://${relPath(artifact1)}!/"] as Set
    }

    @Test
    void onlyAddsSourceDirsThatExistOnFileSystem() {
        runIdeaTask """
apply plugin: "java"
apply plugin: "groovy"
apply plugin: "idea"

sourceSets.main.java.srcDirs.each { it.mkdirs() }
sourceSets.main.resources.srcDirs.each { it.mkdirs() }
sourceSets.test.groovy.srcDirs.each { it.mkdirs() }
        """

        def module = parseImlFile("root")
        def sourceFolders = module.component.content.sourceFolder
        def urls = sourceFolders*.@url*.text()

        assert containsDir("src/main/java", urls)
        assert !containsDir("src/main/groovy", urls)
        assert containsDir("src/main/resources", urls)
        assert !containsDir("src/test/java", urls)
        assert containsDir("src/test/groovy", urls)
        assert !containsDir("src/test/resources", urls)
    }


    @Test
    void triggersWithXmlConfigurationHooks() {
        runIdeaTask '''
apply plugin: 'java'
apply plugin: 'idea'

def hookActivated = 0

idea.module.iml {
    withXml { hookActivated++ }
}

tasks.idea << {
    assert hookActivated == 1 : "withXml() hook shoold be fired"
}
'''
    }

    @Test
    void respectsPerConfigurationExcludes() {
        def repoDir = file("repo")
        maven(repoDir).module("myGroup", "myArtifact1").dependsOn("myArtifact2").publish()
        maven(repoDir).module("myGroup", "myArtifact2").publish()

        runIdeaTask """
apply plugin: 'java'
apply plugin: 'idea'

repositories {
    maven { url "${repoDir.toURI()}" }
}

configurations {
    compile.exclude module: 'myArtifact2'
}

dependencies {
    compile "myGroup:myArtifact1:1.0"
}
        """

        def module = parseImlFile("root")
        def libs = module.component.orderEntry.library
        assert libs.size() == 1
    }

    @Test
    void respectsPerDependencyExcludes() {
        def repoDir = file("repo")
        maven(repoDir).module("myGroup", "myArtifact1").dependsOn("myArtifact2").publish()
        maven(repoDir).module("myGroup", "myArtifact2").publish()

        runIdeaTask """
apply plugin: 'java'
apply plugin: 'idea'

repositories {
    maven { url "${repoDir.toURI()}" }
}

dependencies {
    compile("myGroup:myArtifact1:1.0") {
        exclude module: "myArtifact2"
    }
}
        """

        def module = parseImlFile("root")
        def libs = module.component.orderEntry.library
        assert libs.size() == 1
    }

    @Test
    void allowsCustomOutputFolders() {
        runIdeaTask """
apply plugin: 'java'
apply plugin: 'idea'

idea.module {
    inheritOutputDirs = false
    outputDir = file('foo-out')
    testOutputDir = file('foo-out-test')
}
"""

        //then
        def iml = getFile([:], 'root.iml').text
        assert iml.contains('inherit-compiler-output="false"')
        assert iml.contains('foo-out')
        assert iml.contains('foo-out-test')
    }

    @Test
    void dslSupportsShortFormsForModule() {
        runTask('idea', """
apply plugin: 'idea'

idea.module.name = 'X'
assert idea.module.name == 'X'

idea {
    module.name += 'X'
    assert module.name == 'XX'
}

idea.module {
    name += 'X'
    assert name == 'XXX'
}

""")
    }

    @Test
    void dslSupportsShortFormsForProject() {
        runTask('idea', """
apply plugin: 'idea'

idea.project.wildcards = ['1'] as Set
assert idea.project.wildcards == ['1'] as Set

idea {
    project.wildcards += '2'
    assert project.wildcards == ['1', '2'] as Set
}

idea.project {
    wildcards += '3'
    assert wildcards == ['1', '2', '3'] as Set
}

""")
    }

    @Test
    void showDecentMessageWhenInputFileWasTinkeredWith() {
        //given
        file('root.iml') << 'messed up iml file'

        file('build.gradle') << '''
apply plugin: "java"
apply plugin: "idea"
'''
        file('settings.gradle') << 'rootProject.name = "root"'

        //when
        def failure = executer.withTasks('idea').runWithFailure()

        //then
        failure.output.contains("Perhaps this file was tinkered with?")
    }

    @Test
    void canAddProjectLibraries() {
        runTask("idea", """
apply plugin: 'idea'

idea.project {
    def lib = new org.gradle.plugins.ide.idea.model.ProjectLibrary()
    lib.name = "someLib"
    lib.classes << file("someClasses.jar")
    lib.javadoc << file("someJavadoc.jar")
    lib.sources << file("someSources.jar")
    projectLibraries << lib
}
""")

        hasProjectLibrary("root.ipr", "someLib", ["someClasses.jar"], ["someJavadoc.jar"], ["someSources.jar"])
    }

    private void assertHasExpectedContents(String path) {
        TestFile file = testDirectory.file(path).assertIsFile()
        TestFile expectedFile = testDirectory.file("expectedFiles/${path}.xml").assertIsFile()

        def expectedXml = expectedFile.text

        def homeDir = executer.gradleUserHomeDir.absolutePath.replace(File.separator, '/')
        def pattern = Pattern.compile(Pattern.quote(homeDir) + "/caches/${CacheLayout.ROOT.getKey()}/${CacheLayout.FILE_STORE.getKey()}/([^/]+/[^/]+/[^/]+)/[a-z0-9]+/")
        def actualXml = file.text.replaceAll(pattern, '@CACHE_DIR@/$1/@SHA1@/')

        Diff diff = new Diff(expectedXml, actualXml)
        diff.overrideElementQualifier(new ElementNameAndAttributeQualifier())
        try {
            assert diff.similar()
        } catch (AssertionError e) {
            if (OperatingSystem.current().unix) {
                def process = ["diff", expectedFile.absolutePath, file.absolutePath].execute()
                process.consumeProcessOutput(System.out, System.err)
                process.waitFor()
            }
            throw new AssertionError("generated file '$path' does not contain the expected contents: ${e.message}.\nExpected:\n${expectedXml}\nActual:\n${actualXml}").initCause(e)
        }
    }

    private void hasProjectLibrary(String iprFileName, String libraryName, List<String> classesLibs, List<String> javadocLibs, List<String> sourcesLibs) {
        def project = new XmlSlurper().parse(file(iprFileName))
        def libraryTable = project.component.find { it.@name == "libraryTable" }
        assert libraryTable

        def library = libraryTable.library.find { it.@name == libraryName }
        assert library

        def classesRoots = library.CLASSES.root
        assert classesRoots.size() == classesLibs.size()
        classesLibs.each {
            assert classesRoots.@url.text().contains(it)
        }

        def javadocRoots = library.JAVADOC.root
        assert javadocRoots.size() == javadocLibs.size()
        javadocLibs.each {
            assert javadocRoots.@url.text().contains(it)
        }

        def sourcesRoots = library.SOURCES.root
        assert sourcesRoots.size() == sourcesLibs.size()
        sourcesLibs.each {
            assert sourcesRoots.@url.text().contains(it)
        }
    }

    private void hasScalaFacet(String imlFileName, String libraryName) {
        def module = new XmlSlurper().parse(file(imlFileName))
        def facetManager = module.component.find { it.@name == "FacetManager" }
        assert facetManager

        def facet = facetManager.facet.find { it.@name == "Scala" }
        assert facet
        assert facet.@type == "scala"

        def compilerLibraryLevel = facet.configuration.option.find { it.@name == "compilerLibraryLevel" }
        assert compilerLibraryLevel
        assert compilerLibraryLevel.@value == "Project"

        def compilerLibraryName = facet.configuration.option.find { it.@name == "compilerLibraryName" }
        assert compilerLibraryName
        assert compilerLibraryName.@value == libraryName
    }

    private containsDir(path, urls) {
        urls.any { it.endsWith(path) }
    }

    private String relPath(File file){
        return file.absolutePath.replace(File.separator, "/")
    }
}
