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

    /**
     * Simplified constructor which can be used as selector word
     *
     * @param name RTL selector word
     */
    public WordRTL(String name) {
        super(new StringBuilder(name).reverse().toString(), 0);
    }
}
