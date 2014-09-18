/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.resource;

import org.gradle.internal.resource.metadata.DefaultExternalResourceMetaData;
import org.gradle.internal.resource.metadata.ExternalResourceMetaData;
import org.gradle.internal.UncheckedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class UrlExternalResource extends AbstractExternalResource {
    private final URL url;
    private final URLConnection connection;
    private final DefaultExternalResourceMetaData metaData;

    public UrlExternalResource(URL url) throws IOException {
        this.url = url;
        connection = url.openConnection();

        URI uri;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }

        metaData = new DefaultExternalResourceMetaData(uri, connection.getLastModified(), connection.getContentLength(), null, null);
    }

    public URI getURI() {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }
    }

    public ExternalResourceMetaData getMetaData() {
        return metaData;
    }

    public boolean isLocal() {
        return url.getProtocol().equalsIgnoreCase("file");
    }

    public long getContentLength() {
        return connection.getContentLength();
    }

    public long getLastModified() {
        return connection.getLastModified();
    }

    public InputStream openStream() throws IOException {
        return connection.getInputStream();
    }
}
