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

package org.gradle.model.internal.inspect

import org.gradle.model.InvalidModelRuleDeclarationException
import org.gradle.util.Requires
import org.gradle.util.TestPrecondition
import spock.lang.Specification
import spock.lang.Unroll

@Requires(TestPrecondition.NOT_JDK_IBM)
class ClassModelRuleSourceValidationTest extends Specification {

    @Unroll
    def "invalid #type - #reason"() {
        when:
        new ModelRuleInspector().validate(type)

        then:
        def e = thrown(InvalidModelRuleDeclarationException)
        def message = e.message
        def actualReason = message.split(":", 2)[1].trim()
        actualReason == reason

        where:
        type                            | reason
        OuterClass.AbstractClass        | "class cannot be abstract"
        OuterClass.AnInterface          | "must be a class, not an interface"
        OuterClass.InnerInstanceClass   | "enclosed classes must be static and non private"
        new Object() {}.getClass()      | "enclosed classes must be static and non private"
        OuterClass.HasSuperclass        | "cannot have superclass"
        OuterClass.HasTwoConstructors   | "cannot have more than one constructor"
        OuterClass.HasInstanceVar       | "field foo is not static final"
        OuterClass.HasFinalInstanceVar  | "field foo is not static final"
        OuterClass.HasNonFinalStaticVar | "field foo is not static final"
    }

    @Unroll
    def "valid #type"() {
        when:
        new ModelRuleInspector().validate(type)

        then:
        noExceptionThrown()

        where:
        type << [
                OuterClass.InnerPublicStaticClass,
                OuterClass.HasExplicitDefaultConstructor,
                OuterClass.HasStaticFinalField
        ]
    }
}
