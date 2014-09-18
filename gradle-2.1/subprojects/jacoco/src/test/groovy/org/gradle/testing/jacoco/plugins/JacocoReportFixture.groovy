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

package org.gradle.testing.jacoco.plugins

import org.gradle.test.fixtures.file.TestFile
import org.jsoup.Jsoup

class JacocoReportFixture {
    private final TestFile htmlDir

    JacocoReportFixture(TestFile htmlDir) {
        this.htmlDir = htmlDir
    }

    public boolean exists() {
        htmlDir.file("index.html").exists()
    }

    public String jacocoVersion() {
        def parsedHtmlReport = Jsoup.parse(htmlDir.file("index.html"), "UTF-8")
        def footer = parsedHtmlReport.select("div.footer:has(a[href=http://www.eclemma.org/jacoco])")
        String text = footer.text()
        return text.startsWith("Created with JaCoCo ") ? text.substring(20) : text
    }

    public BigDecimal totalCoverage() {
        def parsedHtmlReport = Jsoup.parse(htmlDir.file("index.html"), "UTF-8")
        def table = parsedHtmlReport.select("table#coveragetable").first()
        def td = table.select("tfoot td:eq(2)").first()
        String totalCoverage = td.text()
        return totalCoverage.endsWith("%") ? totalCoverage.subSequence(0, totalCoverage.length() -1) as BigDecimal : null
    }
}
