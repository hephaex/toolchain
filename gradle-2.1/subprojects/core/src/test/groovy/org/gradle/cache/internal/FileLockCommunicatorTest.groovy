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


package org.gradle.cache.internal

import org.gradle.messaging.remote.internal.inet.InetAddressFactory
import org.gradle.util.ConcurrentSpecification

import static org.gradle.test.fixtures.ConcurrentTestUtil.poll

class FileLockCommunicatorTest extends ConcurrentSpecification {

    def communicator = new FileLockCommunicator(new InetAddressFactory())
    Long receivedId

    def cleanup() {
        communicator.stop()
    }

    def "knows port"() {
        expect:
        communicator.getPort() != -1
    }

    def "knows port after stopping"() {
        when:
        communicator.stop()

        then:
        communicator.getPort() == -1
    }

    def "can receive lock id"() {
        start {
            receivedId = communicator.receive()
        }

        poll {
            assert communicator.getPort() != -1 && receivedId == null
        }

        when:
        communicator.pingOwner(communicator.getPort(), 155, "lock")

        then:
        poll {
            assert receivedId == 155
        }
    }

    def "may not receive after the stop"() {
        communicator.stop()
        when:
        communicator.receive()
        then:
        thrown(GracefullyStoppedException)
    }

    def "pinging on a port that nobody listens is safe"() {
        when:
        communicator.pingOwner(6666, 166, "lock")

        then:
        noExceptionThrown()
    }

    def "can be stopped"() {
        expect:
        communicator.stop()
    }

    def "can be stopped during receive"() {
        start {
            try {
                communicator.receive()
            } catch (GracefullyStoppedException e) {}
        }

        when:
        communicator.stop()

        then:
        finished()
    }
}
