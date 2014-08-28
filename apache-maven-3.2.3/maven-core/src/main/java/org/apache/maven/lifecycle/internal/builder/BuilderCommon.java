package org.apache.maven.lifecycle.internal.builder;

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

import org.apache.maven.InternalErrorException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleNotFoundException;
import org.apache.maven.lifecycle.LifecyclePhaseNotFoundException;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.lifecycle.internal.ExecutionEventCatapult;
import org.apache.maven.lifecycle.internal.LifecycleDebugLogger;
import org.apache.maven.lifecycle.internal.LifecycleExecutionPlanCalculator;
import org.apache.maven.lifecycle.internal.ReactorContext;
import org.apache.maven.lifecycle.internal.TaskSegment;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.InvalidPluginDescriptorException;
import org.apache.maven.plugin.MojoNotFoundException;
import org.apache.maven.plugin.PluginDescriptorParsingException;
import org.apache.maven.plugin.PluginNotFoundException;
import org.apache.maven.plugin.PluginResolutionException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.prefix.NoPluginFoundForPrefixException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.Set;

/**
 * Common code that is shared by the LifecycleModuleBuilder and the LifeCycleWeaveBuilder
 *
 * @since 3.0
 * @author Kristian Rosenvold
 *         Builds one or more lifecycles for a full module
 *         NOTE: This class is not part of any public api and can be changed or deleted without prior notice.
 */
@Component( role = BuilderCommon.class )
public class BuilderCommon
{
    @Requirement
    private LifecycleDebugLogger lifecycleDebugLogger;

    @Requirement
    private LifecycleExecutionPlanCalculator lifeCycleExecutionPlanCalculator;

    @Requirement
    private ExecutionEventCatapult eventCatapult;

    @Requirement
    private Logger logger;


    public BuilderCommon()
    {
    }

    public BuilderCommon( LifecycleDebugLogger lifecycleDebugLogger,
                          LifecycleExecutionPlanCalculator lifeCycleExecutionPlanCalculator, Logger logger )
    {
        this.lifecycleDebugLogger = lifecycleDebugLogger;
        this.lifeCycleExecutionPlanCalculator = lifeCycleExecutionPlanCalculator;
        this.logger = logger;
    }

    public MavenExecutionPlan resolveBuildPlan( MavenSession session, MavenProject project, TaskSegment taskSegment,
                                                Set<Artifact> projectArtifacts )
        throws PluginNotFoundException, PluginResolutionException, LifecyclePhaseNotFoundException,
        PluginDescriptorParsingException, MojoNotFoundException, InvalidPluginDescriptorException,
        NoPluginFoundForPrefixException, LifecycleNotFoundException, PluginVersionResolutionException,
        LifecycleExecutionException
    {
        MavenExecutionPlan executionPlan =
            lifeCycleExecutionPlanCalculator.calculateExecutionPlan( session, project, taskSegment.getTasks() );

        lifecycleDebugLogger.debugProjectPlan( project, executionPlan );

        if ( session.getRequest().getDegreeOfConcurrency() > 1 )
        {
            final Set<Plugin> unsafePlugins = executionPlan.getNonThreadSafePlugins();
            if ( !unsafePlugins.isEmpty()  && logger.isDebugEnabled() )
            {
                logger.warn( "*****************************************************************" );
                logger.warn( "* Your build is requesting parallel execution, but project      *" );
                logger.warn( "* contains the following plugin(s) that have goals not marked   *" );
                logger.warn( "* as @threadSafe to support parallel building.                  *" );
                logger.warn( "* While this /may/ work fine, please look for plugin updates    *" );
                logger.warn( "* and/or request plugins be made thread-safe.                   *" );
                logger.warn( "* If reporting an issue, report it against the plugin in        *" );
                logger.warn( "* question, not against maven-core                              *" );
                logger.warn( "*****************************************************************" );
                if ( logger.isDebugEnabled() )
                {
                    final Set<MojoDescriptor> unsafeGoals = executionPlan.getNonThreadSafeMojos();
                    logger.warn( "The following goals are not marked @threadSafe in " + project.getName() + ":" );
                    for ( MojoDescriptor unsafeGoal : unsafeGoals )
                    {
                        logger.warn( unsafeGoal.getId() );
                    }
                }
                else
                {
                    logger.warn( "The following plugins are not marked @threadSafe in " + project.getName() + ":" );
                    for ( Plugin unsafePlugin : unsafePlugins )
                    {
                        logger.warn( unsafePlugin.getId() );
                    }
                    logger.warn( "Enable debug to see more precisely which goals are not marked @threadSafe." );
                }
                logger.warn( "*****************************************************************" );
            }
        }

        return executionPlan;
    }

    public void handleBuildError( final ReactorContext buildContext, final MavenSession rootSession,
                                  final MavenSession currentSession, final MavenProject mavenProject, Exception e,
                                  final long buildStartTime )
    {
        if ( e instanceof RuntimeException )
        {
            e = new InternalErrorException( "Internal error: " + e, e );
        }

        buildContext.getResult().addException( e );

        long buildEndTime = System.currentTimeMillis();

        buildContext.getResult().addBuildSummary( new BuildFailure( mavenProject, buildEndTime - buildStartTime, e ) );

        eventCatapult.fire( ExecutionEvent.Type.ProjectFailed, currentSession, null, e );

        if ( MavenExecutionRequest.REACTOR_FAIL_NEVER.equals( rootSession.getReactorFailureBehavior() ) )
        {
            // continue the build
        }
        else if ( MavenExecutionRequest.REACTOR_FAIL_AT_END.equals( rootSession.getReactorFailureBehavior() ) )
        {
            // continue the build but ban all projects that depend on the failed one
            buildContext.getReactorBuildStatus().blackList( mavenProject );
        }
        else if ( MavenExecutionRequest.REACTOR_FAIL_FAST.equals( rootSession.getReactorFailureBehavior() ) )
        {
            buildContext.getReactorBuildStatus().halt();
        }
        else
        {
            throw new IllegalArgumentException(
                "invalid reactor failure behavior " + rootSession.getReactorFailureBehavior() );
        }
    }

    public static void attachToThread( MavenProject currentProject )
    {
        ClassRealm projectRealm = currentProject.getClassRealm();
        if ( projectRealm != null )
        {
            Thread.currentThread().setContextClassLoader( projectRealm );
        }
    }

    // Todo: I'm really wondering where this method belongs; smells like it should be on MavenProject, but for some reason
    // it isn't ? This localization is kind-of a code smell.

    public static String getKey( MavenProject project )
    {
        return project.getGroupId() + ':' + project.getArtifactId() + ':' + project.getVersion();
    }

}
