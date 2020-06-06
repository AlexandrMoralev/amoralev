package ru.job4j.array;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;

/**
 * ArrayDuplicatesTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ArrayDuplicateTest {

    /**
     * Test. Input String[5] with 2 duplicates, output String[3] without duplicates
     */
    @Test
    public void whenArrayOfFiveHasTwoDuplicatesThenArrayOfThreeElements() {
        ArrayDuplicate arrayDuplicate = new ArrayDuplicate();
        String[] result = arrayDuplicate.remove(new String[] {"Привет", "Мир", "Привет", "Супер", "Мир"});
        assertThat(result, arrayContainingInAnyOrder(new String[] {"Привет", "Мир", "Супер"}));
    }
}
