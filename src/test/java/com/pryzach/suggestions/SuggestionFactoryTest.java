/*
 * Copyright 2015 Zachar Prychoda
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.pryzach.suggestions;

import com.pryzach.suggestions.service.SuggestionService;
import junit.framework.Assert;
import junit.framework.TestCase;

public class SuggestionFactoryTest extends TestCase {

    public void testGetSuggestionService() throws Exception {
        Assert.assertTrue(SuggestionFactory.getSuggestionService() instanceof SuggestionService);
    }
}