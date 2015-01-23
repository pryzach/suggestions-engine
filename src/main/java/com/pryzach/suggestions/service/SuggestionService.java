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

package com.pryzach.suggestions.service;

import com.pryzach.suggestions.model.Word;

import java.util.Collection;
import java.util.Map;

/**
 * Suggestion service, matches added words {@link #addWord(com.pryzach.suggestions.model.Word)} via {@link #suggest(String, String, int)}
 */
public interface SuggestionService {
    /**
     * Adds words to the suggestion engine. Computationally intensive operation
     *
     * @param word word to add
     */
    public void addWords(Collection<Word> word);

    /**
     * Adds word to the suggestion engine. Computationally intensive operation
     *
     * @param word word to add
     */
    public void addWord(Word word);

    /**
     * Suggests words, returns:
     *  {suggestions: "word&lt;separator&gt;word&lt;separator&gt;", nextLetters: "letter&lt;separator&gt;letter&lt;separator&gt;"}
     *
     * @param selector word to which we suggest match
     * @param separator separator for serialization/de-serialization purposes
     * @param limit limit to how much matches to send back
     * @return map with two strings - one with word suggestions and second string is next letter suggestions, items in both strings are separated by separator
     */
    public Map<String, String> suggest(String selector, String separator, int limit);

}
