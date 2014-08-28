package org.apache.maven.rtinfo.internal;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionConstraint;
import org.eclipse.aether.version.VersionScheme;

/**
 * Provides information about the current Maven runtime.
 */
@Component( role = RuntimeInformation.class )
public class DefaultRuntimeInformation
    implements RuntimeInformation
{

    @Requirement
    private Logger logger;

    private String mavenVersion;

    public String getMavenVersion()
    {
        if ( mavenVersion == null )
        {
            Properties props = new Properties();

            String resource = "META-INF/maven/org.apache.maven/maven-core/pom.properties";

            InputStream is = DefaultRuntimeInformation.class.getResourceAsStream( "/" + resource );
            if ( is != null )
            {
                try
                {
                    props.load( is );
                }
                catch ( IOException e )
                {
                    String msg = "Could not parse " + resource + ", Maven runtime information not available";
                    if ( logger.isDebugEnabled() )
                    {
                        logger.warn( msg, e );
                    }
                    else
                    {
                        logger.warn( msg );
                    }
                }
                finally
                {
                    IOUtil.close( is );
                }
            }
            else
            {
                logger.warn( "Could not locate " + resource + " on classpath, Maven runtime information not available" );
            }

            String version = props.getProperty( "version", "" ).trim();

            if ( !version.startsWith( "${" ) )
            {
                mavenVersion = version;
            }
            else
            {
                mavenVersion = "";
            }
        }

        return mavenVersion;
    }

    public boolean isMavenVersion( String versionRange )
    {
        VersionScheme versionScheme = new GenericVersionScheme();

        if ( versionRange == null )
        {
            throw new IllegalArgumentException( "Version range must not be null" );
        }
        if ( StringUtils.isBlank( versionRange ) )
        {
            throw new IllegalArgumentException( "Version range must not be empty" );
        }

        VersionConstraint constraint;
        try
        {
            constraint = versionScheme.parseVersionConstraint( versionRange );
        }
        catch ( InvalidVersionSpecificationException e )
        {
            throw new IllegalArgumentException( e.getMessage(), e );
        }

        Version current;
        try
        {
            String mavenVersion = getMavenVersion();
            if ( mavenVersion.length() <= 0 )
            {
                throw new IllegalStateException( "Could not determine current Maven version" );
            }

            current = versionScheme.parseVersion( mavenVersion );
        }
        catch ( InvalidVersionSpecificationException e )
        {
            throw new IllegalStateException( "Could not parse current Maven version: " + e.getMessage(), e );
        }

        if ( constraint.getRange() == null )
        {
            return constraint.getVersion().compareTo( current ) <= 0;
        }
        return constraint.containsVersion( current );
    }

}
