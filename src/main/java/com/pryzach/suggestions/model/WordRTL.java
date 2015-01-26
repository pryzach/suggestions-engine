package com.pryzach.suggestions.model;

/**
 * {@link com.pryzach.suggestions.model.Word} override which supports RTL languages
 */
public class WordRTL extends Word {
    /**
     * {@inheritDoc}
     */
    public WordRTL(String name, int popularityIndex) {
        super(new StringBuilder(name).reverse().toString(), popularityIndex);
    }
}
