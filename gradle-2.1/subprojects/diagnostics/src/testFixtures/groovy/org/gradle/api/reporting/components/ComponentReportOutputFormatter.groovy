/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.api.reporting.components

import org.gradle.api.JavaVersion
import org.gradle.api.Transformer
import org.gradle.internal.SystemProperties
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativebinaries.language.cpp.fixtures.AvailableToolChains

class ComponentReportOutputFormatter implements Transformer<String, String> {
    final AvailableToolChains.InstalledToolChain toolChain

    ComponentReportOutputFormatter() {
        this.toolChain = AvailableToolChains.defaultToolChain
    }

    ComponentReportOutputFormatter(AvailableToolChains.InstalledToolChain toolChain) {
        this.toolChain = toolChain
    }

    @Override
    String transform(String original) {
        return original
                .replace("current JDK (1.7)", "current JDK (${JavaVersion.current()})")
                .replace("Tool chain 'clang' (Clang)", toolChain.instanceDisplayName)
                .replace("\n", SystemProperties.lineSeparator)
                .replaceAll('(?m)(build/binaries/.+/)lib(\\w+).dylib$') { it[1] + OperatingSystem.current().getSharedLibraryName(it[2]) }
                .replaceAll('(?m)(build/binaries/.+/)lib(\\w+).a$') { it[1] + OperatingSystem.current().getStaticLibraryName(it[2]) }
                .replaceAll('(?m)(build/binaries/.+/)(\\w+)$') { it[1] + OperatingSystem.current().getExecutableName(it[2]) }
                .replaceAll("(\\w+/)+\\w+") { it[0].replace('/', File.separator) }
    }
}
