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

import java.util.List;
import java.util.Map;

/**
 * Class Lifecycle.
 */
public class Lifecycle
{
    public Lifecycle()
    {
    }

    public Lifecycle( String id, List<String> phases, Map<String, String> defaultPhases )
    {
        this.id = id;
        this.phases = phases;
        this.defaultPhases = defaultPhases;
    }

    // <lifecycle>
    //   <id>clean</id>
    //   <phases>
    //     <phase>pre-clean</phase>
    //     <phase>clean</phase>
    //     <phase>post-clean</phase>
    //   </phases>
    //   <default-phases>
    //     <clean>org.apache.maven.plugins:maven-clean-plugin:clean</clean>
    //   </default-phases>
    // </lifecycle>

    private String id;

    private List<String> phases;

    private Map<String, String> defaultPhases;

    public String getId()
    {
        return this.id;
    }

    public List<String> getPhases()
    {
        return this.phases;
    }

    public Map<String, String> getDefaultPhases()
    {
        return defaultPhases;
    }

    @Override
    public String toString()
    {
        return id + " -> " + phases;
    }

}
