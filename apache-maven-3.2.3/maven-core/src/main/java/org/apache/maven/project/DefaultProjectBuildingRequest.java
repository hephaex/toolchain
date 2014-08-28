package org.apache.maven.project;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.eclipse.aether.RepositorySystemSession;

public class DefaultProjectBuildingRequest
    implements ProjectBuildingRequest
{

    private RepositorySystemSession repositorySession;

    private ArtifactRepository localRepository;

    private List<ArtifactRepository> remoteRepositories;

    private List<ArtifactRepository> pluginArtifactRepositories;

    private MavenProject project;

    private int validationLevel = ModelBuildingRequest.VALIDATION_LEVEL_STRICT;

    private boolean processPlugins;

    private List<Profile> profiles;

    private List<String> activeProfileIds;

    private List<String> inactiveProfileIds;

    private Properties systemProperties;

    private Properties userProperties;

    private Date buildStartTime;

    private boolean resolveDependencies;

    private boolean resolveVersionRanges;

    private RepositoryMerging repositoryMerging = RepositoryMerging.POM_DOMINANT;

    public DefaultProjectBuildingRequest()
    {
        processPlugins = true;
        profiles = new ArrayList<Profile>();
        activeProfileIds = new ArrayList<String>();
        inactiveProfileIds = new ArrayList<String>();
        systemProperties = new Properties();
        userProperties = new Properties();
        remoteRepositories = new ArrayList<ArtifactRepository>();
        pluginArtifactRepositories = new ArrayList<ArtifactRepository>();
    }

    public DefaultProjectBuildingRequest( ProjectBuildingRequest request )
    {
        this();
        setProcessPlugins( request.isProcessPlugins() );
        setProfiles( request.getProfiles() );
        setActiveProfileIds( request.getActiveProfileIds() );
        setInactiveProfileIds( request.getInactiveProfileIds() );
        setSystemProperties( request.getSystemProperties() );
        setUserProperties( request.getUserProperties() );
        setRemoteRepositories( request.getRemoteRepositories() );
        setPluginArtifactRepositories( request.getPluginArtifactRepositories() );
        setRepositorySession( request.getRepositorySession() );
        setLocalRepository( request.getLocalRepository() );
        setBuildStartTime( request.getBuildStartTime() );
        setProject( request.getProject() );
        setResolveDependencies( request.isResolveDependencies() );
        setValidationLevel( request.getValidationLevel() );
    }

    public MavenProject getProject()
    {
        return project;
    }

    public void setProject( MavenProject mavenProject )
    {
        this.project = mavenProject;
    }

    public ProjectBuildingRequest setLocalRepository( ArtifactRepository localRepository )
    {
        this.localRepository = localRepository;
        return this;
    }

    public ArtifactRepository getLocalRepository()
    {
        return localRepository;
    }

    public List<ArtifactRepository> getRemoteRepositories()
    {
        return remoteRepositories;
    }

    public ProjectBuildingRequest setRemoteRepositories( List<ArtifactRepository> remoteRepositories )
    {
        if ( remoteRepositories != null )
        {
            this.remoteRepositories = new ArrayList<ArtifactRepository>( remoteRepositories );
        }
        else
        {
            this.remoteRepositories.clear();
        }

        return this;
    }

    public List<ArtifactRepository> getPluginArtifactRepositories()
    {
        return pluginArtifactRepositories;
    }

    public ProjectBuildingRequest setPluginArtifactRepositories( List<ArtifactRepository> pluginArtifactRepositories )
    {
        if ( pluginArtifactRepositories != null )
        {
            this.pluginArtifactRepositories = new ArrayList<ArtifactRepository>( pluginArtifactRepositories );
        }
        else
        {
            this.pluginArtifactRepositories.clear();
        }

        return this;
    }

    public Properties getSystemProperties()
    {
        return systemProperties;
    }

    public ProjectBuildingRequest setSystemProperties( Properties systemProperties )
    {
        if ( systemProperties != null )
        {
            this.systemProperties = new Properties();
            synchronized ( systemProperties )
            { // avoid concurrentmodification if someone else sets/removes an unrelated system property
                this.systemProperties.putAll( systemProperties );
            }
        }
        else
        {
            this.systemProperties.clear();
        }

        return this;
    }

    public Properties getUserProperties()
    {
        return userProperties;
    }

    public ProjectBuildingRequest setUserProperties( Properties userProperties )
    {
        if ( userProperties != null )
        {
            this.userProperties = new Properties();
            this.userProperties.putAll( userProperties );
        }
        else
        {
            this.userProperties.clear();
        }

        return this;
    }

    public boolean isProcessPlugins()
    {
        return processPlugins;
    }

    public ProjectBuildingRequest setProcessPlugins( boolean processPlugins )
    {
        this.processPlugins = processPlugins;
        return this;
    }
    
    public ProjectBuildingRequest setResolveDependencies( boolean resolveDependencies )
    {
        this.resolveDependencies = resolveDependencies;
        return this;
    }

    public boolean isResolveDependencies()
    {
        return resolveDependencies;
    }

    /** @since 3.2.2 */
    public ProjectBuildingRequest setResolveVersionRanges( boolean value )
    {
        this.resolveVersionRanges = value;
        return this;
    }

    /** @since 3.2.2 */
    public boolean isResolveVersionRanges()
    {
        return this.resolveVersionRanges;
    }

    public ProjectBuildingRequest setValidationLevel( int validationLevel )
    {
        this.validationLevel = validationLevel;
        return this;
    }

    public int getValidationLevel()
    {
        return validationLevel;
    }

    public List<String> getActiveProfileIds()
    {
        return activeProfileIds;
    }

    public void setActiveProfileIds( List<String> activeProfileIds )
    {
        if ( activeProfileIds != null )
        {
            this.activeProfileIds = new ArrayList<String>( activeProfileIds );
        }
        else
        {
            this.activeProfileIds.clear();
        }
    }

    public List<String> getInactiveProfileIds()
    {
        return inactiveProfileIds;
    }

    public void setInactiveProfileIds( List<String> inactiveProfileIds )
    {
        if ( inactiveProfileIds != null )
        {
            this.inactiveProfileIds = new ArrayList<String>( inactiveProfileIds );
        }
        else
        {
            this.inactiveProfileIds.clear();
        }
    }

    public void setProfiles( List<Profile> profiles )
    {
        if ( profiles != null )
        {
            this.profiles = new ArrayList<Profile>( profiles );
        }
        else
        {
            this.profiles.clear();
        }
    }

    public void addProfile( Profile profile )
    {
        profiles.add( profile );
    }

    public List<Profile> getProfiles()
    {
        return profiles;
    }

    public Date getBuildStartTime()
    {
        return buildStartTime;
    }

    public void setBuildStartTime( Date buildStartTime )
    {
        this.buildStartTime = buildStartTime;
    }

    public RepositorySystemSession getRepositorySession()
    {
        return repositorySession;
    }

    public DefaultProjectBuildingRequest setRepositorySession( RepositorySystemSession repositorySession )
    {
        this.repositorySession = repositorySession;
        return this;
    }

    public DefaultProjectBuildingRequest setRepositoryMerging( RepositoryMerging repositoryMerging )
    {
        if ( repositoryMerging == null )
        {
            throw new IllegalArgumentException( "repository merge mode not specified" );
        }
        this.repositoryMerging = repositoryMerging;
        return this;
    }

    public RepositoryMerging getRepositoryMerging()
    {
        return repositoryMerging;
    }

}
