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

package com.pryzach.suggestions.model;

import com.pryzach.suggestions.constants.SuggestionConstants;

/**
 * Words model. Implements {@link java.lang.Comparable} to be used in {@link java.util.SortedSet} for efficient ranking
 */
public class Word implements Comparable {
    private final String name;
    private final int popularityIndex;

    /**
     * Default constructor
     *
     * @param name dictionary word
     * @param popularityIndex popularity index (influences order)
     */
    public Word(String name, int popularityIndex) {
        // cleaning all possible conflicts with internal separator
        this.name = name.replace(SuggestionConstants.WORDS_CACHE_SEPARATOR, "|");
        this.popularityIndex = popularityIndex;
    }

    /**
     * @return actual word
     */
    public String getName() {
        return name;
    }

    /**
     * @return popularity index
     */
    public int getPopularityIndex() {
        return popularityIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (popularityIndex != word.popularityIndex) return false;
        if (!name.equalsIgnoreCase(word.name)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + popularityIndex;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Word) {
            Word comparable = (Word) o;

            if (this.popularityIndex - comparable.popularityIndex == 0) {
                return this.getName().compareTo(comparable.getName());
            } else {
                return this.popularityIndex - comparable.popularityIndex;
            }
        } else {
            // put all other classes below
            return -1;
        }
    }
}
