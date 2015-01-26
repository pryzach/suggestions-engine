package com.pryzach.suggestions.model;

import junit.framework.TestCase;
import org.junit.Assert;

public class WordRTLTest extends TestCase {
    public void testConstructor() {
        // what this class does - is essentially reversing RTL word into LTR word for internal processing
        Assert.assertEquals("cba", new WordRTL("abc", 0).getName());
    }
}