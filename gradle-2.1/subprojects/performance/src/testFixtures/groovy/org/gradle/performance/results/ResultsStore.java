/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.performance.results;

import org.gradle.internal.UncheckedException;
import org.gradle.performance.fixture.BaselineVersion;
import org.gradle.performance.fixture.DataReporter;
import org.gradle.performance.fixture.MeasuredOperationList;
import org.gradle.performance.fixture.PerformanceResults;
import org.gradle.performance.measure.DataAmount;
import org.gradle.performance.measure.Duration;
import org.gradle.performance.measure.MeasuredOperation;
import org.gradle.util.GradleVersion;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A {@link DataReporter} implementation that stores results in an H2 relational database.
 */
public class ResultsStore implements DataReporter {
    private final File dbFile;
    private Connection connection;
    private final long ignoreV17Before;

    public ResultsStore() {
        this(new File(System.getProperty("user.home"), ".gradle-performance-test-data/results"));
    }

    public ResultsStore(File dbFile) {
        this.dbFile = dbFile;

        // Ignore some broken samples before the given date
        DateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            ignoreV17Before = timeStampFormat.parse("2013-07-03 00:00:00").getTime();
        } catch (ParseException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }

    public void report(final PerformanceResults results) {
        try {
            withConnection(new ConnectionAction<Void>() {
                public Void execute(Connection connection) throws Exception {
                    long testId;
                    PreparedStatement statement = connection.prepareStatement("insert into testExecution(testId, executionTime, targetVersion, testProject, tasks, args, operatingSystem, jvm, vcsBranch, vcsCommit) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    try {
                        statement.setString(1, results.getTestId());
                        statement.setTimestamp(2, new Timestamp(results.getTestTime()));
                        statement.setString(3, results.getVersionUnderTest());
                        statement.setString(4, results.getTestProject());
                        statement.setObject(5, results.getTasks());
                        statement.setObject(6, results.getArgs());
                        statement.setString(7, results.getOperatingSystem());
                        statement.setString(8, results.getJvm());
                        statement.setString(9, results.getVcsBranch());
                        statement.setString(10, results.getVcsCommit());
                        statement.execute();
                        ResultSet keys = statement.getGeneratedKeys();
                        keys.next();
                        testId = keys.getLong(1);
                    } finally {
                        statement.close();
                    }
                    statement = connection.prepareStatement("insert into testOperation(testExecution, version, executionTimeMs, heapUsageBytes, totalHeapUsageBytes, maxHeapUsageBytes, maxUncollectedHeapBytes, maxCommittedHeapBytes) values (?, ?, ?, ?, ?, ?, ?, ?)");
                    try {
                        addOperations(statement, testId, null, results.getCurrent());
                        for (BaselineVersion baselineVersion : results.getBaselineVersions()) {
                            addOperations(statement, testId, baselineVersion.getVersion(), baselineVersion.getResults());
                        }
                    } finally {
                        statement.close();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not open results datastore '%s'.", dbFile), e);
        }
    }

    private void addOperations(PreparedStatement statement, long testId, String version, MeasuredOperationList operations) throws SQLException {
        for (MeasuredOperation operation : operations) {
            statement.setLong(1, testId);
            statement.setString(2, version);
            statement.setBigDecimal(3, operation.getExecutionTime().toUnits(Duration.MILLI_SECONDS).getValue());
            statement.setBigDecimal(4, operation.getTotalMemoryUsed().toUnits(DataAmount.BYTES).getValue());
            statement.setBigDecimal(5, operation.getTotalHeapUsage().toUnits(DataAmount.BYTES).getValue());
            statement.setBigDecimal(6, operation.getMaxHeapUsage().toUnits(DataAmount.BYTES).getValue());
            statement.setBigDecimal(7, operation.getMaxUncollectedHeap().toUnits(DataAmount.BYTES).getValue());
            statement.setBigDecimal(8, operation.getMaxCommittedHeap().toUnits(DataAmount.BYTES).getValue());
            statement.execute();
        }
    }

    public List<String> getTestNames() {
        try {
            return withConnection(new ConnectionAction<List<String>>() {
                public List<String> execute(Connection connection) throws Exception {
                    List<String> testNames = new ArrayList<String>();
                    ResultSet testExecutions = connection.createStatement().executeQuery("select distinct testId from testExecution order by testId");
                    while (testExecutions.next()) {
                        testNames.add(testExecutions.getString(1));
                    }
                    return testNames;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not load test history from datastore '%s'.", dbFile), e);
        }
    }

    public List<String> getVersions() {
        try {
            return withConnection(new ConnectionAction<List<String>>() {
                public List<String> execute(Connection connection) throws Exception {
                    Set<String> allVersions = new TreeSet<String>(new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            return GradleVersion.version(o1).compareTo(GradleVersion.version(o2));
                        }
                    });
                    PreparedStatement uniqueVersions = connection.prepareStatement("select distinct version from testOperation");
                    ResultSet versions = uniqueVersions.executeQuery();
                    while (versions.next()) {
                        String version = versions.getString(1);
                        if (version != null) {
                            allVersions.add(version);
                        }
                    }
                    versions.close();
                    uniqueVersions.close();

                    ArrayList<String> result = new ArrayList<String>();
                    result.addAll(allVersions);

                    PreparedStatement uniqueBranches = connection.prepareStatement("select distinct vcsBranch from testExecution");
                    ResultSet branches = uniqueBranches.executeQuery();
                    Set<String> allBranches = new TreeSet<String>();
                    while (branches.next()) {
                        allBranches.add(branches.getString(1).trim());
                    }
                    branches.close();
                    uniqueBranches.close();

                    result.addAll(allBranches);

                    return result;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not load version list from datastore '%s'.", dbFile), e);
        }
    }

    public TestExecutionHistory getTestResults(final String testName) {
        try {
            return withConnection(new ConnectionAction<TestExecutionHistory>() {
                public TestExecutionHistory execute(Connection connection) throws Exception {
                    List<PerformanceResults> results = new ArrayList<PerformanceResults>();
                    Set<String> allVersions = new TreeSet<String>(new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            return GradleVersion.version(o1).compareTo(GradleVersion.version(o2));
                        }
                    });
                    Set<String> allBranches = new TreeSet<String>();
                    PreparedStatement executionsForName = connection.prepareStatement("select id, executionTime, targetVersion, testProject, tasks, args, operatingSystem, jvm, vcsBranch, vcsCommit from testExecution where testId = ? order by executionTime desc");
                    PreparedStatement operationsForExecution = connection.prepareStatement("select version, executionTimeMs, heapUsageBytes, totalHeapUsageBytes, maxHeapUsageBytes, maxUncollectedHeapBytes, maxCommittedHeapBytes from testOperation where testExecution = ?");
                    executionsForName.setString(1, testName);
                    ResultSet testExecutions = executionsForName.executeQuery();
                    while (testExecutions.next()) {
                        long id = testExecutions.getLong(1);
                        PerformanceResults performanceResults = new PerformanceResults();
                        performanceResults.setTestId(testName);
                        performanceResults.setTestTime(testExecutions.getTimestamp(2).getTime());
                        performanceResults.setVersionUnderTest(testExecutions.getString(3));
                        performanceResults.setTestProject(testExecutions.getString(4));
                        performanceResults.setTasks(toArray(testExecutions.getObject(5)));
                        performanceResults.setArgs(toArray(testExecutions.getObject(6)));
                        performanceResults.setOperatingSystem(testExecutions.getString(7));
                        performanceResults.setJvm(testExecutions.getString(8));
                        performanceResults.setVcsBranch(testExecutions.getString(9).trim());
                        performanceResults.setVcsCommit(testExecutions.getString(10));

                        results.add(performanceResults);
                        allBranches.add(performanceResults.getVcsBranch());

                        operationsForExecution.setLong(1, id);
                        ResultSet builds = operationsForExecution.executeQuery();
                        while (builds.next()) {
                            String version = builds.getString(1);
                            if ("1.7".equals(version) && performanceResults.getTestTime() <= ignoreV17Before) {
                                // Ignore some broken samples
                                continue;
                            }
                            MeasuredOperation operation = new MeasuredOperation();
                            operation.setExecutionTime(Duration.millis(builds.getBigDecimal(2)));
                            operation.setTotalMemoryUsed(DataAmount.bytes(builds.getBigDecimal(3)));
                            operation.setTotalHeapUsage(DataAmount.bytes(builds.getBigDecimal(4)));
                            operation.setMaxHeapUsage(DataAmount.bytes(builds.getBigDecimal(5)));
                            operation.setMaxUncollectedHeap(DataAmount.bytes(builds.getBigDecimal(6)));
                            operation.setMaxCommittedHeap(DataAmount.bytes(builds.getBigDecimal(7)));

                            if (version == null) {
                                performanceResults.getCurrent().add(operation);
                            } else {
                                BaselineVersion baselineVersion = performanceResults.baseline(version);
                                baselineVersion.getResults().add(operation);
                                allVersions.add(version);
                            }
                        }
                    }
                    testExecutions.close();
                    operationsForExecution.close();
                    executionsForName.close();

                    return new TestExecutionHistory(testName, new ArrayList<String>(allVersions), new ArrayList<String>(allBranches), results);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not load results from datastore '%s'.", dbFile), e);
        }
    }

    private String[] toArray(Object object) {
        Object[] value = (Object[]) object;
        String[] result = new String[value.length];
        for (int i = 0; i < value.length; i++) {
            result[i] = value[i].toString();
        }
        return result;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Could not close datastore '%s'.", dbFile), e);
            } finally {
                connection = null;
            }
        }
    }

    private <T> T withConnection(ConnectionAction<T> action) throws Exception {
        if (connection == null) {
            dbFile.getParentFile().mkdirs();
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(String.format("jdbc:h2:%s", dbFile.getAbsolutePath()), "sa", "");
            try {
                initSchema(connection);
            } catch (Exception e) {
                connection.close();
                connection = null;
                throw e;
            }
        }
        return action.execute(connection);
    }

    private void initSchema(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        statement.execute("create table if not exists testExecution (id bigint identity not null, testId varchar not null, executionTime timestamp not null, targetVersion varchar not null, testProject varchar not null, tasks array not null, args array not null, operatingSystem varchar not null, jvm varchar not null)");
        statement.execute("create table if not exists testOperation (testExecution bigint not null, version varchar, executionTimeMs decimal not null, heapUsageBytes decimal not null, foreign key(testExecution) references testExecution(id))");
        statement.execute("alter table testExecution add column if not exists vcsBranch varchar not null default 'master'");
        statement.execute("alter table testExecution add column if not exists vcsCommit varchar");
        statement.execute("alter table testOperation add column if not exists totalHeapUsageBytes decimal");
        statement.execute("alter table testOperation add column if not exists maxHeapUsageBytes decimal");
        statement.execute("alter table testOperation add column if not exists maxUncollectedHeapBytes decimal");
        statement.execute("alter table testOperation add column if not exists maxCommittedHeapBytes decimal");
        statement.close();
    }

    private interface ConnectionAction<T> {
        T execute(Connection connection) throws Exception;
    }
}
