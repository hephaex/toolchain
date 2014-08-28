package org.apache.maven.execution;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.maven.project.DependencyResolutionResult;
import org.apache.maven.project.MavenProject;

import com.google.common.collect.Maps;

/** @author Jason van Zyl */
public class DefaultMavenExecutionResult
    implements MavenExecutionResult
{
    private MavenProject project;

    private List<MavenProject> topologicallySortedProjects = Collections.emptyList();

    private DependencyResolutionResult dependencyResolutionResult;

    private List<Throwable> exceptions = new CopyOnWriteArrayList<Throwable>();

    private Map<MavenProject, BuildSummary> buildSummaries = Maps.newIdentityHashMap();

    public MavenExecutionResult setProject( MavenProject project )
    {
        this.project = project;

        return this;
    }

    public MavenProject getProject()
    {
        return project;
    }

    public MavenExecutionResult setTopologicallySortedProjects( List<MavenProject> topologicallySortedProjects )
    {
        this.topologicallySortedProjects = topologicallySortedProjects;

        return this;
    }

    public List<MavenProject> getTopologicallySortedProjects()
    {
        return null == topologicallySortedProjects ? Collections.<MavenProject> emptyList()
                        : topologicallySortedProjects;
    }

    public DependencyResolutionResult getDependencyResolutionResult()
    {
        return dependencyResolutionResult;
    }

    public MavenExecutionResult setDependencyResolutionResult( DependencyResolutionResult dependencyResolutionResult )
    {
        this.dependencyResolutionResult = dependencyResolutionResult;

        return this;
    }

    public List<Throwable> getExceptions()
    {
        return exceptions == null ? Collections.<Throwable> emptyList() : exceptions;
    }

    public MavenExecutionResult addException( Throwable t )
    {
        exceptions.add( t );

        return this;
    }

    public boolean hasExceptions()
    {
        return !getExceptions().isEmpty();
    }

    public BuildSummary getBuildSummary( MavenProject project )
    {
        return ( buildSummaries != null ) ? buildSummaries.get( project ) : null;
    }

    public void addBuildSummary( BuildSummary summary )
    {
        if ( buildSummaries == null )
        {
            buildSummaries = new IdentityHashMap<MavenProject, BuildSummary>();
        }
        buildSummaries.put( summary.getProject(), summary );
    }
}
