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
package org.gradle.logging.internal

import org.gradle.api.logging.LogLevel
import org.gradle.util.TextUtil
import spock.lang.Specification

import java.text.SimpleDateFormat

class OutputSpecification extends Specification {

    private Long counter

    protected String toNative(String value) {
        return TextUtil.toPlatformLineSeparators(value)
    }

    /**
     * Returns timestamp representing 10AM today in local time.
     */
    long getTenAm() {
        return getTime('10:00:00.000')
    }

    long getTime(String time) {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date())
        return new SimpleDateFormat('yyyyMMdd HH:mm:ss.SSS').parse(today + ' ' + time).getTime()
    }

    LogEvent event(String text) {
        return new LogEvent(tenAm, 'category', LogLevel.INFO, text, null)
    }

    LogEvent event(String text, LogLevel logLevel) {
        return new LogEvent(tenAm, 'category', logLevel, text, null)
    }

    LogEvent event(long timestamp, String text, LogLevel logLevel) {
        return new LogEvent(timestamp, 'category', logLevel, text, null)
    }

    LogEvent event(long timestamp, String text) {
        return new LogEvent(timestamp, 'category', LogLevel.INFO, text, null)
    }

    LogEvent event(String text, Throwable throwable) {
        return new LogEvent(tenAm, 'category', LogLevel.INFO, text, throwable)
    }

    ProgressStartEvent start(String description) {
        start(description: description)
    }

    ProgressStartEvent start(Map args) {
        Long parent = counter
        long id = counter == null? counter = 1 : ++counter
        return new ProgressStartEvent(id, parent, tenAm, 'category', args.description, args.shortDescription, args.loggingHeader, args.status)
    }

    ProgressEvent progress(String status) {
        return new ProgressEvent(counter? counter:1, tenAm, 'category', status)
    }

    ProgressCompleteEvent complete(String status) {
        return new ProgressCompleteEvent(counter? counter--:1, tenAm, 'category', 'description', status)
    }
}
