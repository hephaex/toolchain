package org.apache.maven.lifecycle.internal;

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

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.version.DefaultPluginVersionRequest;
import org.apache.maven.plugin.version.PluginVersionRequest;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.plugin.version.PluginVersionResolver;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.0
 * @author Benjamin Bentmann
 * @author Kristian Rosenvold (Extract class)
 *         <p/>
 *         NOTE: This class is not part of any public api and can be changed or deleted without prior notice.
 */
@Component( role = LifecyclePluginResolver.class )
public class LifecyclePluginResolver
{
    @Requirement
    private PluginVersionResolver pluginVersionResolver;


    public LifecyclePluginResolver( PluginVersionResolver pluginVersionResolver )
    {
        this.pluginVersionResolver = pluginVersionResolver;
    }

    public LifecyclePluginResolver()
    {
    }

    public void resolveMissingPluginVersions( MavenProject project, MavenSession session )
        throws PluginVersionResolutionException
    {
        Map<String, String> versions = new HashMap<String, String>( 64 );

        for ( Plugin plugin : project.getBuildPlugins() )
        {
            if ( plugin.getVersion() == null )
            {
                PluginVersionRequest request =
                    new DefaultPluginVersionRequest( plugin, session.getRepositorySession(),
                                                     project.getRemotePluginRepositories() );
                plugin.setVersion( pluginVersionResolver.resolve( request ).getVersion() );
            }
            versions.put( plugin.getKey(), plugin.getVersion() );
        }

        PluginManagement pluginManagement = project.getPluginManagement();
        if ( pluginManagement != null )
        {
            for ( Plugin plugin : pluginManagement.getPlugins() )
            {
                if ( plugin.getVersion() == null )
                {
                    plugin.setVersion( versions.get( plugin.getKey() ) );
                    if ( plugin.getVersion() == null )
                    {
                        PluginVersionRequest request =
                            new DefaultPluginVersionRequest( plugin, session.getRepositorySession(),
                                                             project.getRemotePluginRepositories() );
                        plugin.setVersion( pluginVersionResolver.resolve( request ).getVersion() );
                    }
                }
            }
        }
    }
}