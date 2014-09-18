/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.api.internal.artifacts.dsl;

import org.gradle.api.IllegalDependencyNotation;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.artifacts.ModuleVersionSelector;
import org.gradle.internal.typeconversion.*;

import java.util.Collection;
import java.util.Set;

import static org.gradle.api.internal.artifacts.DefaultModuleVersionSelector.newSelector;

public class ModuleVersionSelectorParsers {

    public static NotationParser<Object, Set<ModuleVersionSelector>> multiParser() {
        return builder().toFlatteningComposite();
    }

    public static NotationParser<Object, ModuleVersionSelector> parser() {
        return builder().toComposite();
    }

    private static NotationParserBuilder<ModuleVersionSelector> builder() {
        return NotationParserBuilder
                .toType(ModuleVersionSelector.class)
                .fromCharSequence(new StringParser())
                .parser(new MapParser());
    }

    static class MapParser extends MapNotationParser<ModuleVersionSelector> {
        @Override
        public void describe(Collection<String> candidateFormats) {
            candidateFormats.add("Maps, e.g. [group: 'org.gradle', name:'gradle-core', version: '1.0'].");
        }

        protected ModuleVersionSelector parseMap(@MapKey("group") String group, @MapKey("name") String name, @MapKey("version") String version) {
            return newSelector(group, name, version);
        }
    }

    static class StringParser implements NotationConverter<String, ModuleVersionSelector> {
        public void describe(Collection<String> candidateFormats) {
            candidateFormats.add("String or CharSequence values, e.g. 'org.gradle:gradle-core:1.0'.");
        }

        public void convert(String notation, NotationConvertResult<? super ModuleVersionSelector> result) throws TypeConversionException {
            ParsedModuleStringNotation parsed;
            try {
                parsed = new ParsedModuleStringNotation(notation, null);
            } catch (IllegalDependencyNotation e) {
                throw new InvalidUserDataException(
                        "Invalid format: '" + notation + "'. The correct notation is a 3-part group:name:version notation, "
                                + "e.g: 'org.gradle:gradle-core:1.0'");
            }

            if (parsed.getGroup() == null || parsed.getName() == null || parsed.getVersion() == null) {
                throw new InvalidUserDataException(
                        "Invalid format: '" + notation + "'. Group, name and version cannot be empty. Correct example: "
                                + "'org.gradle:gradle-core:1.0'");
            }
            result.converted(newSelector(parsed.getGroup(), parsed.getName(), parsed.getVersion()));
        }
    }
}