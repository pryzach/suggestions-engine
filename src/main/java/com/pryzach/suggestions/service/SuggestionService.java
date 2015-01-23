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
     *  "word&lt;separator&gt;word&lt;separator&gt;"
     *
     * @param selector word to which we suggest match
     * @param separator separator for serialization/de-serialization purposes
     * @param limit limit to how much matches to send back
     * @return string, separated by separator specified as a parameter, with words suggestions
     */
    public String suggest(String selector, String separator, int limit);

    /**
     * Suggests words, returns:
     *  [word, word]
     *
     * @param selector word to which we suggest match
     * @param limit limit to how much matches to send back
     * @return array with words suggestions
     */
    public String[] suggest(String selector, int limit);

    /**
     * Suggests next letters, returns: "letter&lt;separator&gt;letter&lt;separator&gt;"
     *
     * @param selector word to which we suggest match
     * @param suggestedWords string, separated by separator, with words suggestions
     * @param separator separator for serialization/de-serialization purposes
     * @return string of next letter suggestions separated by separator
     */
    public String suggestNextLetter(String selector, String suggestedWords, String separator);

    /**
     * Suggests words, returns: [letter, letter]]
     *
     * @param selector word to which we suggest match
     * @param suggestedWords array with words suggestions
     * @return array of next letter suggestions
     */
    public String[] suggestNextLetter(String selector, String[] suggestedWords);
}
