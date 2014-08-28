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

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.LifeCyclePluginAnalyzer;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.lifecycle.mapping.LifecycleMapping;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @since 3.0
 * @author Benjamin Bentmann
 * @author Jason van Zyl
 * @author jdcasey
 * @author Kristian Rosenvold (extracted class only)
 *         <p/>
 *         NOTE: This class is not part of any public api and can be changed or deleted without prior notice.
 */
@Component( role = LifeCyclePluginAnalyzer.class )
public class DefaultLifecyclePluginAnalyzer
    implements LifeCyclePluginAnalyzer
{

    @Requirement( role = LifecycleMapping.class )
    private Map<String, LifecycleMapping> lifecycleMappings;

    @Requirement
    private DefaultLifecycles defaultLifeCycles;

    @Requirement
    private Logger logger;

    public DefaultLifecyclePluginAnalyzer()
    {
    }

    // These methods deal with construction intact Plugin object that look like they come from a standard
    // <plugin/> block in a Maven POM. We have to do some wiggling to pull the sources of information
    // together and this really shows the problem of constructing a sensible default configuration but
    // it's all encapsulated here so it appears normalized to the POM builder.

    // We are going to take the project packaging and find all plugin in the default lifecycle and create
    // fully populated Plugin objects, including executions with goals and default configuration taken
    // from the plugin.xml inside a plugin.
    //

    public Set<Plugin> getPluginsBoundByDefaultToAllLifecycles( String packaging )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Looking up lifecyle mappings for packaging " + packaging + " from "
                + Thread.currentThread().getContextClassLoader() );
        }

        LifecycleMapping lifecycleMappingForPackaging = lifecycleMappings.get( packaging );

        if ( lifecycleMappingForPackaging == null )
        {
            return null;
        }

        Map<Plugin, Plugin> plugins = new LinkedHashMap<Plugin, Plugin>();

        for ( Lifecycle lifecycle : getOrderedLifecycles() )
        {
            org.apache.maven.lifecycle.mapping.Lifecycle lifecycleConfiguration =
                lifecycleMappingForPackaging.getLifecycles().get( lifecycle.getId() );

            Map<String, String> phaseToGoalMapping = null;

            if ( lifecycleConfiguration != null )
            {
                phaseToGoalMapping = lifecycleConfiguration.getPhases();
            }
            else if ( lifecycle.getDefaultPhases() != null )
            {
                phaseToGoalMapping = lifecycle.getDefaultPhases();
            }

            if ( phaseToGoalMapping != null )
            {
                // These are of the form:
                //
                // compile -> org.apache.maven.plugins:maven-compiler-plugin:compile[,gid:aid:goal,...]
                //
                for ( Map.Entry<String, String> goalsForLifecyclePhase : phaseToGoalMapping.entrySet() )
                {
                    String phase = goalsForLifecyclePhase.getKey();
                    String goals = goalsForLifecyclePhase.getValue();
                    if ( goals != null )
                    {
                        parseLifecyclePhaseDefinitions( plugins, phase, goals );
                    }
                }
            }
        }

        return plugins.keySet();
    }

    private List<Lifecycle> getOrderedLifecycles()
    {
        // NOTE: The lifecycle order can affect implied execution ids so we better be deterministic.

        List<Lifecycle> lifecycles = new ArrayList<Lifecycle>( defaultLifeCycles.getLifeCycles() );

        Collections.sort( lifecycles, new Comparator<Lifecycle>()
        {

            public int compare( Lifecycle l1, Lifecycle l2 )
            {
                return l1.getId().compareTo( l2.getId() );
            }

        } );

        return lifecycles;
    }

    private void parseLifecyclePhaseDefinitions( Map<Plugin, Plugin> plugins, String phase, String goals )
    {
        String[] mojos = StringUtils.split( goals, "," );

        for ( int i = 0; i < mojos.length; i++ )
        {
            GoalSpec gs = parseGoalSpec( mojos[i].trim() );

            if ( gs == null )
            {
                logger.warn( "Ignored invalid goal specification '" + mojos[i] + "' from lifecycle mapping for phase "
                    + phase );
                continue;
            }

            Plugin plugin = new Plugin();
            plugin.setGroupId( gs.groupId );
            plugin.setArtifactId( gs.artifactId );
            plugin.setVersion( gs.version );

            Plugin existing = plugins.get( plugin );
            if ( existing != null )
            {
                if ( existing.getVersion() == null )
                {
                    existing.setVersion( plugin.getVersion() );
                }
                plugin = existing;
            }
            else
            {
                plugins.put( plugin, plugin );
            }

            PluginExecution execution = new PluginExecution();
            execution.setId( getExecutionId( plugin, gs.goal ) );
            execution.setPhase( phase );
            execution.setPriority( i - mojos.length );
            execution.getGoals().add( gs.goal );

            plugin.getExecutions().add( execution );
        }
    }

    private GoalSpec parseGoalSpec( String goalSpec )
    {
        GoalSpec gs = new GoalSpec();

        String[] p = StringUtils.split( goalSpec.trim(), ":" );

        if ( p.length == 3 )
        {
            // <groupId>:<artifactId>:<goal>
            gs.groupId = p[0];
            gs.artifactId = p[1];
            gs.goal = p[2];
        }
        else if ( p.length == 4 )
        {
            // <groupId>:<artifactId>:<version>:<goal>
            gs.groupId = p[0];
            gs.artifactId = p[1];
            gs.version = p[2];
            gs.goal = p[3];
        }
        else
        {
            // invalid
            gs = null;
        }

        return gs;
    }

    private String getExecutionId( Plugin plugin, String goal )
    {
        Set<String> existingIds = new HashSet<String>();
        for ( PluginExecution execution : plugin.getExecutions() )
        {
            existingIds.add( execution.getId() );
        }

        String base = "default-" + goal;
        String id = base;

        for ( int index = 1; existingIds.contains( id ); index++ )
        {
            id = base + '-' + index;
        }

        return id;
    }

    static class GoalSpec
    {

        String groupId;

        String artifactId;

        String version;

        String goal;

    }

}
