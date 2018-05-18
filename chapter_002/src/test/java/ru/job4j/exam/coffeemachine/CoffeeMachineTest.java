package ru.job4j.exam.coffeemachine;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * CoffeeMachineTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CoffeeMachineTest {

    /**
     * Test. When need more money to buy coffee.
     */
    @Test
    public void whenValueLessThanPriceThenGetErrMessage() {

        CoffeeMachine coffeeMachine = new CoffeeMachine();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream stdOut = System.out;

        String msg;

        System.setOut(new PrintStream(out));

        coffeeMachine.changes(10, 15);
        msg = out.toString();
        assertThat(msg.contains("Not enough money."), is(true));

        System.setOut(stdOut);
    }

    /**
     * Test. Spend'em all.
     */
    @Test
    public void whenValueEqualsPriceThenGetZeroChange() {

        CoffeeMachine coffeeMachine = new CoffeeMachine();

        int[] result = coffeeMachine.changes(100, 100);
        int[] expected = {};

        assertThat(result, is(expected));
    }

    /**
     * Test. Cash back.
     */
    @Test
    public void whenValueGreaterThanPriceThenGetChangeCoins() {

        CoffeeMachine coffeeMachine = new CoffeeMachine();

        int[] result = coffeeMachine.changes(50, 35);
        int[] expected = {5, 10};

        assertThat(result, is(expected));
    }

}
