package ru.job4j.exam.braces;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * BracesParserTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BracesParserTest {

    private BracesParser bracesParser;

    private static final String FIRST = "{{}}[]";
    private static final String SECOND = "[{}{}]";

    private static final String TEST_STR = "a[b{c}d)e]";
    private static final String THIRD = "}{}]";
    private static final String FOURTH = "12:b;5(";
    private static final String FIFTH = "{[}]";

    @Before
    public void init() {
        bracesParser = new BracesParser();
    }

    @Test
    public void whenStringIsValidShouldReturnListOfBracketsPairs() {
        ArrayList<BracketsPair> first = new ArrayList<>();
        first.add(new BracketsPair(1, 2));
        first.add(new BracketsPair(0, 3));
        first.add(new BracketsPair(4, 5));

        ArrayList<BracketsPair> second = new ArrayList<>();
        second.add(new BracketsPair(1, 2));
        second.add(new BracketsPair(3, 4));
        second.add(new BracketsPair(0, 5));

        assertThat(bracesParser.parse(FIRST).containsAll(first), is(true));
        assertThat(bracesParser.parse(SECOND).containsAll(second), is(true));
    }

    @Test
    public void whenStringIsInvalidShouldReturnEmptyList() {
        assertThat(bracesParser.parse(TEST_STR).isEmpty(), is(true));
        assertThat(bracesParser.parse(THIRD).isEmpty(), is(true));
        assertThat(bracesParser.parse(FOURTH).isEmpty(), is(true));
        assertThat(bracesParser.parse(FIFTH).isEmpty(), is(true));
    }

    @Test
    public void whenStringIsEmptyShouldReturnReturnEmptyList() {
        assertThat(bracesParser.parse("").isEmpty(), is(true));

    }

    @Test(expected = IllegalArgumentException.class)
    public void whenStringIsNullShouldThrowIAException() {
        bracesParser.parse(null);
    }
}
