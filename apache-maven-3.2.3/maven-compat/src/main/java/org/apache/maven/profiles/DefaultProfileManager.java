package org.apache.maven.profiles;

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

import org.apache.maven.model.Activation;
import org.apache.maven.model.Profile;
import org.apache.maven.model.building.ModelProblem;
import org.apache.maven.model.building.ModelProblemCollector;
import org.apache.maven.model.profile.DefaultProfileActivationContext;
import org.apache.maven.model.profile.ProfileSelector;
import org.apache.maven.profiles.activation.ProfileActivationException;
import org.codehaus.plexus.MutablePlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.model.building.ModelProblemCollectorRequest;

@Deprecated
public class DefaultProfileManager
    implements ProfileManager
{

    @Requirement
    private Logger logger;

    @Requirement
    private ProfileSelector profileSelector;

    private List activatedIds = new ArrayList();

    private List deactivatedIds = new ArrayList();

    private List defaultIds = new ArrayList();

    private Map profilesById = new LinkedHashMap();

    private Properties requestProperties;

    /**
     * @deprecated without passing in the system properties, the SystemPropertiesProfileActivator will not work
     *             correctly in embedded envirnments.
     */
    public DefaultProfileManager( PlexusContainer container )
    {
        this( container, null );
    }

    /**
     * the properties passed to the profile manager are the props that
     * are passed to maven, possibly containing profile activator properties
     *
     */
    public DefaultProfileManager( PlexusContainer container, Properties props )
    {
        try
        {
            this.profileSelector = container.lookup( ProfileSelector.class );
            this.logger = ( (MutablePlexusContainer) container ).getLogger();
        }
        catch ( ComponentLookupException e )
        {
            throw new IllegalStateException( e );
        }
        this.requestProperties = props;
    }

    public Properties getRequestProperties()
    {
        return requestProperties;
    }

    public Map getProfilesById()
    {
        return profilesById;
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#addProfile(org.apache.maven.model.Profile)
    */
    public void addProfile( Profile profile )
    {
        String profileId = profile.getId();

        Profile existing = (Profile) profilesById.get( profileId );
        if ( existing != null )
        {
            logger.warn( "Overriding profile: \'" + profileId + "\' (source: " + existing.getSource()
                + ") with new instance from source: " + profile.getSource() );
        }

        profilesById.put( profile.getId(), profile );

        Activation activation = profile.getActivation();

        if ( activation != null && activation.isActiveByDefault() )
        {
            activateAsDefault( profileId );
        }
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#explicitlyActivate(java.lang.String)
    */
    public void explicitlyActivate( String profileId )
    {
        if ( !activatedIds.contains( profileId ) )
        {
            logger.debug( "Profile with id: \'" + profileId + "\' has been explicitly activated." );

            activatedIds.add( profileId );
        }
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#explicitlyActivate(java.util.List)
    */
    public void explicitlyActivate( List profileIds )
    {
        for ( Object profileId1 : profileIds )
        {
            String profileId = (String) profileId1;

            explicitlyActivate( profileId );
        }
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#explicitlyDeactivate(java.lang.String)
    */
    public void explicitlyDeactivate( String profileId )
    {
        if ( !deactivatedIds.contains( profileId ) )
        {
            logger.debug( "Profile with id: \'" + profileId + "\' has been explicitly deactivated." );

            deactivatedIds.add( profileId );
        }
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#explicitlyDeactivate(java.util.List)
    */
    public void explicitlyDeactivate( List profileIds )
    {
        for ( Object profileId1 : profileIds )
        {
            String profileId = (String) profileId1;

            explicitlyDeactivate( profileId );
        }
    }

    /* (non-Javadoc)
    * @see org.apache.maven.profiles.ProfileManager#getActiveProfiles()
    */
    public List getActiveProfiles()
        throws ProfileActivationException
    {
        DefaultProfileActivationContext context = new DefaultProfileActivationContext();
        context.setActiveProfileIds( activatedIds );
        context.setInactiveProfileIds( deactivatedIds );
        context.setSystemProperties( System.getProperties() );
        context.setUserProperties( requestProperties );

        final List<ProfileActivationException> errors = new ArrayList<ProfileActivationException>();

        List<Profile> profiles =
            profileSelector.getActiveProfiles( profilesById.values(), context, new ModelProblemCollector()
            {

                public void add( ModelProblemCollectorRequest req )
                {
                    if ( !ModelProblem.Severity.WARNING.equals( req.getSeverity() ) )
                    {
                        errors.add( new ProfileActivationException( req.getMessage(), req.getException() ) );
                    }
                }
            } );

        if ( !errors.isEmpty() )
        {
            throw errors.get( 0 );
        }

        return profiles;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.profiles.ProfileManager#addProfiles(java.util.List)
     */
    public void addProfiles( List profiles )
    {
        for ( Object profile1 : profiles )
        {
            Profile profile = (Profile) profile1;

            addProfile( profile );
        }
    }

    public void activateAsDefault( String profileId )
    {
        if ( !defaultIds.contains( profileId ) )
        {
            defaultIds.add( profileId );
        }
    }

    public List getExplicitlyActivatedIds()
    {
        return activatedIds;
    }

    public List getExplicitlyDeactivatedIds()
    {
        return deactivatedIds;
    }

    public List getIdsActivatedByDefault()
    {
        return defaultIds;
    }

}
