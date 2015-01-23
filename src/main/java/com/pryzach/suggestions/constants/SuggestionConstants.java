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

package com.pryzach.suggestions.constants;

/**
 * Suggestion service constants. In future maybe better be replaced with some kind of configuration
 * to be set upon {@link com.pryzach.suggestions.service.impl.SuggestionServiceImpl} construction
 */
public class SuggestionConstants {
    /**
     * Separator is used to serialize/de-serialize words in cache
     */
    public static String WORDS_CACHE_SEPARATOR = "|";

    /**
     * Limit, in bytes, on how long cache string can be
     */
    public static int CACHE_STRING_LENGTH_LIMIT = 0;

    public static String[] TO_STRING_ARRAY_HELPER = new String[] {};
}
