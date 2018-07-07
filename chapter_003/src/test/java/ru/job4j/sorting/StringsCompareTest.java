package ru.job4j.sorting;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * StringsCompareTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StringsCompareTest {

    /**
     * Test. Equal Strings.
     */
    @Test
    public void whenStringAreEqualThenZero() {
        ListCompare compare = new ListCompare();
        int result = compare.compare(
                "Ivanov",
                "Ivanov"
        );
        assertThat(result, is(0));
    }

    /**
     * Test. Left less than Right.
     */
    @Test
    public void whenLeftLessThanRightResultShouldBeNegative() {
        ListCompare compare = new ListCompare();
        int result = compare.compare(
                "Ivanov",
                "Ivanova"
        );
        assertThat(result, lessThan(0));
    }

    /**
     * Test. Left greater than Right.
     */
    @Test
    public void whenLeftGreaterThanRightResultShouldBePositive() {
        ListCompare compare = new ListCompare();
        int result = compare.compare(
                "Petrov",
                "Ivanova"
        );
        assertThat(result, greaterThan(0));
    }

    /**
     * Test. Strings differs by the second char, left is greater.
     */
    @Test
    public void secondCharOfLeftGreaterThanRightResultShouldBePositive() {
        ListCompare compare = new ListCompare();
        int result = compare.compare(
                "Petrov",
                "Patrov"
        );
        assertThat(result, greaterThan(0));
    }

    /**
     * Test. Strings differs by the second char, left is less.
     */
    @Test
    public void secondCharOfLeftLessThanRightResultShouldBeNegative() {
        ListCompare compare = new ListCompare();
        int result = compare.compare(
                "Patrova",
                "Petrov"
        );
        assertThat(result, lessThan(0));
    }

}
