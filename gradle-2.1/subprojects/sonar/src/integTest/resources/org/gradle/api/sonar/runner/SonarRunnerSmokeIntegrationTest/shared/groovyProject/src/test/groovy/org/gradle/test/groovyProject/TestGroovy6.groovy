package org.gradle.test.groovyProject

import static org.junit.Assert.assertEquals

public class TestGroovy6 {
    private final ProductionGroovy6 production = new ProductionGroovy6("value");

    @org.junit.Test
    public void test() {
        assertEquals(production.getProperty(), "value");
    }
}