package org.apache.maven.settings.io;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Handles deserialization of settings from the default textual format.
 * 
 * @author Benjamin Bentmann
 */
@Component( role = SettingsReader.class )
public class DefaultSettingsReader
    implements SettingsReader
{

    public Settings read( File input, Map<String, ?> options )
        throws IOException
    {
        if ( input == null )
        {
            throw new IllegalArgumentException( "input file missing" );
        }

        Settings settings = read( ReaderFactory.newXmlReader( input ), options );

        return settings;
    }

    public Settings read( Reader input, Map<String, ?> options )
        throws IOException
    {
        if ( input == null )
        {
            throw new IllegalArgumentException( "input reader missing" );
        }

        try
        {
            SettingsXpp3Reader r = new SettingsXpp3Reader();
            return r.read( input, isStrict( options ) );
        }
        catch ( XmlPullParserException e )
        {
            throw new SettingsParseException( e.getMessage(), e.getLineNumber(), e.getColumnNumber(), e );
        }
        finally
        {
            IOUtil.close( input );
        }
    }

    public Settings read( InputStream input, Map<String, ?> options )
        throws IOException
    {
        if ( input == null )
        {
            throw new IllegalArgumentException( "input stream missing" );
        }

        try
        {
            SettingsXpp3Reader r = new SettingsXpp3Reader();
            return r.read( input, isStrict( options ) );
        }
        catch ( XmlPullParserException e )
        {
            throw new SettingsParseException( e.getMessage(), e.getLineNumber(), e.getColumnNumber(), e );
        }
        finally
        {
            IOUtil.close( input );
        }
    }

    private boolean isStrict( Map<String, ?> options )
    {
        Object value = ( options != null ) ? options.get( IS_STRICT ) : null;
        return value == null || Boolean.parseBoolean( value.toString() );
    }

}
