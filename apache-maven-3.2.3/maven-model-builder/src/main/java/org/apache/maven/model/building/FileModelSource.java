package org.apache.maven.model.building;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Wraps an ordinary {@link File} as a model source.
 * 
 * @author Benjamin Bentmann
 */
public class FileModelSource
    implements ModelSource2
{
    private final File pomFile;

    /**
     * Creates a new model source backed by the specified file.
     * 
     * @param pomFile The POM file, must not be {@code null}.
     */
    public FileModelSource( File pomFile )
    {
        if ( pomFile == null )
        {
            throw new IllegalArgumentException( "no POM file specified" );
        }
        this.pomFile = pomFile.getAbsoluteFile();
    }

    public InputStream getInputStream()
        throws IOException
    {
        return new FileInputStream( pomFile );
    }

    public String getLocation()
    {
        return pomFile.getPath();
    }

    /**
     * Gets the POM file of this model source.
     * 
     * @return The underlying POM file, never {@code null}.
     */
    public File getPomFile()
    {
        return pomFile;
    }

    @Override
    public String toString()
    {
        return getLocation();
    }

    public ModelSource2 getRelatedSource( String relPath )
    {
        relPath = relPath.replace( '\\', File.separatorChar ).replace( '/', File.separatorChar );

        File relatedPom = new File( pomFile.getParentFile(), relPath );

        if ( relatedPom.isDirectory() )
        {
            // TODO figure out how to reuse ModelLocator.locatePom(File) here
            relatedPom = new File( relatedPom, "pom.xml" );
        }

        if ( relatedPom.isFile() && relatedPom.canRead() )
        {
            return new FileModelSource( new File( relatedPom.toURI().normalize() ) );
        }

        return null;
    }

    public URI getLocationURI()
    {
        return pomFile.toURI();
    }
}
