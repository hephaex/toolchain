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

package org.gradle.performance.fixture

import org.gradle.api.logging.Logging

public class PerformanceResults {
    private final static LOGGER = Logging.getLogger(PerformanceResults.class)

    String testId
    String testProject
    String[] args
    String[] tasks
    String jvm
    String operatingSystem
    long testTime
    String versionUnderTest
    String vcsBranch
    String vcsCommit

    private final Map<String, BaselineVersion> baselineVersions = new LinkedHashMap<>()
    final MeasuredOperationList current = new MeasuredOperationList(name: "Current Gradle")
    private final results = new CurrentVersionResults(current)

    def clear() {
        baselineVersions.values().each { it.clearResults() }
        current.clear()
    }

    @Override
    String toString() {
        return displayName
    }

    String getDisplayName() {
        return "Results for test project '$testProject' with tasks ${tasks.join(', ')}"
    }

    Collection<BaselineVersion> getBaselineVersions() {
        return baselineVersions.values()
    }

    /**
     * Locates the given baseline version, adding it if not present.
     */
    BaselineVersion baseline(String version) {
        def baselineVersion = baselineVersions[version]
        if (baselineVersion == null) {
            baselineVersion = new BaselineVersion(version)
            baselineVersions[version] = baselineVersion
        }
        return baselineVersion
    }

    /**
     * Locates the given version. Can use either a baseline version or the current branch name.
     */
    VersionResults version(String version) {
        if (version.equals(vcsBranch)) {
            return results
        }
        return baseline(version)
    }

    List<MeasuredOperationList> getFailures() {
        def failures = []
        baselineVersions.values().each {
            failures.addAll it.results.findAll { it.exception }
        }
        failures.addAll current.findAll { it.exception }
        return failures
    }

    void assertEveryBuildSucceeds() {
        LOGGER.info("Asserting all builds have succeeded...");
        assert failures.collect { it.exception }.empty: "Some builds have failed."
    }

    void assertCurrentVersionHasNotRegressed() {
        def slower = checkBaselineVersion({ it.fasterThan(current) }, { it.getSpeedStatsAgainst(displayName, current) })
        def larger = checkBaselineVersion({ it.usesLessMemoryThan(current) }, { it.getMemoryStatsAgainst(displayName, current) })
        assertEveryBuildSucceeds()
        if (slower && larger) {
            throw new AssertionError("$slower\n$larger")
        }
        if (slower) {
            throw new AssertionError(slower)
        }
        if (larger) {
            throw new AssertionError(larger)
        }
    }

    private String checkBaselineVersion(Closure fails, Closure provideMessage) {
        def failed = false
        def failure = new StringBuilder()
        baselineVersions.values().each {
            String message = provideMessage(it)
            if (fails(it)) {
                failed = true
                failure.append message
            }
            println message
        }
        return failed ? failure.toString() : null
    }

    private static class CurrentVersionResults implements VersionResults {
        final MeasuredOperationList results

        CurrentVersionResults(MeasuredOperationList results) {
            this.results = results
        }
    }
}
