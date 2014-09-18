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

package org.gradle.api.internal.tasks.options

import org.gradle.api.Project
import spock.lang.Specification

class OptionReaderTest extends Specification {

    OptionReader reader
    Project project

    def setup() {
        reader = new OptionReader()
    }

    def "can read options linked to setter methods of a task"() {
        when:
        List<InstanceOptionDescriptor> options = reader.getOptions(new TestClass1())
        then:
        options[0].name == "aFlag"
        options[0].description == "simple flag"
        options[0].argumentType == Void.TYPE
        options[0].optionElement.elementName == "setActive"
        options[0].availableValues == []

        options[1].name == "booleanValue"
        options[1].description == "boolean value"
        options[1].argumentType == Void.TYPE
        options[1].optionElement.elementName == "setBooleanValue"
        options[1].availableValues == []

        options[2].name == "enumValue"
        options[2].description == "enum value"
        options[2].argumentType == TestEnum
        options[2].optionElement.elementName == "setEnumValue"
        options[2].availableValues == ["ABC", "DEF"]

        options[3].name == "objectValue"
        options[3].description == "object value"
        options[3].argumentType == Object
        options[3].optionElement.elementName == "setObjectValue"
        options[3].availableValues == []

        options[4].name == "stringValue"
        options[4].description == "string value"
        options[4].argumentType == String
        options[4].optionElement.elementName == "setStringValue"
        options[4].availableValues == ["dynValue1", "dynValue2"]

    }

    def "fail when multiple methods define same option"() {
        when:
        reader.getOptions(new TestClass2())
        then:
        def e = thrown(OptionValidationException)
        e.message == "@Option 'stringValue' linked to multiple elements in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass2'."
    }

    def "fails on static methods"() {
        when:
        reader.getOptions(new TestClass31())
        then:
        def e = thrown(OptionValidationException)
        e.message == "@Option on static method 'setStaticString' not supported in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass31'."
    }

    def "fails on static fields"() {
        when:
        reader.getOptions(new TestClass32())
        then:
        def e = thrown(OptionValidationException)
        e.message == "@Option on static field 'staticField' not supported in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass32'."
    }

    def "fail when parameter cannot be converted from the command-line"() {
        when:
        reader.getOptions(new TestClass5())
        then:
        def e = thrown(OptionValidationException)
        e.message == "Option 'fileValue' cannot be casted to type 'java.io.File' in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass5'."
    }

    def "fails when method has > 1 parameter"() {
        when:
        reader.getOptions(new TestClass4());
        then:
        def e = thrown(OptionValidationException)
        e.message == "Option 'stringValue' cannot be linked to methods with multiple parameters in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass4#setStrings'."
    }

    def "handles field options"() {
        when:
        List<InstanceOptionDescriptor> options = reader.getOptions(new TestClass6())
        then:
        options[0].name == "customOptionName"
        options[0].description == "custom description"
        options[0].argumentType == String

        options[1].name == "field2"
        options[1].description == "Descr Field2"
        options[1].argumentType == String
        options[1].availableValues == ["dynValue1", "dynValue2"]

        options[2].name == "field3"
        options[2].description == "Descr Field3"
        options[2].argumentType == TestEnum
        options[2].availableValues as Set == ["ABC", "DEF"] as Set

        options[3].name == "field4"
        options[3].description == "Descr Field4"
        options[3].argumentType == Void.TYPE
        options[3].availableValues.isEmpty()
    }


    def "throws decent error when description not set"() {
        when:
        reader.getOptions(new TestClass7());
        then:
        def e = thrown(OptionValidationException)
        e.message == "No description set on option 'aValue' at for class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass7'."

        when:
        reader.getOptions(new TestClass8());
        then:
        e = thrown(OptionValidationException)
        e.message == "No description set on option 'field' at for class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass8'."
    }

    def "throws decent error when method annotated without option name set"() {
        when:
        reader.getOptions(new TestClass9());
        then:
        def e = thrown(OptionValidationException)
        e.message == "No option name set on 'setStrings' in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass9'."
    }

