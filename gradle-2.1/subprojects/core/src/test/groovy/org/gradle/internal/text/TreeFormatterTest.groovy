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

package org.gradle.internal.text

import spock.lang.Specification

import static org.gradle.util.TextUtil.toPlatformLineSeparators

class TreeFormatterTest extends Specification {
    def formatter = new TreeFormatter()

    def "formats single node"() {
        when:
        formatter.node("Some thing.")

        then:
        formatter.toString() == "Some thing."
    }

    def "formats root with no children"() {
        when:
        formatter.node("Some thing.")
        formatter.startChildren()
        formatter.endChildren()

        then:
        formatter.toString() == "Some thing."
    }

    def "formats root with single leaf child"() {
        when:
        formatter.node("Some things")
        formatter.startChildren()
        formatter.node("child 1")
        formatter.endChildren()

        then:
        formatter.toString() == 'Some things: child 1'
    }

    def "formats root with single nested leaf child"() {
        when:
        formatter.node("Some things")
        formatter.startChildren()
        formatter.node("child 1")
        formatter.startChildren()
        formatter.node("child 2")
        formatter.endChildren()
        formatter.endChildren()

        then:
        formatter.toString() == toPlatformLineSeparators("""Some things:
  - child 1: child 2""")
    }

    def "formats root with single child with multiple children"() {
        when:
        formatter.node("Some things")
        formatter.startChildren()
        formatter.node("child 1")
        formatter.startChildren()
        formatter.node("child 1.1")
        formatter.node("child 1.2")
        formatter.endChildren()
        formatter.endChildren()

        then:
        formatter.toString() == toPlatformLineSeparators("""Some things: child 1:
  - child 1.1
  - child 1.2""")
    }

    def "formats root with multiple children"() {
        when:
        formatter.node("Some things")
        formatter.startChildren()
        formatter.node("child 1")
        formatter.node("child 2")
        formatter.endChildren()

        then:
        formatter.toString() == toPlatformLineSeparators("""Some things:
  - child 1
  - child 2""")
    }

    def "formats nested children"() {
        when:
        formatter.node("Some things")
        formatter.startChildren()
        formatter.node("child 1")
        formatter.startChildren()
        formatter.node("child 1.1")
        formatter.node("child 1.2")
        formatter.endChildren()
        formatter.node("child 2")
        formatter.endChildren()

        then:
        formatter.toString() == toPlatformLineSeparators("""Some things:
  - child 1:
      - child 1.1
      - child 1.2
  - child 2""")
    }

    def "indents nested children that span multiple lines"() {
        when:
        formatter.node(toPlatformLineSeparators("Multiple\nlines"))
        formatter.startChildren()
        formatter.node("child 1")
        formatter.startChildren()
        formatter.node(toPlatformLineSeparators("multiple\nlines"))
        formatter.node(toPlatformLineSeparators("another\nchild"))
        formatter.endChildren()
        formatter.node(toPlatformLineSeparators("one\ntwo"))
        formatter.endChildren()

        then:
        formatter.toString() == toPlatformLineSeparators("""Multiple
lines:
  - child 1:
      - multiple
        lines
      - another
        child
  - one
    two""")
    }
}
