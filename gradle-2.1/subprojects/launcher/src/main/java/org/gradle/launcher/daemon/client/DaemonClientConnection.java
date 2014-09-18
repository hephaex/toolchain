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
package org.gradle.launcher.daemon.client;

import org.gradle.api.Nullable;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.messaging.remote.internal.Connection;

/**
 * A simple wrapper for the connection to a daemon plus its password.
 */
public class DaemonClientConnection implements Connection<Object> {
    private final static Logger LOG = Logging.getLogger(DaemonClientConnection.class);
    private final Connection<Object> connection;
    private final String uid;
    private final StaleAddressDetector staleAddressDetector;
    private boolean hasReceived;

    public DaemonClientConnection(Connection<Object> connection, String uid, StaleAddressDetector staleAddressDetector) {
        this.connection = connection;
        this.uid = uid;
        this.staleAddressDetector = staleAddressDetector;
    }

    public void requestStop() {
        LOG.debug("thread {}: requesting connection stop", Thread.currentThread().getId());
        connection.requestStop();
    }

    public String getUid() {
        return uid;
    }

    public void dispatch(Object message) throws DaemonConnectionException {
        LOG.debug("thread {}: dispatching {}", Thread.currentThread().getId(), message.getClass());
        try {
            connection.dispatch(message);
        } catch (Exception e) {
            LOG.debug("Problem dispatching message to the daemon. Performing 'on failure' operation...");
            if (!hasReceived && staleAddressDetector.maybeStaleAddress(e)) {
                throw new StaleDaemonAddressException("Could not dispatch a message to the daemon.", e);
            }
            throw new DaemonConnectionException("Could not dispatch a message to the daemon.", e);
        }
    }

    @Nullable
    public Object receive() throws DaemonConnectionException {
        try {
            return connection.receive();
        } catch (Exception e) {
            LOG.debug("Problem receiving message to the daemon. Performing 'on failure' operation...");
            if (!hasReceived && staleAddressDetector.maybeStaleAddress(e)) {
                throw new StaleDaemonAddressException("Could not receive a message from the daemon.", e);
            }
            throw new DaemonConnectionException("Could not receive a message from the daemon.", e);
        } finally {
            hasReceived = true;
        }
    }

    public void stop() {
        LOG.debug("thread {}: connection stop", Thread.currentThread().getId());
        connection.stop();
    }

    interface StaleAddressDetector {
        /**
         * @return true if the failure should be considered due to a stale address.
         */
        boolean maybeStaleAddress(Exception failure);
    }
}