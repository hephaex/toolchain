/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.external.javadoc.internal;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StringsJavadocOptionFileOptionTest {

    private final JUnit4Mockery context = new JUnit4Mockery();
    private JavadocOptionFileWriterContext writerContextMock;
    private final String optionName = "testOption";
    private final String joinBy = ";";

    private StringsJavadocOptionFileOption stringsOption;

    @Before
    public void setUp() {
        context.setImposteriser(ClassImposteriser.INSTANCE);
        writerContextMock = context.mock(JavadocOptionFileWriterContext.class);

        stringsOption = new StringsJavadocOptionFileOption(optionName, joinBy);
    }

    @Test
    public void writeNullValue() throws IOException {
        stringsOption.write(writerContextMock);
    }

    @Test
    public void writeNoneNullValue() throws IOException {
        final String valueOne = "valueOne";
        final String valueTwo = "valueTwo";

        stringsOption.getValue().add(valueOne);
        stringsOption.getValue().add(valueTwo);

        context.checking(new Expectations() {{
            one(writerContextMock).writeValuesOption(optionName, stringsOption.getValue(), joinBy);
        }});

        stringsOption.write(writerContextMock);
    }
}
