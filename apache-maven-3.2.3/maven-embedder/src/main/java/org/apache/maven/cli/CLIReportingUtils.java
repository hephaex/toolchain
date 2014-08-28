package org.apache.maven.cli;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.Os;
import org.slf4j.Logger;

/**
 * Utility class used to report errors, statistics, application version info, etc.
 *
 * @author jdcasey
 *
 */
public final class CLIReportingUtils
{

    public static final long MB = 1024 * 1024;

    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;

    public static final String BUILD_VERSION_PROPERTY = "version";

    public static String showVersion()
    {
        final String LS = System.getProperty( "line.separator" );
        Properties properties = getBuildProperties();
        StringBuilder version = new StringBuilder();
        version.append( createMavenVersionString( properties ) ).append( LS );
        version.append( reduce( properties.getProperty( "distributionShortName" ) + " home: "
                            + System.getProperty( "maven.home", "<unknown maven home>" ) ) ).append( LS );
        version.append( "Java version: " ).append(
            System.getProperty( "java.version", "<unknown java version>" ) ).append( ", vendor: " ).append(
            System.getProperty( "java.vendor", "<unknown vendor>" ) ).append( LS );
        version.append( "Java home: " ).append( System.getProperty( "java.home", "<unknown java home>" ) ).append( LS );
        version.append( "Default locale: " ).append( Locale.getDefault() ).append( ", platform encoding: " ).append(
            System.getProperty( "file.encoding", "<unknown encoding>" ) ).append( LS );
        version.append( "OS name: \"" ).append( Os.OS_NAME ).append( "\", version: \"" ).append( Os.OS_VERSION ).append(
            "\", arch: \"" ).append( Os.OS_ARCH ).append( "\", family: \"" ).append( Os.OS_FAMILY ).append( "\"" );
        return version.toString();
    }

    /**
     * Create a human readable string containing the Maven version, buildnumber, and time of build
     *
     * @param buildProperties The build properties
     * @return Readable build info
     */
    static String createMavenVersionString( Properties buildProperties )
    {
        String timestamp = reduce( buildProperties.getProperty( "timestamp" ) );
        String version = reduce( buildProperties.getProperty( BUILD_VERSION_PROPERTY ) );
        String rev = reduce( buildProperties.getProperty( "buildNumber" ) );
        String distributionName = reduce( buildProperties.getProperty( "distributionName" ) );

        String msg = distributionName + " ";
        msg += ( version != null ? version : "<version unknown>" );
        if ( rev != null || timestamp != null )
        {
            msg += " (";
            msg += ( rev != null ? rev : "" );
            if ( timestamp != null )
            {
                String ts = formatTimestamp( Long.valueOf( timestamp ) );
                msg += ( rev != null ? "; " : "" ) + ts;
            }
            msg += ")";
        }
        return msg;
    }

    private static String reduce( String s )
    {
        return ( s != null ? ( s.startsWith( "${" ) && s.endsWith( "}" ) ? null : s ) : null );
    }

    static Properties getBuildProperties()
    {
        Properties properties = new Properties();
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = MavenCli.class.getResourceAsStream( "/org/apache/maven/messages/build.properties" );

            if ( resourceAsStream != null )
            {
                properties.load( resourceAsStream );
            }
        }
        catch ( IOException e )
        {
            System.err.println( "Unable determine version from JAR file: " + e.getMessage() );
        }
        finally
        {
            IOUtil.close( resourceAsStream );
        }

        return properties;
    }

    public static void showError( Logger logger, String message, Throwable e, boolean showStackTrace )
    {
        if ( showStackTrace )
        {
            logger.error( message, e );
        }
        else
        {
            logger.error( message );

            if ( e != null )
            {
                logger.error( e.getMessage() );

                for ( Throwable cause = e.getCause(); cause != null; cause = cause.getCause() )
                {
                    logger.error( "Caused by: " + cause.getMessage() );
                }
            }
        }
    }

    public static String formatTimestamp( long timestamp )
    {
        // Manual construction of the tz offset because only Java 7 is aware of ISO 8601 time zones
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getRawOffset();

        // Raw offset ignores DST, so check if we are in DST now and add the offset
        if ( tz.inDaylightTime( new Date( timestamp ) ) )
        {
            offset += tz.getDSTSavings();
        }

        long m = Math.abs( ( offset / ONE_MINUTE ) % 60 );
        long h = Math.abs( ( offset / ONE_HOUR ) % 24 );

        int offsetDir = (int) Math.signum( (float) offset );
        char offsetSign = offsetDir >= 0 ? '+' : '-';
        return String.format( "%tFT%<tT%s%02d:%02d", timestamp, offsetSign, h, m );
    }

    public static String formatDuration( long duration )
    {
        long ms = duration % 1000;
        long s = ( duration / ONE_SECOND ) % 60;
        long m = ( duration / ONE_MINUTE ) % 60;
        long h = ( duration / ONE_HOUR ) % 24;
        long d = duration / ONE_DAY;

        String format;
        if ( d > 0 )
        {
            format = "%d d %02d:%02d h";
        }
        else if ( h > 0 )
        {
            format = "%2$02d:%3$02d h";
        }
        else if ( m > 0 )
        {
            format = "%3$02d:%4$02d min";
        }
        else
        {
            format = "%4$d.%5$03d s";
        }

        return String.format( format, d, h, m, s, ms );
    }

}
