package org.apache.maven.lifecycle;

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

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 */
public class LifecycleExecutionException
    extends Exception
{
    private MavenProject project;
    
    public LifecycleExecutionException( String message )
    {
        super( message );
    }

    public LifecycleExecutionException( Throwable cause )
    {
        super( cause );
    }

    public LifecycleExecutionException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    public LifecycleExecutionException( String message, MavenProject project )
    {
        super( message );
        this.project = project;
    }

    public LifecycleExecutionException( String message, MojoExecution execution, MavenProject project )
    {
        super( message );
        this.project = project;
    }

    public LifecycleExecutionException( String message, MojoExecution execution, MavenProject project, Throwable cause )
    {
        super( message, cause );
        this.project = project;
    }

    public LifecycleExecutionException( MojoExecution execution, MavenProject project, Throwable cause )
    {
        this( createMessage( execution, project, cause ), execution, project, cause );
    }

    public MavenProject getProject()
    {
        return project;
    }

    private static String createMessage( MojoExecution execution, MavenProject project, Throwable cause )
    {
        StringBuilder buffer = new StringBuilder( 256 );

        buffer.append( "Failed to execute goal" );

        if ( execution != null )
        {
            buffer.append( ' ' );
            buffer.append( execution.getGroupId() );
            buffer.append( ':' );
            buffer.append( execution.getArtifactId() );
            buffer.append( ':' );
            buffer.append( execution.getVersion() );
            buffer.append( ':' );
            buffer.append( execution.getGoal() );
            buffer.append( " (" );
            buffer.append( execution.getExecutionId() );
            buffer.append( ")" );
        }

        if ( project != null )
        {
            buffer.append( " on project " );
            buffer.append( project.getArtifactId() );
        }

        if ( cause != null )
        {
            buffer.append( ": " ).append( cause.getMessage() );
        }

        return buffer.toString();
    }

}
