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
package org.gradle.api.internal.artifacts.ivyservice;


import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.artifacts.result.ResolutionResult
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.artifacts.ArtifactDependencyResolver
import org.gradle.api.internal.artifacts.CachingDependencyResolveContext
import org.gradle.api.internal.artifacts.ModuleMetadataProcessor
import org.gradle.api.internal.artifacts.ResolverResults
import org.gradle.api.internal.artifacts.configurations.ConfigurationInternal
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository
import org.gradle.api.specs.Spec
import org.gradle.api.specs.Specs
import spock.lang.Specification

public class SelfResolvingDependencyResolverTest extends Specification {

    private delegate = Mock(ArtifactDependencyResolver)
    private resolvedConfiguration = Mock(ResolvedConfiguration)
    private configuration = Mock(ConfigurationInternal)
    private repositories = [Mock(ResolutionAwareRepository)]
    private dependencies = Mock(DependencySet)
    private metadataProcessor = Stub(ModuleMetadataProcessor)
    private results = new ResolverResults()
    private resolver = new SelfResolvingDependencyResolver(delegate);

    void "returns correct resolved configuration"() {
        given:
        delegate.resolve(configuration, repositories, metadataProcessor, results) >> { results.resolved(resolvedConfiguration, Mock(ResolutionResult)) }
        configuration.getAllDependencies() >> dependencies
        configuration.isTransitive() >> true

        when:
        resolver.resolve(configuration, repositories, metadataProcessor, results)

        then:
        def conf = (SelfResolvingDependencyResolver.FilesAggregatingResolvedConfiguration) results.resolvedConfiguration
        conf.resolvedConfiguration == resolvedConfiguration
        conf.selfResolvingFilesProvider
        conf.selfResolvingFilesProvider.resolveContext.transitive
        conf.selfResolvingFilesProvider.dependencies == dependencies
    }

    void "uses configuration transitive setting"() {
        given:
        delegate.resolve(configuration, repositories, metadataProcessor, results) >> { results.resolved(resolvedConfiguration, Mock(ResolutionResult)) }
        configuration.getAllDependencies() >> dependencies
        configuration.isTransitive() >> false

        when:
        resolver.resolve(configuration, repositories, metadataProcessor, results)

        then:
        def conf = (SelfResolvingDependencyResolver.FilesAggregatingResolvedConfiguration) results.resolvedConfiguration
        !conf.selfResolvingFilesProvider.resolveContext.transitive
    }

    void "delegates to provided resolved configuration"() {
        given:
        delegate.resolve(configuration, repositories, metadataProcessor, results) >> { results.resolved(resolvedConfiguration, Mock(ResolutionResult)) }
        configuration.getAllDependencies() >> dependencies
        configuration.isTransitive() >> true

        when:
        resolver.resolve(configuration, repositories, metadataProcessor, results)
        results.resolvedConfiguration.getFirstLevelModuleDependencies(Specs.satisfyAll())
        results.resolvedConfiguration.getResolvedArtifacts()
        results.resolvedConfiguration.hasError()
        results.resolvedConfiguration.rethrowFailure()
        results.resolvedConfiguration.getLenientConfiguration()

        then:
        1 * resolvedConfiguration.getFirstLevelModuleDependencies(Specs.satisfyAll())
        1 * resolvedConfiguration.getResolvedArtifacts()
        1 * resolvedConfiguration.hasError()
        1 * resolvedConfiguration.rethrowFailure()
        1 * resolvedConfiguration.getLenientConfiguration()
    }

    void "knows how to extract self resolving files"() {
        given:
        def resolvedFiles = Mock(FileCollection)
        def resolveContext = Mock(CachingDependencyResolveContext)
        def fooDep = new DefaultExternalModuleDependency("org", "foo", "1.0")
        Set<Dependency> dependencies = [fooDep, new DefaultExternalModuleDependency("org", "bar", "1.0")]

        def provider = new SelfResolvingDependencyResolver.SelfResolvingFilesProvider(resolveContext, dependencies)

        when:
        def files = provider.getFiles({ it.name == 'foo' } as Spec)

        then:
        1 * resolveContext.add(fooDep)
        1 * resolveContext.resolve() >> resolvedFiles
        1 * resolvedFiles.getFiles() >> [new File('foo.jar')]
        0 * _._

        files*.name == ['foo.jar']
    }

    void "aggregates files with self resolving files first"() {
        given:
        def provider = Mock(SelfResolvingDependencyResolver.SelfResolvingFilesProvider) {
            getFiles(Specs.satisfyAll()) >> [new File("foo.jar")]
        }
        resolvedConfiguration.getFiles(Specs.satisfyAll()) >> new HashSet<File>([new File("bar.jar")])

        def conf = new SelfResolvingDependencyResolver.FilesAggregatingResolvedConfiguration(resolvedConfiguration, provider)

        when:
        def files = conf.getFiles(Specs.satisfyAll())

        then:
        files*.name == ['foo.jar', 'bar.jar']
    }
}