    def "throws decent error when private field is annotated as option and no setter declared"() {
        when:
        reader.getOptions(new TestClass10())
        then:
        def e = thrown(OptionValidationException)
        e.message == "No setter for Option annotated field 'field' in class 'class org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass10'."
    }

    def "throws decent error for invalid OptionValues annotated methods"() {
        when:
        reader.getOptions(new WithInvalidSomeOptionMethod());
        then:
        def e = thrown(OptionValidationException)
        e.message == "@OptionValues annotation not supported on method 'getValues' in class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$WithInvalidSomeOptionMethod'. Supported method must be non-static, return a Collection<String> and take no parameters.";

        when:
        reader.getOptions(new TestClass8());
        then:
        e = thrown(OptionValidationException)
        e.message == "No description set on option 'field' at for class 'org.gradle.api.internal.tasks.options.OptionReaderTest\$TestClass8'."
    }

    public static class TestClass1{
        @Option(option = "stringValue", description = "string value")
        public void setStringValue(String value) {
        }

        @Option(option = "objectValue", description = "object value")
        public void setObjectValue(Object value) {
        }

        @Option(option = "booleanValue", description = "boolean value")
        public void setBooleanValue(boolean value) {
        }

        @Option(option = "enumValue", description = "enum value")
        public void setEnumValue(TestEnum value) {
        }

        @Option(option = "aFlag", description = "simple flag")
        public void setActive() {
        }

        @OptionValues("stringValue")
        public Collection<CustomClass> getAvailableValues() {
            return Arrays.asList(new CustomClass(value: "dynValue1"), new CustomClass(value: "dynValue2"))
        }
    }

    public static class CustomClass {
        String value

        public String toString() {
            value
        }
    }

    public static class TestClass2{
        @Option(option = "stringValue", description = "string value")
        public void setStringValue(String value) {
        }

        @Option(option = "stringValue", description = "string value")
        public void setStringValue2(String value) {
        }
    }

    public static class TestClass31{
        @Option(option = "staticString", description = "string value")
        public static void setStaticString(String value) {
        }
    }

    public static class TestClass32{
        @Option(description = "staticOption")
        static String staticField
    }

    public static class TestClass4{
        @Option(option = 'stringValue', description = "string value")
        public void setStrings(String value1, String value2) {
        }
    }

    public static class TestClass5{
        @Option(option = 'fileValue', description = "file value")
        public void setStrings(File file) {
        }
    }

    public static class TestClass6{
        @Option(option = 'customOptionName', description = "custom description")
        String field1

        @Option(description = "Descr Field2")
        String field2

        @Option(description = "Descr Field3")
        TestEnum field3

        @Option(description = "Descr Field4")
        boolean field4


        @OptionValues("field2")
        List<String> getField2Options() {
            return Arrays.asList("dynValue1", "dynValue2")
        }
    }

    public static class TestClass7{
        @Option(option = 'aValue')
        public void setStrings(String value) {
        }
    }

    public static class TestClass8{
        @Option
        String field
    }

    public static class TestClass9 {
        @Option(description = "some description")
        public void setStrings(String value) {
        }
    }

    public static class TestClass10{
        @Option(description = "some description")
        private String field
    }

    public static class WithInvalidSomeOptionMethod {
        @OptionValues("someOption")
        List<String> getValues(String someParam) { return Arrays.asList("something")}
    }

    public static class WithDuplicateSomeOptions {
        @OptionValues("someOption")
        List<String> getValues() { return Arrays.asList("something")}

        @OptionValues("someOption")
        List<String> getValues2() { return Arrays.asList("somethingElse")}
    }

    public static class WithAnnotatedStaticMethod {
        @OptionValues("someOption")
        static List<String> getValues(String someParam) { return Arrays.asList("something")}
    }


    public class SomeOptionValues{
        @OptionValues("someOption")
        List<String> getValues() { return Arrays.asList("something")}
    }

    enum TestEnum {
        ABC, DEF
    }
}


