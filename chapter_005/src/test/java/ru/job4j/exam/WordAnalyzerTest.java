package ru.job4j.exam;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * WordAnalyzerTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class WordAnalyzerTest {

    private WordAnalyzer wordAnalyzer;
    private final String abcdef = "abcdef";
    private final String fgh = "fgh";
    private final String zxcvbn = "zxcvbn";
    private final String nbvcxz = "nbvcxz";
    private final String aaabbbccc = "aaabbbccc";
    private final String bbccaa = "bbccaa";

    @Before
    public void init() {
        wordAnalyzer = new WordAnalyzer();
    }

    // hasSameSymbols() tests
    @Test
    public void whenTwoWordsHasSameSymbolsShouldReturnTrue() {
        assertThat(wordAnalyzer.hasSameSymbols(zxcvbn, nbvcxz), is(true));
        assertThat(wordAnalyzer.hasSameSymbols(bbccaa, aaabbbccc), is(true));
        assertThat(wordAnalyzer.hasSameSymbols(abcdef, " -F+!="), is(true));
    }

    @Test
    public void whenWordsHasntSameSymbolsShouldReturnFalse() {
        assertThat(wordAnalyzer.hasSameSymbols(fgh, nbvcxz), is(false));
        assertThat(wordAnalyzer.hasSameSymbols(bbccaa, fgh), is(false));
    }

    @Test
    public void whenAnyWordIsEmptyShouldReturnFalse() {
        assertThat(wordAnalyzer.hasSameSymbols("", fgh), is(false));
        assertThat(wordAnalyzer.hasSameSymbols(fgh, String.valueOf("")), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenFirstWordIsNullShouldThrowIAException() {
        wordAnalyzer.hasSameSymbols(abcdef, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenSecondWordIsNullShouldThrowIAException() {
        wordAnalyzer.hasSameSymbols(null, abcdef);
    }

    // isAnagram() tests
    @Test
    public void whenWordsIsAnagramShouldReturnTrue() {
        assertThat(wordAnalyzer.isAnagram(zxcvbn, nbvcxz), is(true));
        assertThat(wordAnalyzer.isAnagram(fgh, "HFG"), is(true));
    }

    @Test
    public void whenWordsIsNotAnagramShouldReturnFalse() {
        assertThat(wordAnalyzer.isAnagram(fgh, nbvcxz), is(false));
        assertThat(wordAnalyzer.isAnagram(bbccaa, fgh), is(false));
    }

    @Test
    public void whenWordIsEmptyShouldReturnFalse() {
        assertThat(wordAnalyzer.isAnagram("", zxcvbn), is(false));
        assertThat(wordAnalyzer.isAnagram(nbvcxz, String.valueOf("")), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAnyWordIsNullShouldThrowIAException() {
        wordAnalyzer.isAnagram(null, null);
    }

    // countCharMatches() tests
    @Test
    public void whenWordsHasCharMatchesShouldReturnCountOfMatched() {
        assertThat(wordAnalyzer.countCharMatches(zxcvbn, nbvcxz), is(6));
        assertThat(wordAnalyzer.countCharMatches(fgh, "HFG"), is(3));
        assertThat(wordAnalyzer.countCharMatches((fgh), (aaabbbccc + "H")), is(1));
        assertThat(wordAnalyzer.countCharMatches("BCvd", "bcxd"), is(3));
        assertThat(wordAnalyzer.countCharMatches(aaabbbccc, abcdef), is(3));
    }

    @Test
    public void whenWordsHasNotCharMatchesShouldReturnZero() {
        assertThat(wordAnalyzer.countCharMatches(fgh, nbvcxz), is(0));
        assertThat(wordAnalyzer.countCharMatches(bbccaa, fgh), is(0));
        assertThat(wordAnalyzer.countCharMatches(aaabbbccc, "!"), is(0));
    }

    @Test
    public void whenWordsIsEmptyShouldReturnZero() {
        assertThat(wordAnalyzer.countCharMatches("", zxcvbn), is(0));
        assertThat(wordAnalyzer.countCharMatches(nbvcxz, String.valueOf("")), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenWordsIsNullShouldThrowIAException() {
        wordAnalyzer.countCharMatches(null, null);
    }

    // findDuplicateChars() tests
    @Test
    public void whenWordHasCharDuplicatesShouldReturnDuplicates() {
        assertThat(wordAnalyzer.findDuplicateChars(zxcvbn + nbvcxz).size(), is(6));
        assertThat(wordAnalyzer.findDuplicateChars(fgh + "HFG").size(), is(3));
        assertThat(wordAnalyzer.findDuplicateChars(aaabbbccc).size(), is(3));
        assertThat(wordAnalyzer.findDuplicateChars("BCvdb").size(), is(1));
        assertThat(wordAnalyzer.findDuplicateChars("A +" + abcdef).size(), is(1));
    }

    @Test
    public void whenWordHasNotCharDuplicatesShouldReturnEmptySet() {
        assertThat(wordAnalyzer.findDuplicateChars(nbvcxz), is(Collections.EMPTY_SET));
        assertThat(wordAnalyzer.findDuplicateChars(fgh), is(Collections.EMPTY_SET));
        assertThat(wordAnalyzer.findDuplicateChars("!-=:fgr/"), is(Collections.EMPTY_SET));
    }

    @Test
    public void whenWordIsEmptyShouldReturnEmptySet() {
        assertThat(wordAnalyzer.findDuplicateChars(""), is(Collections.EMPTY_SET));
        assertThat(wordAnalyzer.findDuplicateChars(String.valueOf("")), is(Collections.EMPTY_SET));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenWordIsNullShouldThrowIAException() {
        wordAnalyzer.findDuplicateChars(null);
    }

    // countCharDuplicates() tests
    @Test
    public void whenWordHasCharDuplicatesShouldReturnNumberOfEach() {
        Map<Character, Integer> znMap = fillMap(zxcvbn + nbvcxz);
        assertThat(wordAnalyzer.countCharDuplicates(zxcvbn + nbvcxz), is(znMap));
        Map<Character, Integer> fghMap = fillMap(fgh + "HFG");
        assertThat(wordAnalyzer.countCharDuplicates(fgh + "HFG"), is(fghMap));
        assertThat(wordAnalyzer.countCharDuplicates(aaabbbccc).size(), is(3));
        Map<Character, Integer> aaaMap = new HashMap<>();
        aaaMap.put('a', 16);
        assertThat(wordAnalyzer.countCharDuplicates(" AAAAAAaaaaAAAAAA "), is(aaaMap));
    }

    @Test
    public void whenWordHasNotCharDuplicatesShouldReturnEmptyMap() {
        assertThat(wordAnalyzer.countCharDuplicates(nbvcxz), is(Collections.EMPTY_MAP));
        assertThat(wordAnalyzer.countCharDuplicates(fgh), is(Collections.EMPTY_MAP));
        assertThat(wordAnalyzer.countCharDuplicates("!-=:fgr/"), is(Collections.EMPTY_MAP));
    }

    @Test
    public void whenWordIsEmptyShouldReturnEmptyMap() {
        assertThat(wordAnalyzer.countCharDuplicates(""), is(Collections.EMPTY_MAP));
        assertThat(wordAnalyzer.countCharDuplicates(String.valueOf("")), is(Collections.EMPTY_MAP));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenWordIsNullThenThrowIAException() {
        wordAnalyzer.countCharDuplicates(null);
    }

    private Map<Character, Integer> fillMap(String word) {
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = word.toLowerCase().trim().toCharArray();
        for (char ch : chars) {
            map.put(ch, 2);
        }
        return map;
    }
}
