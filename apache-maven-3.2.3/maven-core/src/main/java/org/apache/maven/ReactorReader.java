package org.apache.maven;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.WorkspaceReader;
import org.eclipse.aether.repository.WorkspaceRepository;
import org.eclipse.aether.util.artifact.ArtifactIdUtils;

/**
 * An implementation of a workspace reader that knows how to search the Maven reactor for artifacts, either
 * as packaged jar if it has been built, or only compile output directory if packaging hasn't happened yet.
 * 
 * @author Jason van Zyl
 */
@Named( ReactorReader.HINT )
@SessionScoped
class ReactorReader
    implements WorkspaceReader
{
    public static final String HINT = "reactor";
    
    private static final Collection<String> COMPILE_PHASE_TYPES = Arrays.asList( "jar", "ejb-client" );

    private Map<String, MavenProject> projectsByGAV;

    private Map<String, List<MavenProject>> projectsByGA;

    private WorkspaceRepository repository;

    @Inject
    public ReactorReader( MavenSession session )
    {
        projectsByGAV = session.getProjectMap();

        projectsByGA = new HashMap<String, List<MavenProject>>( projectsByGAV.size() * 2 );
        for ( MavenProject project : projectsByGAV.values() )
        {
            String key = ArtifactUtils.versionlessKey( project.getGroupId(), project.getArtifactId() );

            List<MavenProject> projects = projectsByGA.get( key );

            if ( projects == null )
            {
                projects = new ArrayList<MavenProject>( 1 );
                projectsByGA.put( key, projects );
            }

            projects.add( project );
        }

        repository = new WorkspaceRepository( "reactor", new HashSet<String>( projectsByGAV.keySet() ) );
    }

    //
    // Public API
    //

    public WorkspaceRepository getRepository()
    {
        return repository;
    }

    public File findArtifact( Artifact artifact )
    {
        String projectKey = ArtifactUtils.key( artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion() );

        MavenProject project = projectsByGAV.get( projectKey );

        if ( project != null )
        {
            File file = find( project, artifact );
            if ( file == null && project != project.getExecutionProject() )
            {
                file = find( project.getExecutionProject(), artifact );
            }
            return file;
        }

        return null;
    }

    public List<String> findVersions( Artifact artifact )
    {
        String key = ArtifactUtils.versionlessKey( artifact.getGroupId(), artifact.getArtifactId() );

        List<MavenProject> projects = projectsByGA.get( key );
        if ( projects == null || projects.isEmpty() )
        {
            return Collections.emptyList();
        }

        List<String> versions = new ArrayList<String>();

        for ( MavenProject project : projects )
        {
            if ( find( project, artifact ) != null )
            {
                versions.add( project.getVersion() );
            }
        }

        return Collections.unmodifiableList( versions );
    }

    //
    // Implementation
    //

    private File find( MavenProject project, Artifact artifact )
    {
        if ( "pom".equals( artifact.getExtension() ) )
        {
            return project.getFile();
        }

        Artifact projectArtifact = findMatchingArtifact( project, artifact );

        if ( hasArtifactFileFromPackagePhase( projectArtifact ) )
        {
            return projectArtifact.getFile();
        }
        else if ( !hasBeenPackaged( project ) )
        {
            // fallback to loose class files only if artifacts haven't been packaged yet
            // and only for plain old jars. Not war files, not ear files, not anything else.

            if ( isTestArtifact( artifact ) )
            {
                if ( project.hasLifecyclePhase( "test-compile" ) )
                {
                    return new File( project.getBuild().getTestOutputDirectory() );
                }
            }
            else
            {
                String type = artifact.getProperty( "type", "" );
                if ( project.hasLifecyclePhase( "compile" ) && COMPILE_PHASE_TYPES.contains( type ) )
                {
                    return new File( project.getBuild().getOutputDirectory() );
                }
            }
        }

        // The fall-through indicates that the artifact cannot be found;
        // for instance if package produced nothing or classifier problems.
        return null;
    }

    private boolean hasArtifactFileFromPackagePhase( Artifact projectArtifact )
    {
        return projectArtifact != null && projectArtifact.getFile() != null && projectArtifact.getFile().exists();
    }

    private boolean hasBeenPackaged( MavenProject project )
    {
        return project.hasLifecyclePhase( "package" ) || project.hasLifecyclePhase( "install" )
            || project.hasLifecyclePhase( "deploy" );
    }

    /**
     * Tries to resolve the specified artifact from the artifacts of the given project.
     * 
     * @param project The project to try to resolve the artifact from, must not be <code>null</code>.
     * @param requestedArtifact The artifact to resolve, must not be <code>null</code>.
     * @return The matching artifact from the project or <code>null</code> if not found. Note that this
     */
    private Artifact findMatchingArtifact( MavenProject project, Artifact requestedArtifact )
    {
        String requestedRepositoryConflictId = ArtifactIdUtils.toVersionlessId( requestedArtifact );

        Artifact mainArtifact = RepositoryUtils.toArtifact( project.getArtifact() );
        if ( requestedRepositoryConflictId.equals( ArtifactIdUtils.toVersionlessId( mainArtifact ) ) )
        {
            return mainArtifact;
        }

        for ( Artifact attachedArtifact : RepositoryUtils.toArtifacts( project.getAttachedArtifacts() ) )
        {
            if ( attachedArtifactComparison( requestedArtifact, attachedArtifact ) )
            {
                return attachedArtifact;
            }
        }

        return null;
    }

    private boolean attachedArtifactComparison( Artifact requested, Artifact attached )
    {
        //
        // We are taking as much as we can from the DefaultArtifact.equals(). The requested artifact has no file so
        // we want to remove that from the comparison.
        //
        return requested.getArtifactId().equals( attached.getArtifactId() )
            && requested.getGroupId().equals( attached.getGroupId() )
            && requested.getVersion().equals( attached.getVersion() )
            && requested.getExtension().equals( attached.getExtension() )
            && requested.getClassifier().equals( attached.getClassifier() );
    }

    /**
     * Determines whether the specified artifact refers to test classes.
     * 
     * @param artifact The artifact to check, must not be {@code null}.
     * @return {@code true} if the artifact refers to test classes, {@code false} otherwise.
     */
    private static boolean isTestArtifact( Artifact artifact )
    {
        return ( "test-jar".equals( artifact.getProperty( "type", "" ) ) )
            || ( "jar".equals( artifact.getExtension() ) && "tests".equals( artifact.getClassifier() ) );
    }
}
