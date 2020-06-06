package ru.job4j.array;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * TurnTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TurnTest {

    /**
     * Test.
     */
    @Test
    public void whenTurnArrayWithEvenAmountOfElementsThenTurnedArray() {
        Turn turn = new Turn();
        int[] result = turn.back(new int[] {4, 1, 6, 2});
        assertThat(result, is(new int[] {2, 6, 1, 4}));
    }

    /**
     * Test.
     */
    @Test
    public void whenTurnArrayWithOddAmountOfElementsThenTurnedArray() {
        Turn turn = new Turn();
        int[] result = turn.back(new int[] {1, 2, 3, 4, 5});
        assertThat(result, is(new int[] {5, 4, 3, 2, 1}));
    }
}
