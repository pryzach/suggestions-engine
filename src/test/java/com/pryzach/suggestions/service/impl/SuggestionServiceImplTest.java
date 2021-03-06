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
import com.pryzach.suggestions.model.WordRTL;
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

        String[] suggestedWordsArray;
        String suggestedWordsString;
        String[] suggestedNextLettersArray;
        String suggestedNextLettersString;

        // two success words is deliberate
        suggestionService.addWords(new HashSet<>(Arrays.asList(new Word("success", 10), new Word("success", 10), new Word("succubus-long_one here", 9), new Word("success-very-unpopular", 1))));

        suggestedWordsString = suggestionService.suggest("succ", "|", 2);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success|succubus-long_one here", suggestedWordsString);
        Assert.assertEquals("e|u", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 2);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success", "succubus-long_one here"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e", "u"});
    }

    public void testSuggestEngine() throws Exception {
        SuggestionService suggestionService = SuggestionFactory.getSuggestionService();
        String[] suggestedWordsArray;
        String suggestedWordsString;
        String[] suggestedNextLettersArray;
        String suggestedNextLettersString;

        suggestedWordsString = suggestionService.suggest("succes", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succes", suggestedWordsString, "|");
        Assert.assertEquals("", suggestedWordsString);
        Assert.assertEquals("", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succes", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succes", suggestedWordsArray);
        Assert.assertEquals(0, suggestedWordsArray.length);
        Assert.assertEquals(0, suggestedNextLettersArray.length);

        suggestionService.addWord(new Word("success", 10));

        suggestedWordsString = suggestionService.suggest("succes", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succes", suggestedWordsString, "|");
        Assert.assertEquals("success", suggestedWordsString);
        Assert.assertEquals("s", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succes", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succes", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"s"});

        suggestionService.addWord(new Word("confusuccess", 10));

        suggestedWordsString = suggestionService.suggest("succ", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success", suggestedWordsString);
        Assert.assertEquals("e", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e"});

        suggestionService.addWord(new Word("succubus-long_one here", 1));

        suggestedWordsString = suggestionService.suggest("succ", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success|succubus-long_one here", suggestedWordsString);
        Assert.assertEquals("e|u", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success", "succubus-long_one here"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e", "u"});

        suggestionService.addWord(new Word("sucks-if-this-will-appear-in-results", 10));

        suggestedWordsString = suggestionService.suggest("succ", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success|succubus-long_one here", suggestedWordsString);
        Assert.assertEquals("e|u", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success", "succubus-long_one here"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e", "u"});

        suggestionService.addWord(new Word("success-very-popular", 100));

        suggestedWordsString = suggestionService.suggest("succ", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success-very-popular|success|succubus-long_one here", suggestedWordsString);
        Assert.assertEquals("e|u", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-popular", "success", "succubus-long_one here"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e", "u"});

        suggestedWordsString = suggestionService.suggest("succ", "|", 2);
        suggestedNextLettersString = suggestionService.suggestNextLetter("succ", suggestedWordsString, "|");
        Assert.assertEquals("success-very-popular|success", suggestedWordsString);
        Assert.assertEquals("e", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("succ", 2);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("succ", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-popular", "success"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"e"});

        // starting to test selectors which exceed caching max length
        suggestedWordsString = suggestionService.suggest("success-very-", "|", 2);
        suggestedNextLettersString = suggestionService.suggestNextLetter("success-very-", suggestedWordsString, "|");
        Assert.assertEquals("success-very-popular", suggestedWordsString);
        Assert.assertEquals("p", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("success-very-", 2);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("success-very-", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-popular"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"p"});

        suggestionService.addWord(new Word("success-very-long", 101));

        suggestedWordsString = suggestionService.suggest("success-very-", "|", 2);
        suggestedNextLettersString = suggestionService.suggestNextLetter("success-very-", suggestedWordsString, "|");
        Assert.assertEquals("success-very-long|success-very-popular", suggestedWordsString);
        Assert.assertEquals("l|p", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("success-very-", 2);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("success-very-", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-long", "success-very-popular"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"l", "p"});

        suggestionService.addWord(new Word("success-very-boring", 1));

        suggestedWordsString = suggestionService.suggest("success-very-", "|", 2);
        suggestedNextLettersString = suggestionService.suggestNextLetter("success-very-", suggestedWordsString, "|");
        Assert.assertEquals("success-very-long|success-very-popular", suggestedWordsString);
        Assert.assertEquals("l|p", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("success-very-", 2);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("success-very-", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-long", "success-very-popular"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"l", "p"});

        // confirmation that white spaces are supported
        suggestionService.addWord(new Word("success with whitespaces", 1));

        suggestedWordsString = suggestionService.suggest("success", "|", 4);
        suggestedNextLettersString = suggestionService.suggestNextLetter("success", suggestedWordsString, "|");
        Assert.assertEquals("success-very-long|success-very-popular|success-very-boring|success with whitespaces", suggestedWordsString);
        Assert.assertEquals("-| ", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("success", 4);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("success", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-long", "success-very-popular", "success-very-boring", "success with whitespaces"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"-", " "});

        // confirmation that implementation is case insensitive
        suggestionService.addWord(new Word("SucCEss_with-different case", 2));

        suggestedWordsString = suggestionService.suggest("success", "|", 5);
        suggestedNextLettersString = suggestionService.suggestNextLetter("success", suggestedWordsString, "|");
        Assert.assertEquals("success-very-long|success-very-popular|SucCEss_with-different case|success-very-boring|success with whitespaces", suggestedWordsString);
        Assert.assertEquals("-|_| ", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("success", 5);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("success", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success-very-long", "success-very-popular", "SucCEss_with-different case", "success-very-boring", "success with whitespaces"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"-", "_", " "});
    }

    public void testInternationalSupport() {
        SuggestionService suggestionService = SuggestionFactory.getSuggestionService();
        String[] suggestedWordsArray;
        String suggestedWordsString;
        String[] suggestedNextLettersArray;
        String suggestedNextLettersString;

        // spanish
        suggestionService.addWord(new Word("éxito", 10));

        suggestedWordsString = suggestionService.suggest("éxi", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("éxi", suggestedWordsString, "|");
        Assert.assertEquals("éxito", suggestedWordsString);
        Assert.assertEquals("t", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("éxi", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("éxi", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"éxito"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"t"});

        // german
        suggestionService.addWord(new Word("großmutter", 10));

        suggestedWordsString = suggestionService.suggest("gro", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("gro", suggestedWordsString, "|");
        Assert.assertEquals("großmutter", suggestedWordsString);
        Assert.assertEquals("ß", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("gro", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("gro", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"großmutter"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"ß"});

        // swedish
        suggestionService.addWord(new Word("framgång", 10));

        suggestedWordsString = suggestionService.suggest("fra", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("fra", suggestedWordsString, "|");
        Assert.assertEquals("framgång", suggestedWordsString);
        Assert.assertEquals("m", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("fra", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("fra", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"framgång"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"m"});

        // russian (cyrillic)
        suggestionService.addWord(new Word("успех", 10));

        suggestedWordsString = suggestionService.suggest("ус", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("ус", suggestedWordsString, "|");
        Assert.assertEquals("успех", suggestedWordsString);
        Assert.assertEquals("п", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("ус", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("ус", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"успех"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"п"});

        // turkish
        suggestionService.addWord(new Word("başarı", 10));

        suggestedWordsString = suggestionService.suggest("ba", "|", 10);
        suggestedNextLettersString = suggestionService.suggestNextLetter("ba", suggestedWordsString, "|");
        Assert.assertEquals("başarı", suggestedWordsString);
        Assert.assertEquals("ş", suggestedNextLettersString);

        suggestedWordsArray = suggestionService.suggest("ba", 10);
        suggestedNextLettersArray = suggestionService.suggestNextLetter("ba", suggestedWordsArray);
        Assert.assertArrayEquals(suggestedWordsArray, new String[]{"başarı"});
        Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"ş"});

        // experimental support of RTL languages, please contact me if you if you want to improve / extend current implementation
        // RTL languages implementation doesn't provide "next word" functionality, but if you need it - please let me know and I will add it immediately

        // arabic 1
        suggestionService.addWord(new WordRTL("نجاح", 10));

        WordRTL[] suggestedWordsRLTArray = suggestionService.suggest(new WordRTL("اح"), 10);
        suggestedWordsString = suggestionService.suggest(new WordRTL("اح"), "|", 10);
        Assert.assertEquals(1, suggestedWordsRLTArray.length);
        Assert.assertArrayEquals(new String[] {suggestedWordsRLTArray[0].getName()}, new String[]{"نجاح"});
        Assert.assertEquals(suggestedWordsString, "نجاح");

        // arabic 2
        suggestionService.addWord(new WordRTL("نجح", 11));

        suggestedWordsRLTArray = suggestionService.suggest(new WordRTL("ح"), 10);
        suggestedWordsString = suggestionService.suggest(new WordRTL("ح"), "|", 10);
        Assert.assertEquals(2, suggestedWordsRLTArray.length);
        Assert.assertArrayEquals(new String[] {suggestedWordsRLTArray[0].getName(), suggestedWordsRLTArray[1].getName()}, new String[]{"نجح", "نجاح"});
        Assert.assertEquals(suggestedWordsString, "نجح" + "|" + "نجاح");

        // jewish
        suggestionService.addWord(new WordRTL("להצליח", 11));

        suggestedWordsRLTArray = suggestionService.suggest(new WordRTL("ח"), 10);
        suggestedWordsString = suggestionService.suggest(new WordRTL("ח"), "|", 10);
        Assert.assertEquals(1, suggestedWordsRLTArray.length);
        Assert.assertArrayEquals(new String[] {suggestedWordsRLTArray[0].getName()}, new String[]{"להצליח"});
        Assert.assertEquals(suggestedWordsString, "להצליח");
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
        String[] suggestedWordsArray;
        String[] suggestedNextLettersArray;

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

        suggestionService.addWords(words);
        words.clear();

        System.out.println("Added to service [" + amount + "] and took [" + (new Date().getTime() - startTime) + " ms]");

        int i = 0;
        startTime = new Date().getTime();
        for (String selector : selectors) {

            suggestedWordsArray = suggestionService.suggest(selector, 10);

            // uncomment below to visually confirm matches
            // if (i % 1000 == 0) System.out.println("Matched [" + selector + "] to [" + result.get("suggestions") + "]");

            Assert.assertTrue(suggestedWordsArray.length >= 1);
            i++;
        }

        System.out.println("Matched: [" + selectors.size() + "] and took [" + (new Date().getTime() - startTime) + " ms] [" + new BigDecimal(((double) new Date().getTime() - startTime) / selectors.size()).setScale(5, BigDecimal.ROUND_HALF_UP).toString() + " ms] per match and [" + Math.round(1000 / (((double) new Date().getTime() - startTime) / selectors.size())) + " words] per second");
    }

    private int randomInt(int start, int end) {
        return random.nextInt(end - start) + start;
    }
}
