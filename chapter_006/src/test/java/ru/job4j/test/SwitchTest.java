package ru.job4j.test;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SwitchTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SwitchTest {
    private Switch aSwitch;
    private static final int REPETITIONS = 7;

    @Before
    public void init() {
        aSwitch = new Switch(REPETITIONS);
    }

    @Test
    public void whenAddDigitsThenHasDigitsInSameOrder() {
        String expected = "0987654321";
        aSwitch.addDigit(0);
        for (int i = 9; i > 0; i--) {
            aSwitch.addDigit(i);
        }
        assertThat(aSwitch.getValue(), is(expected));
    }

    @Test
    public void whenAddNumberThenHasNumber() {
        String expected = "123456789";
        aSwitch.addDigit(12345);
        aSwitch.addDigit(6789);
        assertThat(aSwitch.getValue(), is(expected));
    }

    @Test
    public void whenThen() throws InterruptedException {
        Worker worker1 = new Worker(1, aSwitch);
        CoWorker worker2 = new CoWorker(2, aSwitch);
        Thread first = new Thread(worker1);
        Thread second = new Thread(worker2);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(aSwitch.getValue(), is("1111111111222222222211111111112222222222111111111122222222221111111111"));
    }
}
