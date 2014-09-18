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

package org.gradle.language.base.plugins

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.TaskDependency
import org.gradle.language.base.FunctionalSourceSet
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.internal.LanguageRegistration
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.model.internal.core.ModelPath
import org.gradle.runtime.base.BinarySpec
import org.gradle.runtime.base.TransformationFileType
import org.gradle.runtime.base.internal.ComponentSpecInternal
import org.gradle.runtime.base.library.DefaultLibrarySpec
import org.gradle.util.TestUtil
import spock.lang.Specification

class ComponentModelBasePluginTest extends Specification {
    def project = TestUtil.createRootProject()

    def "adds componentSpecs extension"() {
        when:
        project.apply(plugin: ComponentModelBasePlugin)
        then:
        project.componentSpecs != null
    }

    def "adds componentSpecs model"() {
        when:
        project.apply(plugin: ComponentModelBasePlugin)
        then:
        project.modelRegistry.get(ModelPath.path("componentSpecs")) != null
    }

    def "registers language sourceset factory per functional sourceset"() {
        setup:
        project.apply(plugin: ComponentModelBasePlugin)
        project.languages.add(new TestLanguageRegistration())

        when:
        def fSourceSet = project.sources.create("testFunctionalSourceSet")

        then:
        fSourceSet.create("test", TestSourceSet) != null
    }

    def "creates language sourceSets for component"() {
        setup:
        project.apply(plugin: ComponentModelBasePlugin)
        project.languages.add(new TestLanguageRegistration())

        def componentSpecInternal = Mock(ComponentSpecInternal)
        _ * componentSpecInternal.name >> "testComponent"
        _ * componentSpecInternal.inputTypes >> [TestTransformFile.class]

        def componentFunctionalSourceSet = Mock(FunctionalSourceSet)
        _ * componentFunctionalSourceSet.name >> "testComponentSources"
        _ * componentSpecInternal.mainSource >> componentFunctionalSourceSet

        when:
        project.componentSpecs.add(componentSpecInternal)

        then:
        1 * componentFunctionalSourceSet.maybeCreate("test", _)
        0 * componentFunctionalSourceSet._
    }

    public static class TestLanguageRegistration implements LanguageRegistration {
        @Override
        String getName() {
            return "test"
        }

        @Override
        Class getSourceSetType() {
            return TestSourceSet.class
        }

        @Override
        Class getSourceSetImplementation() {
            return TestSourceImplementation.class
        }

        @Override
        Map<String, Class<?>> getBinaryTools() {
            return null
        }

        @Override
        Class<? extends TransformationFileType> getOutputType() {
            return TestTransformFile.class
        }

        @Override
        SourceTransformTaskConfig getTransformTask() {
            return null
        }

        @Override
        boolean applyToBinary(BinarySpec binary) {
            return false
        }
    }

    public static class TestSourceImplementation implements TestSourceSet {
        String name

        public TestSourceImplementation(String name, FunctionalSourceSet parent, FileResolver fileResolver) {
            this.name = name;
        }

        @Override
        String getName() {
            return name;
        }

        FileCollection getCompileClasspath() {
            return null
        }

        void setCompileClasspath(FileCollection classpath) {

        }

        FileCollection getRuntimeClasspath() {
            return null
        }

        void setRuntimeClasspath(FileCollection classpath) {

        }

        def getOutput() {
            return null
        }

        TestSourceSet compiledBy(Object... taskPaths) {
            return null
        }

        SourceDirectorySet getResources() {
            return null
        }

        TestSourceSet resources(Closure configureClosure) {
            return null
        }

        SourceDirectorySet getJava() {
            return null
        }

        TestSourceSet java(Closure configureClosure) {
            return null
        }

        SourceDirectorySet getAllJava() {
            return null
        }

        SourceDirectorySet getAllSource() {
            return null
        }

        String getClassesTaskName() {
            return null
        }

        String getProcessResourcesTaskName() {
            return null
        }

        String getCompileJavaTaskName() {
            return null
        }

        String getCompileTaskName(String language) {
            return null
        }

        String getJarTaskName() {
            return null
        }

        String getTaskName(String verb, String target) {
            return null
        }

        String getCompileConfigurationName() {
            return null
        }

        String getRuntimeConfigurationName() {
            return null
        }

        String getDisplayName() {
            return null
        }

        SourceDirectorySet getSource() {
            return null
        }

        @Override
        void source(Action<? super SourceDirectorySet> config) {

        }

        @Override
        void generatedBy(Task generatorTask) {

        }

        @Override
        Task getBuildTask() {
            return null
        }

        @Override
        void setBuildTask(Task lifecycleTask) {

        }

        @Override
        void builtBy(Object... tasks) {

        }

        @Override
        boolean hasBuildDependencies() {
            return false
        }

        @Override
        TaskDependency getBuildDependencies() {
            return null
        }
    }

    public static class TestTransformFile implements TransformationFileType {

    }

    public static interface TestSourceSet extends LanguageSourceSet {
    }

    public static class TestComponentSpecInternal extends DefaultLibrarySpec implements ComponentSpecInternal {
        public TestComponentSpecInternal() {
        }

        @Override
        Set<Class<? extends TransformationFileType>> getInputTypes() {
            return new HashSet<Class<? extends TransformationFileType>>(0)
        }
    }

    public static class TestComponentSpec extends DefaultLibrarySpec {
        public TestComponentSpec() {
        }
    }
}