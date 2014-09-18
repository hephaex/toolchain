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
package org.gradle.internal.resource.transport.http;


import spock.lang.Specification

class JavaSystemPropertiesHttpProxySettingsTest extends Specification {
    def "proxy is not configured when proxyHost property not set"() {
        expect:
        def settings = settings(proxyHost, proxyPort, nonProxyHosts)
        settings.getProxy(requestHost) == null

        where:
        proxyHost | proxyPort | nonProxyHosts | requestHost
        null      | null      | null          | null
        null      | null      | null          | "foo"
        null      | "111"     | null          | "foo"
        null      | null      | "foo|bar|baz" | "foo"
        ""        | null      | null          | null
        ""        | ""        | null          | null
        null      | ""        | null          | null
    }

    private JavaSystemPropertiesHttpProxySettings settings(host, proxyPort, nonProxyHosts) {
        return new JavaSystemPropertiesHttpProxySettings(host, proxyPort, null, null, nonProxyHosts)
    }

    def "proxy is not configured when host is in list of nonproxy hosts"() {
        expect:
        settings("proxyHost", "111", nonProxyHosts).getProxy(host)?.host == proxyHost

        where:
        nonProxyHosts | host     | proxyHost
        null          | "foo"    | "proxyHost"
        ""            | "foo"    | "proxyHost"
        "bar"         | "foo"    | "proxyHost"
        "foo"         | "foo"    | null
        "fo"          | "foo"    | "proxyHost"
        "foo|bar|baz" | "foo"    | null
        "foo.*"       | "foo.bar"| null
        "*.bar"       | "foo.bar"| null
        "*.ba"        | "foo.bar"| "proxyHost"
        "*"           | "foo"    | null
        "*"           | "foo"    | null
        "foo.*|baz"   | "foo.bar"| null
    }

    def "uses specified port property and default port when port property not set or invalid"() {
        expect:
       settings("proxyHost", prop, null).getProxy("host").port == value

        where:
        prop     | value
        null     | 80
        ""       | 80
        "notInt" | 80
        "0"      | 0
        "111"    | 111
    }

    def "uses specified proxy user and password"() {
        expect:
        def proxy = new JavaSystemPropertiesHttpProxySettings("proxyHost", null, user, password, null).getProxy("host")
        proxy.credentials?.username == proxyUser
        proxy.credentials?.password == proxyPassword

        where:
        user     | password  | proxyUser | proxyPassword
        "user"   | "password"| "user"    | "password"
        "user"   | ""        | "user"    | ""
        ""       | "password"| null      | null
        null     | "anything"| null      | null
    }
}
