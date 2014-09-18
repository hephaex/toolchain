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

package org.gradle.integtests.samples

import org.gradle.integtests.fixtures.UserGuideSamplesRunner
import org.junit.runner.RunWith

@RunWith(UserGuideSamplesRunner.class)
class UserGuideSamplesIntegrationTest {
    /*
    Important info:

     If you're working in samples area there're gradle tasks that you should know of:
     - gradle intTestImage makes sure that the samples' resources are copied to the right place
     - gradle docs:userguideDocbook makes sure that samples' info is extracted from XMLs
     - the 'expected' content of the asserting mechanism lives under docs/samples/userguideOutput

    */
}