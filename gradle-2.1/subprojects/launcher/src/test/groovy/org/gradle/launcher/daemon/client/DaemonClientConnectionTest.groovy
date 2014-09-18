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

package org.gradle.launcher.daemon.client

import org.gradle.messaging.remote.internal.Connection
import spock.lang.Specification

class DaemonClientConnectionTest extends Specification {

    final delegate = Mock(Connection)
    final staleAddressDetector = Mock(DaemonClientConnection.StaleAddressDetector)
    final connection = new DaemonClientConnection(delegate, 'id', staleAddressDetector)

    def "stops"() {
        when:
        connection.stop()

        then:
        1 * delegate.stop()
        0 * staleAddressDetector._

        when:
        connection.requestStop()

        then:
        1 * delegate.requestStop()
        0 * staleAddressDetector._
    }

    def "dispatches messages"() {
        when:
        connection.dispatch("foo")

        then:
        1 * delegate.dispatch("foo")
        0 * staleAddressDetector._
    }

    def "receives messages"() {
        given:
        delegate.receive() >> "bar"

        when:
        def out = connection.receive()

        then:
        "bar" == out
        0 * staleAddressDetector._
    }

    def "treats failure to dispatch before receiving as a stale address"() {
        def failure = new FooException()

        given:
        delegate.dispatch("foo") >> { throw failure }

        when:
        connection.dispatch("foo")

        then:
        def ex = thrown(StaleDaemonAddressException)
        ex.cause == failure
        1 * staleAddressDetector.maybeStaleAddress(failure) >> true
        0 * staleAddressDetector._
    }

    def "handles failed dispatch"() {
        def failure = new FooException()

        given:
        delegate.receive() >> "result"
        delegate.dispatch("broken") >> { throw failure }

        when:
        connection.receive()
        connection.dispatch("broken")

        then:
        def ex = thrown(DaemonConnectionException)
        ex.class == DaemonConnectionException
        ex.cause == failure
        0 * staleAddressDetector._
    }

    def "treats failure to receive first message as a stale address"() {
        def failure = new FooException()

        given:
        delegate.receive() >> { throw failure }

        when:
        connection.receive()

        then:
        def ex = thrown(StaleDaemonAddressException)
        ex.cause == failure
        1 * staleAddressDetector.maybeStaleAddress(failure) >> true
        0 * staleAddressDetector._
    }

    def "handles failed receive"() {
        def failure = new FooException()

        given:
        1 * delegate.receive() >> "first"
        delegate.receive() >> { throw failure }

        when:
        connection.receive()
        connection.receive()

        then:
        def ex = thrown(DaemonConnectionException)
        ex.class == DaemonConnectionException
        ex.cause == failure
        0 * staleAddressDetector._
    }

    class FooException extends RuntimeException {}
}
