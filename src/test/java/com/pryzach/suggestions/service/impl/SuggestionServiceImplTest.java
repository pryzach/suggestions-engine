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

import com.pryzach.suggestions.SuggestionFactory;
import com.pryzach.suggestions.model.Word;
import com.pryzach.suggestions.service.SuggestionService;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.*;

public class SuggestionServiceImplTest extends TestCase {
    private final Random random = new Random();

    public void testAddWords() throws Exception {
        SuggestionService suggestionService = SuggestionFactory.getSuggestionService();
        Map<String, String> result;

        // two success words is deliberate
        suggestionService.addWords(new HashSet<>(Arrays.asList(new Word("success", 10), new Word("success", 10), new Word("succubus-long_one here", 9), new Word("success-very-unpopular", 1))));

        result = suggestionService.suggest("succ", "|", 2);

        Assert.assertEquals("success|succubus-long_one here|", result.get("suggestions"));
        Assert.assertEquals("e|u|", result.get("nextLetters"));
    }

    public void testSuggestEngine() throws Exception {
        SuggestionService suggestionService = SuggestionFactory.getSuggestionService();
        Map<String, String> result;

        result = suggestionService.suggest("succes", "|", 10);

        Assert.assertEquals("", result.get("suggestions"));
        Assert.assertEquals("", result.get("nextLetters"));

        suggestionService.addWord(new Word("success", 10));

        result = suggestionService.suggest("succes", "|", 10);

        Assert.assertEquals("success|", result.get("suggestions"));
        Assert.assertEquals("s|", result.get("nextLetters"));

        suggestionService.addWord(new Word("confusuccess", 10));

        result = suggestionService.suggest("succ", "|", 10);

        Assert.assertEquals("success|", result.get("suggestions"));
        Assert.assertEquals("e|", result.get("nextLetters"));

        suggestionService.addWord(new Word("succubus-long_one here", 1));

        result = suggestionService.suggest("succ", "|", 10);

        Assert.assertEquals("success|succubus-long_one here|", result.get("suggestions"));
        Assert.assertEquals("e|u|", result.get("nextLetters"));

        suggestionService.addWord(new Word("sucks-if-this-will-appear-in-results", 10));

        result = suggestionService.suggest("succ", "|", 10);

        Assert.assertEquals("success|succubus-long_one here|", result.get("suggestions"));
        Assert.assertEquals("e|u|", result.get("nextLetters"));

        suggestionService.addWord(new Word("success-very-popular", 100));

        result = suggestionService.suggest("succ", "|", 10);

        Assert.assertEquals("success-very-popular|success|succubus-long_one here|", result.get("suggestions"));
        Assert.assertEquals("e|u|", result.get("nextLetters"));

        result = suggestionService.suggest("succ", "|", 2);

        Assert.assertEquals("success-very-popular|success|", result.get("suggestions"));
        Assert.assertEquals("e|", result.get("nextLetters"));

        // starting to test selectors which exceed caching max length
        result = suggestionService.suggest("success-very-", "|", 2);

        Assert.assertEquals("success-very-popular|", result.get("suggestions"));
        Assert.assertEquals("p|", result.get("nextLetters"));

        suggestionService.addWord(new Word("success-very-long", 101));

        result = suggestionService.suggest("success-very-", "|", 2);

        Assert.assertEquals("success-very-long|success-very-popular|", result.get("suggestions"));
        Assert.assertEquals("l|p|", result.get("nextLetters"));

        suggestionService.addWord(new Word("success-very-boring", 1));

        result = suggestionService.suggest("success-very-", "|", 2);

        Assert.assertEquals("success-very-long|success-very-popular|", result.get("suggestions"));
        Assert.assertEquals("l|p|", result.get("nextLetters"));

        // confirmation that white spaces are supported
        suggestionService.addWord(new Word("success with whitespaces", 1));

        result = suggestionService.suggest("success", "|", 4);

        Assert.assertEquals("success-very-long|success-very-popular|success-very-boring|success with whitespaces|", result.get("suggestions"));
        Assert.assertEquals("-| |", result.get("nextLetters"));

        // confirmation that implementation is case insensitive
        suggestionService.addWord(new Word("SucCEss_with-different case", 2));

        result = suggestionService.suggest("success", "|", 5);

        Assert.assertEquals("success-very-long|success-very-popular|SucCEss_with-different case|success-very-boring|success with whitespaces|", result.get("suggestions"));
        Assert.assertEquals("-|_| |", result.get("nextLetters"));
    }

    public void testSuggestEnginePerformanceAndReliabilityFullOxfordTest() {
        System.out.println("The Second Edition of the 20-volume  Oxford English Dictionary contains full entries for 171,476 words in current use");
        System.out.println("Started adding 171k words, please wait...");

        performanceTest(171476);
    }

    public void testSuggestEnginePerformanceAndReliabilityTest() {
        System.out.println("Started adding 10k words, please wait...");

        performanceTest(10000);
    }

    private void performanceTest(long amount) {
        SuggestionService suggestionService = SuggestionFactory.getSuggestionService();
        Map<String, String> result;

        List<String> selectors = new ArrayList<>();

        String wordString;

        long startTime = new Date().getTime();

        Set<Word> words = new HashSet<>();

        for (int i = 0; i < amount; i++) {

            wordString = RandomStringUtils.randomAlphabetic(randomInt(3, 10));

            words.add(new Word(wordString, randomInt(1, 100)));

            if (randomInt(0, 5) == 2 && wordString.length() > 3) {
                selectors.add(wordString.substring(0, wordString.length() - 3));
            }
        }

        suggestionService.addWords(words);words.clear();

        System.out.println("Added to service [" + amount + "] and took [" + (new Date().getTime() - startTime) + " ms]");

        int i = 0;
        startTime = new Date().getTime();
        for (String selector : selectors) {

            result = suggestionService.suggest(selector, "|", 10);

            // uncomment below to visually confirm matches
            // if (i % 1000 == 0) System.out.println("Matched [" + selector + "] to [" + result.get("suggestions") + "]");

            Assert.assertTrue(result.get("suggestions").length() > 1);
            Assert.assertTrue(result.get("nextLetters").length() > 1);
            i++;
        }

        System.out.println("Matched: [" + selectors.size() + "] and took [" + (new Date().getTime() - startTime) + " ms] [" + new BigDecimal(((double) new Date().getTime() - startTime)/selectors.size()).setScale(5, BigDecimal.ROUND_HALF_UP).toString() + " ms] per match and [" + Math.round(1000 / (((double) new Date().getTime() - startTime)/selectors.size())) + " words] per second");
    }

    private int randomInt(int start, int end) {
        return random.nextInt(end - start) + start;
    }
}