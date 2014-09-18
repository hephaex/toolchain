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
package org.gradle.launcher.daemon.configuration;

import com.google.common.collect.ImmutableList;
import org.gradle.api.internal.file.IdentityFileResolver;
import org.gradle.initialization.BuildLayoutParameters;
import org.gradle.internal.jvm.Jvm;
import org.gradle.process.internal.JvmOptions;
import org.gradle.util.GUtil;

import java.io.File;
import java.util.*;

import static org.gradle.util.GFileUtils.canonicalise;

public class DaemonParameters {
    static final int DEFAULT_IDLE_TIMEOUT = 3 * 60 * 60 * 1000;

    public static final List<String> DEFAULT_JVM_ARGS = ImmutableList.of("-Xmx1024m", "-XX:MaxPermSize=256m", "-XX:+HeapDumpOnOutOfMemoryError");

    private final String uid;

    private File baseDir;
    private int idleTimeout = DEFAULT_IDLE_TIMEOUT;
    private final JvmOptions jvmOptions = new JvmOptions(new IdentityFileResolver());
    private boolean enabled;
    private File javaHome;

    public DaemonParameters(BuildLayoutParameters layout) {
        this(layout, Collections.<String, String>emptyMap());
    }

    public DaemonParameters(BuildLayoutParameters layout, Map<String, String> extraSystemProperties) {
        this.uid = UUID.randomUUID().toString();
        jvmOptions.setAllJvmArgs(DEFAULT_JVM_ARGS);
        jvmOptions.systemProperties(extraSystemProperties);
        baseDir = new File(layout.getGradleUserHomeDir(), "daemon");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public DaemonParameters setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public File getBaseDir() {
        return baseDir;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public List<String> getEffectiveJvmArgs() {
        return jvmOptions.getAllImmutableJvmArgs();
    }

    public List<String> getAllJvmArgs() {
        return jvmOptions.getAllJvmArgs();
    }

    public File getEffectiveJavaHome() {
        if (javaHome == null) {
            return canonicalise(Jvm.current().getJavaHome());
        }
        return javaHome;
    }

    public String getEffectiveJavaExecutable() {
        if (javaHome == null) {
            return Jvm.current().getJavaExecutable().getAbsolutePath();
        }
        return Jvm.forHome(javaHome).getJavaExecutable().getAbsolutePath();
    }

    public DaemonParameters setJavaHome(File javaHome) {
        this.javaHome = javaHome;
        return this;
    }

    public Map<String, String> getSystemProperties() {
        Map<String, String> systemProperties = new HashMap<String, String>();
        GUtil.addToMap(systemProperties, jvmOptions.getSystemProperties());
        return systemProperties;
    }

    public Map<String, String> getEffectiveSystemProperties() {
        Map<String, String> systemProperties = new HashMap<String, String>();
        GUtil.addToMap(systemProperties, jvmOptions.getSystemProperties());
        GUtil.addToMap(systemProperties, System.getProperties());
        return systemProperties;
    }

    public void setJvmArgs(Iterable<String> jvmArgs) {
        jvmOptions.setAllJvmArgs(jvmArgs);
    }

    public void setDebug(boolean debug) {
        jvmOptions.setDebug(debug);
    }

    public DaemonParameters setBaseDir(File baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public boolean getDebug() {
        return jvmOptions.getDebug();
    }
}
