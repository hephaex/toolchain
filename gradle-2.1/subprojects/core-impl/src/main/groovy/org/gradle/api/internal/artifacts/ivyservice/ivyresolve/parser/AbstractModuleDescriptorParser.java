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

package org.gradle.api.internal.artifacts.ivyservice.ivyresolve.parser;

import org.gradle.api.internal.artifacts.metadata.MutableModuleVersionMetaData;
import org.gradle.internal.resource.DefaultLocallyAvailableExternalResource;
import org.gradle.internal.resource.LocallyAvailableExternalResource;
import org.gradle.internal.resource.local.DefaultLocallyAvailableResource;
import org.gradle.internal.resource.local.LocallyAvailableResource;

import java.io.File;

public abstract class AbstractModuleDescriptorParser implements MetaDataParser {
    public MutableModuleVersionMetaData parseMetaData(DescriptorParseContext ivySettings, File descriptorFile, boolean validate) throws MetaDataParseException {
        LocallyAvailableResource localResource = new DefaultLocallyAvailableResource(descriptorFile);
        LocallyAvailableExternalResource resource = new DefaultLocallyAvailableExternalResource(descriptorFile.toURI(), localResource);
        return parseDescriptor(ivySettings, resource, validate);
    }

    public MutableModuleVersionMetaData parseMetaData(DescriptorParseContext ivySettings, File descriptorFile) throws MetaDataParseException {
        return parseMetaData(ivySettings, descriptorFile, true);
    }

    public MutableModuleVersionMetaData parseMetaData(DescriptorParseContext ivySettings, LocallyAvailableExternalResource resource) throws MetaDataParseException {
        return parseDescriptor(ivySettings, resource, true);
    }

    protected MutableModuleVersionMetaData parseDescriptor(DescriptorParseContext ivySettings, LocallyAvailableExternalResource resource, boolean validate) throws MetaDataParseException {
        try {
            return doParseDescriptor(ivySettings, resource, validate);
        } catch (MetaDataParseException e) {
            throw e;
        } catch (Exception e) {
            throw new MetaDataParseException(getTypeName(), resource, e);
        }
    }

    protected abstract String getTypeName();

    protected abstract MutableModuleVersionMetaData doParseDescriptor(DescriptorParseContext ivySettings, LocallyAvailableExternalResource resource, boolean validate) throws Exception;
}
