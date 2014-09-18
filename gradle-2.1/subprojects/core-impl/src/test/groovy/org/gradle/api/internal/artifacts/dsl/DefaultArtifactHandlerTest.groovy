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

package org.gradle.api.internal.artifacts.dsl

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.artifacts.PublishArtifactSet
import org.gradle.api.internal.artifacts.publish.DefaultPublishArtifact
import org.gradle.internal.typeconversion.NotationParser
import spock.lang.Specification

class DefaultArtifactHandlerTest extends Specification {

    private static final String TEST_CONF_NAME = "someConf"

    private ConfigurationContainer configurationContainerStub = Mock()
    private NotationParser<Object, PublishArtifact> artifactFactoryStub = Mock()
    private Configuration configurationMock = Mock()
    private PublishArtifactSet artifactsMock = Mock()

    private DefaultArtifactHandler artifactHandler = new DefaultArtifactHandler(configurationContainerStub, artifactFactoryStub)

    void setup() {
        configurationContainerStub.findByName(TEST_CONF_NAME) >> configurationMock
        configurationContainerStub.getByName(TEST_CONF_NAME) >> configurationMock
        configurationMock.artifacts >> artifactsMock
    }

    void pushOneDependency() {
        PublishArtifact artifactDummy = Mock()

        when:
        artifactHandler.someConf("someNotation")

        then:
        1 * artifactFactoryStub.parseNotation("someNotation") >> artifactDummy
        1 * artifactsMock.add(artifactDummy)
    }

    void pushOneDependencyWithClosure() {
        PublishArtifact artifact = new DefaultPublishArtifact("name", "ext", "jar", "classifier", null, new File(""))

        when:
        artifactHandler.someConf("someNotation") { type = 'source' }

        then:
        artifact.type == 'source'

        and:
        1 * artifactFactoryStub.parseNotation("someNotation") >> artifact
        1 * artifactsMock.add(artifact)
    }

    void pushMultipleDependencies() {
        PublishArtifact artifactDummy1 = Mock()
        PublishArtifact artifactDummy2 = Mock()

        when:
        artifactHandler.someConf("someNotation", "someNotation2")

        then:
        1 * artifactFactoryStub.parseNotation("someNotation") >> artifactDummy1
        1 * artifactFactoryStub.parseNotation("someNotation2") >> artifactDummy2
        1 * artifactsMock.add(artifactDummy1)
        1 * artifactsMock.add(artifactDummy2)

    }

    void addOneDependency() {
        PublishArtifact artifactDummy = Mock()

        when:
        artifactHandler.add('someConf', "someNotation")

        then:
        1 * artifactFactoryStub.parseNotation("someNotation") >> artifactDummy
        1 * artifactsMock.add(artifactDummy)
    }

    void addOneDependencyWithClosure() {
        PublishArtifact artifact = new DefaultPublishArtifact("name", "ext", "jar", "classifier", null, new File(""))

        when:
        artifactHandler.add('someConf', "someNotation") { type = 'source' }

        then:
        artifact.type == 'source'

        and:
        1 * artifactFactoryStub.parseNotation("someNotation") >> artifact
        1 * artifactsMock.add(artifact)
    }

    void pushToUnknownConfiguration() {
        String unknownConf = TEST_CONF_NAME + "delta"

        given:
        configurationContainerStub.findByName(unknownConf) >> null

        when:
        artifactHandler."$unknownConf"("someNotation")

        then:
        thrown(MissingMethodException)
    }
}
