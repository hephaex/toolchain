/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.api.internal.artifacts.repositories

import org.gradle.api.InvalidUserDataException
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.ResolverStrategy
import org.gradle.api.internal.artifacts.repositories.resolver.IvyResolver
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransport
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransportFactory
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.filestore.ivy.ArtifactIdentifierFileStore
import org.gradle.internal.reflect.DirectInstantiator
import org.gradle.internal.resource.local.LocallyAvailableResourceFinder
import org.gradle.internal.resource.transport.ExternalResourceRepository
import org.gradle.logging.ProgressLoggerFactory
import spock.lang.Specification

class DefaultIvyArtifactRepositoryTest extends Specification {
    final FileResolver fileResolver = Mock()
    final PasswordCredentials credentials = Mock()
    final RepositoryTransportFactory transportFactory = Mock()
    final LocallyAvailableResourceFinder locallyAvailableResourceFinder = Mock()
    final ExternalResourceRepository resourceRepository = Mock()
    final ProgressLoggerFactory progressLoggerFactory = Mock()
    final ResolverStrategy resolverStrategy = Stub()
    final ArtifactIdentifierFileStore artifactIdentifierFileStore = Stub()

    final DefaultIvyArtifactRepository repository = new DefaultIvyArtifactRepository(
            fileResolver, credentials, transportFactory, locallyAvailableResourceFinder,
            new DirectInstantiator(), resolverStrategy, artifactIdentifierFileStore
    )

    def "default values"() {
        expect:
        repository.url == null
        !repository.resolve.dynamicMode
    }

    def "creates a resolver for HTTP patterns"() {
        repository.name = 'name'
        repository.artifactPattern 'http://host/[organisation]/[artifact]-[revision].[ext]'
        repository.artifactPattern 'http://other/[module]/[artifact]-[revision].[ext]'
        repository.ivyPattern 'http://host/[module]/ivy-[revision].xml'

        given:
        fileResolver.resolveUri('http://host/') >> new URI('http://host/')
        fileResolver.resolveUri('http://other/') >> new URI('http://other/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()


        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository == resourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[organisation]/[artifact]-[revision].[ext]', 'http://other/[module]/[artifact]-[revision].[ext]']
            ivyPatterns == ['http://host/[module]/ivy-[revision].xml']
        }
    }

    def "creates a resolver for file patterns"() {
        repository.name = 'name'
        repository.artifactPattern 'repo/[organisation]/[artifact]-[revision].[ext]'
        repository.artifactPattern 'repo/[organisation]/[module]/[artifact]-[revision].[ext]'
        repository.ivyPattern 'repo/[organisation]/[module]/ivy-[revision].xml'
        def file = new File("test")
        def fileUri = file.toURI()

        given:
        fileResolver.resolveUri('repo/') >> fileUri
        transportFactory.createTransport({ it == ['file'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ["${fileUri}/[organisation]/[artifact]-[revision].[ext]", "${fileUri}/[organisation]/[module]/[artifact]-[revision].[ext]"]
            ivyPatterns == ["${fileUri}/[organisation]/[module]/ivy-[revision].xml"]
        }
    }

    def "uses ivy patterns with specified url and default layout"() {
        repository.name = 'name'
        repository.url = 'http://host'
        repository.layout 'ivy'

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[organisation]/[module]/[revision]/[type]s/[artifact](.[ext])']
            ivyPatterns == ["http://host/[organisation]/[module]/[revision]/[type]s/[artifact](.[ext])"]
        }
    }

    def "uses gradle patterns with specified url and default layout"() {
        repository.name = 'name'
        repository.url = 'http://host'

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier])(.[ext])']
            ivyPatterns == ["http://host/[organisation]/[module]/[revision]/ivy-[revision].xml"]
        }
    }

    def "uses maven patterns with specified url and maven layout"() {
        repository.name = 'name'
        repository.url = 'http://host'
        repository.layout 'maven'

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier])(.[ext])']
            ivyPatterns == ["http://host/[organisation]/[module]/[revision]/ivy-[revision].xml"]
            m2compatible
        }
    }

    def "uses specified base url with configured pattern layout"() {
        repository.name = 'name'
        repository.url = 'http://host'
        repository.layout 'pattern', {
            artifact '[module]/[revision]/[artifact](.[ext])'
            ivy '[module]/[revision]/ivy.xml'
        }

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[module]/[revision]/[artifact](.[ext])']
            ivyPatterns == ["http://host/[module]/[revision]/ivy.xml"]
            !resolver.m2compatible
        }
    }

    def "when requested uses maven patterns with configured pattern layout"() {
        repository.name = 'name'
        repository.url = 'http://host'
        repository.layout 'pattern', {
            artifact '[module]/[revision]/[artifact](.[ext])'
            ivy '[module]/[revision]/ivy.xml'
            m2compatible = true
        }

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[module]/[revision]/[artifact](.[ext])']
            ivyPatterns == ["http://host/[module]/[revision]/ivy.xml"]
            m2compatible
        }
    }

    def "combines layout patterns with additionally specified patterns"() {
        repository.name = 'name'
        repository.url = 'http://host/'
        repository.artifactPattern 'http://host/[other]/artifact'
        repository.ivyPattern 'http://host/[other]/ivy'

        given:
        fileResolver.resolveUri('http://host/') >> new URI('http://host/')
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        when:
        def resolver = repository.createResolver()

        then:
        with(resolver) {
            it instanceof IvyResolver
            repository instanceof ExternalResourceRepository
            name == 'name'
            artifactPatterns == ['http://host/[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier])(.[ext])', 'http://host/[other]/artifact']
            ivyPatterns == ["http://host/[organisation]/[module]/[revision]/ivy-[revision].xml", 'http://host/[other]/ivy']
        }
    }

    def "uses artifact pattern for ivy files when no ivy pattern provided"() {
        repository.name = 'name'
        repository.url = 'http://host'
        repository.layout 'pattern', {
            artifact '[layoutPattern]'
        }
        repository.artifactPattern 'http://other/[additionalPattern]'
        transportFactory.createTransport({ it == ['http'] as Set}, 'name', credentials) >> transport()

        given:
        fileResolver.resolveUri('http://host') >> new URI('http://host')
        fileResolver.resolveUri('http://other/') >> new URI('http://other/')

        when:
        def resolver = repository.createResolver()

        then:
        resolver.artifactPatterns == ['http://host/[layoutPattern]', 'http://other/[additionalPattern]']
        resolver.ivyPatterns == resolver.artifactPatterns
    }

    def "fails when no artifact patterns specified"() {
        given:
        transportFactory.createHttpTransport('name', credentials) >> transport()

        when:
        repository.createResolver()

        then:
        InvalidUserDataException e = thrown()
        e.message == 'You must specify a base url or at least one artifact pattern for an Ivy repository.'
    }

    private RepositoryTransport transport() {
        return Mock(RepositoryTransport) {
            getRepository() >> resourceRepository
        }
    }
}
