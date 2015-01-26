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

package com.pryzach.suggestions.service.impl;

import com.pryzach.suggestions.constants.SuggestionConstants;
import com.pryzach.suggestions.model.Word;
import com.pryzach.suggestions.model.WordRTL;
import com.pryzach.suggestions.service.SuggestionService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link com.pryzach.suggestions.service.SuggestionService} . Uses regex to match words
 *
 * CAUTION due to proof of concept type of implementation {@link #addWord(com.pryzach.suggestions.model.Word)} recalculates
 * cache all the time every time and {@link #addWords(java.util.Collection)} doesn't do bulk cache update
 *
 * This implementation is case insensitive (more computationally intensive, but closer to real-life requirements)
 */
public class SuggestionServiceImpl implements SuggestionService {
    /**
     * Minimal length starting from which suggestions would be offered (min 1)
     */
    private final int minLength;

    /**
     * Max caching length. Essentially optimization to shard cache until it becomes more manageable
     */
    private final int maxCachingLength;

    private final Map<String, String> cache;
    private final Map<String, Set<Word>> wordsCache;

    public SuggestionServiceImpl () {
        // min length = 1, matches would be produced always.
        // max caching length = average english word length + 1 (http://www.wolframalpha.com/input/?i=average+english+word+length)
        this(1, 6);
    }

    /**
     * Creates new {@link com.pryzach.suggestions.service.SuggestionService} instance
     *
     * @param minLength minimal selector length after which matches would be offered
     * @param maxCachingLength how long maximum partial cache key string should be. Essentially - have a lot of long words - set higher, otherwise - set lower.
     */
    public SuggestionServiceImpl(int minLength, int maxCachingLength) {
        this.minLength = minLength;
        this.maxCachingLength = maxCachingLength;

        this.cache = new HashMap<>();
        this.wordsCache = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWords(Collection<Word> words) {
        Map<String, Set<Word>> wordsCachePartial = new HashMap<>();

        for (Word word : words) {

            String wordNameLowercase = word.getName().toLowerCase();
            StringBuilder cacheKeyPartial = new StringBuilder( (this.minLength > 1 ? wordNameLowercase.substring(0, this.minLength - 1) : "") );
            for (int i = this.minLength - 1; i < Math.min(wordNameLowercase.length(), this.maxCachingLength); i++) {
                //String cacheKeyPartial = word.getName().substring(0, i + 1).toLowerCase();
                cacheKeyPartial.append(wordNameLowercase.charAt(i));

                if (!wordsCachePartial.containsKey(cacheKeyPartial.toString())) {
                    wordsCachePartial.put(cacheKeyPartial.toString(), new TreeSet<Word>(Collections.reverseOrder()));
                }

                wordsCachePartial.get(cacheKeyPartial.toString()).add(word);
            }
        }

        for (String cacheKeyPartial : wordsCachePartial.keySet()) {

            if (!this.wordsCache.containsKey(cacheKeyPartial)) {
                this.wordsCache.put(cacheKeyPartial, new TreeSet<Word>(Collections.reverseOrder()));
            }

            // merging partial and full cache
            this.wordsCache.get(cacheKeyPartial).addAll(wordsCachePartial.get(cacheKeyPartial));

            StringBuilder cacheUpdate = new StringBuilder(SuggestionConstants.WORDS_CACHE_SEPARATOR);
            // updating cache
            for (Word word : this.wordsCache.get(cacheKeyPartial)) {
                cacheUpdate.append(word.getName()).append(SuggestionConstants.WORDS_CACHE_SEPARATOR);

                if (SuggestionConstants.CACHE_STRING_LENGTH_LIMIT > 0 && cacheUpdate.length() > SuggestionConstants.CACHE_STRING_LENGTH_LIMIT) {
                    break;
                }
            }

            this.cache.put(cacheKeyPartial, cacheUpdate.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWord(Word word) {
        addWords(Arrays.asList(word));
    }

    /**
     * {@inheritDoc}
     */
    public String[] suggest(String selector, int limit) {
        Set<String> suggestions = new LinkedHashSet<>();

        if (selector.length() >= this.minLength) {

            if (selector.length() > this.maxCachingLength) {
                String cacheKeyPartial = selector.substring(0, this.maxCachingLength);

                if (this.cache.get(cacheKeyPartial.toLowerCase()) != null) {

                    Matcher matcher = Pattern.compile("(?<=[" + SuggestionConstants.WORDS_CACHE_SEPARATOR + "])" + selector + "[a-zA-Z-_ ]+?" + "(?=[" + SuggestionConstants.WORDS_CACHE_SEPARATOR + "])", Pattern.CASE_INSENSITIVE).matcher(this.cache.get(cacheKeyPartial.toLowerCase()));

                    String matchedWord;
                    for (int i = 0; matcher.find() && i < limit; i++) {
                        matchedWord = matcher.group();

                        suggestions.add(matchedWord);

                    }
                }
            } else {
                if (this.cache.get(selector.toLowerCase()) != null) {
                    Set<Word> cacheWords = this.wordsCache.get(selector.toLowerCase());

                    int i = 0;
                    for (Word cacheWord : cacheWords) {

                        if (!(i < limit && i < cacheWords.size())) {
                            break;
                        }

                        suggestions.add(cacheWord.getName());

                        i++;
                    }
                }
            }

        }

        return suggestions.toArray(SuggestionConstants.TO_STRING_ARRAY_HELPER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WordRTL[] suggest(WordRTL selector, int limit) {
        String[] suggestedWordsStringArray = this.suggest(selector.getName(), limit);
        WordRTL[] suggestedWordsArray = new WordRTL[suggestedWordsStringArray.length];

        for (int i = 0; i < suggestedWordsStringArray.length; i++) {
            suggestedWordsArray[i] = new WordRTL(suggestedWordsStringArray[i], suggestedWordsStringArray.length - i);
        }

        return suggestedWordsArray;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String suggest(WordRTL selector, String separator, int limit) {
        WordRTL[] suggestedWordsArray = this.suggest(selector, limit);

        StringBuilder suggestedWordsString = new StringBuilder("");

        for (int i = 0; i < suggestedWordsArray.length; i++) {
            suggestedWordsString.append(suggestedWordsArray[i].getName());

            if (i + 1 < suggestedWordsArray.length) {
                suggestedWordsString.append(separator);
            }
        }

        return suggestedWordsString.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String suggest(String selector, String separator, int limit) {
        return String.join(separator, this.suggest(selector, limit));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] suggestNextLetter(String selector, String[] suggestedWords) {
        Set<String> suggestedNextLetters = new LinkedHashSet<>();
        int nextLetterPosition = selector.length();

        String nextLetter;
        for (int i = 0; i < suggestedWords.length; i++) {
            if (suggestedWords[i].length() > nextLetterPosition) {
                suggestedNextLetters.add(Character.toString(suggestedWords[i].charAt(nextLetterPosition)));
            }

        }

        return suggestedNextLetters.toArray(SuggestionConstants.TO_STRING_ARRAY_HELPER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String suggestNextLetter(String selector, String suggestedWords, String separator) {
        return String.join(separator, this.suggestNextLetter(selector, suggestedWords.split("[" + separator + "]")));
    }

}
