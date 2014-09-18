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



package org.gradle.internal.resource.transfer

import org.gradle.internal.Factory
import org.gradle.logging.ProgressLogger
import org.gradle.logging.ProgressLoggerFactory
import spock.lang.Specification

class ProgressLoggingExternalResourceUploaderTest extends Specification {
    ExternalResourceUploader uploader = Mock()
    ProgressLoggerFactory progressLoggerFactory = Mock();
    def progressLoggerUploader = new ProgressLoggingExternalResourceUploader(uploader, progressLoggerFactory)
    ProgressLogger progressLogger = Mock()
    InputStream inputStream = Mock();
    Factory<InputStream> delegateFactory = Mock();

    def "delegates upload to delegate uploader and logs progress"() {
        setup:
        startsProgress()

        when:
        progressLoggerUploader.upload(inputStreamFactory(), 5 * 1024, new URI("http://a/remote/path"))
        then:
        1 * delegateFactory.create() >> inputStream
        1 * uploader.upload(_, 5 * 1024, new URI("http://a/remote/path")) >> {factory, length, destination ->
            def stream = factory.create();
            assert stream.read(new byte[1024]) == 1024
            assert stream.read() == 48
        }
        1 * inputStream.read(_, 0, 1024) >> 1024
        1 * inputStream.read() >> 48
        1 * progressLogger.progress(_)
        1 * progressLogger.completed()
    }

    private Factory inputStreamFactory() {
        new Factory<InputStream>() {
            InputStream create() {
                return delegateFactory.create()
            }
        }
    }

    def startsProgress() {
        1 * progressLoggerFactory.newOperation(ProgressLoggingExternalResourceUploader.class) >> progressLogger;
        1 * progressLogger.setDescription("Upload http://a/remote/path")
        1 * progressLogger.setLoggingHeader("Upload http://a/remote/path")
        1 * progressLogger.started()
    }
}
