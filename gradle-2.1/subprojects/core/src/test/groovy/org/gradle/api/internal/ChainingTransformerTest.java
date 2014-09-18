/*
 * Copyright 2008 the original author or authors.
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
package org.gradle.api.internal;

import org.gradle.util.TestUtil;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.Expectations;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.gradle.api.Transformer;
import groovy.lang.Closure;

@RunWith(JMock.class)
public class ChainingTransformerTest {
    private final JUnit4Mockery context = new JUnit4Mockery();
    private final ChainingTransformer<String> transformer = new ChainingTransformer<String>(String.class);

    @Test
    public void doesNothingWhenNoTransformersAdded() {
        assertThat(transformer.transform("value"), equalTo("value"));
    }

    @Test
    public void passesObjectToEachTransformerInTurn() {
        @SuppressWarnings("unchecked")
        final Transformer<String, String> transformerA = context.mock(Transformer.class, "transformerA");
        @SuppressWarnings("unchecked")
        final Transformer<String, String> transformerB = context.mock(Transformer.class, "transformerB");

        context.checking(new Expectations(){{
            one(transformerA).transform("original");
            will(returnValue("a"));

            one(transformerB).transform("a");
            will(returnValue("b"));
        }});

        transformer.add(transformerA);
        transformer.add(transformerB);

        assertThat(transformer.transform("original"), equalTo("b"));
    }

    @Test
    public void canUseAClosureAsATransformer() {
        Closure closure = TestUtil.toClosure("{ it + ' transformed' }");

        transformer.add(closure);

        assertThat(transformer.transform("original"), equalTo("original transformed"));
    }

    @Test
    public void usesOriginalObjectWhenClosureReturnsNull() {
        Closure closure = TestUtil.toClosure("{ null }");

        transformer.add(closure);

        assertThat(transformer.transform("original"), equalTo("original"));
    }

    @Test
    public void usesOriginalObjectWhenClosureReturnsObjectOfUnexpectedType() {
        Closure closure = TestUtil.toClosure("{ 9 }");

        transformer.add(closure);

        assertThat(transformer.transform("original"), equalTo("original"));
    }

    @Test
    public void originalObjectIsSetAsDelegateForClosure() {
        Closure closure = TestUtil.toClosure("{ substring(1, 3) }");

        transformer.add(closure);

        assertThat(transformer.transform("original"), equalTo("ri"));
    }
    
    @Test
    public void closureCanTransformAStringIntoAGString() {
        Closure closure = TestUtil.toClosure("{ \"[$it]\" }");

        transformer.add(closure);

        assertThat(transformer.transform("original"), equalTo("[original]"));
    }
}
