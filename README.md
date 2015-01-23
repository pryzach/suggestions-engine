# [Suggestions Engine]

## About

Very simplistic proof of concept implementation of highly performant suggestions engine

## Performance

AMD mobile CPU core (2.45Ghz).

*Tests were executed in IDEA. Since, for intergation tests, words are randomly generated - performance numbers have tendency to vary significantly (up to 300k-400k words matched per second).*

```
Started adding 10k words, please wait...
Added to service [10000] and took [330 ms]
Matched: [1728] and took [18 ms] [0.01273 ms] per match and [69120 words] per second
```

```
The Second Edition of the 20-volume  Oxford English Dictionary contains full entries for 171,476 words in current use Started adding 171k words, please wait...
Added to service [171476] and took [3379 ms]
Matched: [29239] and took [220 ms] [0.00752 ms] per match and [132905 words] per second
```

## Start using

Download **suggestions-proof-of-concept-0.1.0*.jar**s from GitHub Releases tab or clone git repository and invoke "mvn package" on master

```Java
SuggestionService suggestionService = SuggestionFactory.getSuggestionService();

suggestionService.addWord(new Word("success", 10));

String[] suggestedWordsArray = suggestionService.suggest("succes", 10);
String[] suggestedNextLettersArray = suggestionService.suggestNextLetter("succes", suggestedWordsArray);

Assert.assertArrayEquals(suggestedWordsArray, new String[]{"success"});
Assert.assertArrayEquals(suggestedNextLettersArray, new String[]{"s"});
```
	
```Java
SuggestionService suggestionService = SuggestionFactory.getSuggestionService();

suggestionService.addWord(new Word("success", 10));

String suggestedWordsString = suggestionService.suggest("succes", "|", 10);
String suggestedNextLettersString = suggestionService.suggestNextLetter("succes", suggestedWordsString, "|");

Assert.assertEquals("success", suggestedWordsString);
Assert.assertEquals("s", suggestedNextLettersString);
```

## Contribute

If you would like to help with development - fork, contact me via [pryzach@gmail.com] (mailto:pryzach@gmail.com) or post a question using [GitHub Issue Tracker] (https://github.com/pryzach/midao/issues).

## License

Licensed under Apache License 2.0.
