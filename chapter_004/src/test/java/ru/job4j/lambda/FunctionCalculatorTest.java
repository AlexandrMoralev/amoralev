package ru.job4j.lambda;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * FunctionCalculatorTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class FunctionCalculatorTest {

    private FunctionCalculator calc;
    private List<Double> expected;
    private int start = 0;
    private int end = 10;

    /**
     * Init. New instances construction.
     */
    @Before
    public void init() {
        this.calc = new FunctionCalculator();
        this.expected = new ArrayList<>();

    }

    /**
     * Test. linearFunctionTest.
     */
    @Test
    public void linearFunctionTest() {
        for (int i = start; i < end; i++) {
            expected.add((double) i);
        }
        List<Double> result = calc.diapason(start, end, calc.linearFunction);
        assertThat(result, is(expected));
    }

    /**
     * Test. quadraticFunctionTest.
     */
    @Test
    public void quadraticFunctionTest() {
        for (int i = start; i < end; i++) {
            expected.add((double) i * i);
        }
        List<Double> result = calc.diapason(start, end, calc.quadraticFunction);
        assertThat(result, is(expected));
    }

    /**
     * Test. logarithmicFunctionTest.
     */
    @Test
    public void logarithmicFunctionTest() {
        for (int i = start; i < end; i++) {
            expected.add(Math.log(i));
        }
        List<Double> result = calc.diapason(start, end, calc.logarithmicFunction);
        assertThat(result, is(expected));
    }
}
