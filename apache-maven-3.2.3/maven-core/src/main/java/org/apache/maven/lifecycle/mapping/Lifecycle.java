package org.apache.maven.lifecycle.mapping;

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

import java.util.List;
import java.util.Map;

/**
 * Class Lifecycle.
 */
public class Lifecycle
{
    /**
     * Field id
     */
    private String id;

    /**
     * Field phases
     */
    private Map<String, String> phases;

    /*
     * NOTE: This exists merely for backward-compat with legacy-style lifecycle definitions and allows configuration
     * injection to work instead of failing.
     */
    @SuppressWarnings( "unused" )
    private List<String> optionalMojos;

    /**
     * Method getId
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Method getPhases
     */
    public Map<String, String> getPhases()
    {
        return this.phases;
    }

    /**
     * Method setId
     *
     * @param id
     */
    public void setId( String id )
    {
        this.id = id;
    }

    /**
     * Method setPhases
     *
     * @param phases
     */
    public void setPhases( Map<String, String> phases )
    {
        this.phases = phases;
    } //-- void setPhases(java.util.List)

}
