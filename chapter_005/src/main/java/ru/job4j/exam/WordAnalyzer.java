package ru.job4j.exam;

import java.util.*;

/**
 * WordAnalyzer
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class WordAnalyzer {

    private Set<Character> firstSet;
    private Set<Character> secondSet;

// Задание 1: Даны 2 слова, проверить, что они состоят из одинаковых символов, и найти кол-во дубликатов

    /**
     * Method hasSameSymbols - checks for common characters in two words
     *
     * @param first  notnull String first word
     * @param second notnull String second word
     * @return true, if both words has at least one common character
     * false, if there are no common characters in the words
     */
    public boolean hasSameSymbols(final String first, final String second) {
        boolean result = false;
        if (isArgValid(first) && isArgValid(second)) {
            this.initCharSets(first, second);
            result = !Collections.disjoint(firstSet, secondSet);
        }
        return result;
    }

    /**
     * Method isAnagram - checks, if two words are an anagram (consists of the same characters)
     *
     * @param first  notnull String first word
     * @param second notnull String second word
     * @return true, if words are an anagram
     * false, if there are no common characters in the words
     */
    public boolean isAnagram(final String first, final String second) {
        boolean result = false;
        if (isArgValid(first) && isArgValid(second)
                && first.length() == second.length()) {
            this.initCharSets(first, second);
            firstSet.removeAll(secondSet);
            result = firstSet.size() == 0;
        }
        return result;
    }

    /**
     * Method countCharMatches - counting common characters in two words
     *
     * @param first  notnull String first word
     * @param second notnull String second word
     * @return int number of common characters
     */
    public int countCharMatches(final String first, final String second) {
        int result = 0;
        if (isArgValid(first) && isArgValid(second)) {
            this.initCharSets(first, second);
            result = firstSet.size() >= secondSet.size()
                    ? countCharMatches(firstSet, secondSet)
                    : countCharMatches(secondSet, firstSet);
        }
        return result;
    }

    private boolean isArgValid(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Want some NPE?");
        }
        return !word.isEmpty();
    }

    private int countCharMatches(Set<Character> firstSet, Set<Character> secondSet) {
        int setSizeBefore = firstSet.size();
        firstSet.removeAll(secondSet);
        return setSizeBefore - firstSet.size();
    }

    private Collection<Character> convert(String word, Collection<Character> chars) {
        char[] charArray = word.toLowerCase().trim().toCharArray();
        for (Character ch : charArray) {
            chars.add(ch);
        }
        return chars;
    }

    private void initCharSets(String first, String second) {
        firstSet = (Set<Character>) this.convert(first, new HashSet<>());
        secondSet = (Set<Character>) this.convert(second, new HashSet<>());
    }

//  Задание 2: Дано слово, найти все повторяющиеся символы и кол-во вхождений дубликатов

    /**
     * Method findDuplicateChars - find all duplicates of characters in the word
     *
     * @param word notnull String word to be checked
     * @return Set of duplicated characters, or EmptySet, if there are no duplicates in the word
     */
    public Set<Character> findDuplicateChars(final String word) {
        Set<Character> result = new HashSet<>();
        if (this.isArgValid(word)) {
            char[] arr = word.toLowerCase().trim().toCharArray();
            Arrays.parallelSort(arr);
            for (int i = arr.length - 1; i > 0; i--) {
                if (arr[i] == arr[i - 1]) {
                    result.add(arr[i]);
                }
            }
        }
        return result.isEmpty() ? Collections.EMPTY_SET : result;
    }

    /**
     * Method countCharDuplicates - counts
     *
     * @param word notnull String word to be checked
     * @return Map with Key-Value pairs, where Key is a repeated Character, and Value is the number of repeats
     * if there are no repeated characters, returns an EmptyMap
     */
    public Map<Character, Integer> countCharDuplicates(final String word) {
        Map<Character, Integer> result = new HashMap<>();
        Collection<Character> chars;
        if (this.isArgValid(word)) {
            chars = this.convert(word, new ArrayList<>());
            for (Character ch : chars) {
                int freq = Collections.frequency(chars, ch);
                if (freq > 1) {
                    result.put(ch, freq);
                }
            }
        }
        return result.isEmpty() ? Collections.EMPTY_MAP : result;
    }
}
