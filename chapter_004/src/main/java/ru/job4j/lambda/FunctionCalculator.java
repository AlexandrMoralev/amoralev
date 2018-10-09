package ru.job4j.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * FunctionCalculator
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class FunctionCalculator {

    public final Function<Double, Double> linearFunction = (Double value) -> value;

    public final Function<Double, Double> quadraticFunction = (Double value) -> value*value;

    public final Function<Double, Double> logarithmicFunction = (Double value) -> Math.log(value);

    /**
     * Method diapason - calculates function with initial parameters
     * @param start int start of calculation range
     * @param end int end of calculation range
     * @param func Function to be calculated
     * @return List of Double values as result
     */
    public List<Double> diapason(int start, int end, Function<Double, Double> func) {
        List<Double> result = new ArrayList<>();

        for (int i = start; i < end; i++) {
            result.add(func.apply((double) i));
        }
        return result;
    }
}
