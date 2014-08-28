/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.maven.lifecycle;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.List;

/**
 * @author Kristian Rosenvold
 */

public class DefaultLifecyclesTest
    extends PlexusTestCase
{
    @Requirement
    private DefaultLifecycles defaultLifeCycles;


    protected void setUp()
        throws Exception
    {
        super.setUp();
        defaultLifeCycles = lookup( DefaultLifecycles.class );
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        defaultLifeCycles = null;
        super.tearDown();
    }

    public void testLifecycle()
        throws Exception
    {
        final List<Lifecycle> cycles = defaultLifeCycles.getLifeCycles();
        assertNotNull( cycles );
        final Lifecycle lifecycle = cycles.get( 0 );
        assertEquals( "default", lifecycle.getId() );
        assertEquals( 23, lifecycle.getPhases().size() );

    }

}