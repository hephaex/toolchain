/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.api.internal.file;

import groovy.lang.Closure;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.internal.file.collections.FileCollectionResolveContext;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.util.TestUtil;
import static org.gradle.util.WrapUtil.*;
import static org.hamcrest.Matchers.*;

import org.gradle.util.JUnit4GroovyMockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class CompositeFileTreeTest {
    private final JUnit4Mockery context = new JUnit4GroovyMockery();
    private final FileTree source1 = context.mock(FileTree.class);
    private final FileTree source2 = context.mock(FileTree.class);
    private final CompositeFileTree tree = new CompositeFileTree() {
        @Override
        public String getDisplayName() {
            return "<display-name>";
        }

        @Override
        public void resolve(FileCollectionResolveContext context) {
            context.add(source1);
            context.add(source2);
        }
    };

    @Test
    public void matchingWithClosureReturnsUnionOfFilteredSets() {
        final Closure closure = TestUtil.TEST_CLOSURE;
        final FileTree filtered1 = context.mock(FileTree.class);
        final FileTree filtered2 = context.mock(FileTree.class);

        context.checking(new Expectations() {{
            one(source1).matching(closure);
            will(returnValue(filtered1));
            one(source2).matching(closure);
            will(returnValue(filtered2));
        }});

        FileTree filtered = tree.matching(closure);
        assertThat(filtered, instanceOf(CompositeFileTree.class));
        CompositeFileTree filteredCompositeSet = (CompositeFileTree) filtered;

        assertThat(toList(filteredCompositeSet.getSourceCollections()), equalTo(toList((FileTree)filtered1, filtered2)));
    }

    @Test
    public void matchingWithPatternSetReturnsUnionOfFilteredSets() {
        final PatternSet patternSet = new PatternSet();
        final FileTree filtered1 = context.mock(FileTree.class);
        final FileTree filtered2 = context.mock(FileTree.class);

        context.checking(new Expectations() {{
            one(source1).matching(patternSet);
            will(returnValue(filtered1));
            one(source2).matching(patternSet);
            will(returnValue(filtered2));
        }});

        FileTree filtered = tree.matching(patternSet);
        assertThat(filtered, instanceOf(CompositeFileTree.class));
        CompositeFileTree filteredCompositeSet = (CompositeFileTree) filtered;

        assertThat(toList(filteredCompositeSet.getSourceCollections()), equalTo(toList((FileTree) filtered1, filtered2)));
    }

    @Test
    public void plusReturnsUnionOfThisTreeAndSourceTree() {
        FileTree other = context.mock(FileTree.class);

        FileTree sum = tree.plus(other);
        assertThat(sum, instanceOf(CompositeFileTree.class));
        UnionFileTree sumCompositeTree = (UnionFileTree) sum;
        assertThat(sumCompositeTree.getSourceCollections(), equalTo((Iterable) toList(source1, source2, other)));
    }

    @Test
    public void visitsEachTreeWithVisitor() {
        final FileVisitor visitor = context.mock(FileVisitor.class);

        context.checking(new Expectations() {{
            one(source1).visit(visitor);
            one(source2).visit(visitor);
        }});

        tree.visit(visitor);
    }

    @Test
    public void visitsEachTreeWithClosure() {
        final Closure visitor = TestUtil.TEST_CLOSURE;

        context.checking(new Expectations() {{
            one(source1).visit(visitor);
            one(source2).visit(visitor);
        }});

        tree.visit(visitor);
    }
}
