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

import junit.framework.TestCase;
import org.junit.Assert;

public class WordTest extends TestCase {

    public void testGetName() throws Exception {
        Word word = new Word("success", 0);

        Assert.assertEquals("success", word.getName());
    }

    public void testGetPopularityIndex() throws Exception {
        Word word = new Word("success", 10);

        Assert.assertEquals(10, word.getPopularityIndex());
    }

    public void testEquals() throws Exception {
        Word word1 = new Word("success", 10);
        Word word2 = new Word("success", 12);
        Word word3 = new Word("success", 10);
        Word word4 = new Word("Success", 10);

        Assert.assertTrue(word1.equals(word3));
        Assert.assertFalse(word1.equals(word2));
        Assert.assertTrue(word1.equals(word4));
    }

    public void testHashCode() throws Exception {
        Word word1 = new Word("success", 10);

        Assert.assertEquals(-2047688601, word1.hashCode());
    }

    public void testCompareTo() throws Exception {
        Word word1 = new Word("success", 10);
        Word word2 = new Word("success", 12);
        Word word3 = new Word("success", 10);
        Word word4 = new Word("success", 1);
        Word word5 = new Word("higher", 120);

        Assert.assertTrue(word1.compareTo(word2) < 0);
        Assert.assertTrue(word1.compareTo(word3) == 0);
        Assert.assertTrue(word1.compareTo(word4) > 0);
        Assert.assertTrue(word1.compareTo(word5) < 0);
    }
}