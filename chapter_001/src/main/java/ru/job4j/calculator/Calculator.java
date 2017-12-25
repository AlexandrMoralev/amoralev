package ru.job4j.calculator;


/**
 * Calculator
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */

public class Calculator {

    private double result;

    /**
     * Method add.
     * @param first "double" number
     * @param second "double" number
     */
    public void add (double first, double second) {
        this.result = first + second;
    }
    /**
     * Method substract.
     * @param first "double" number
     * @param second "double" number
     */
    public void substract (double first, double second) {
        this.result = first - second;
    }
    /**
     * Method div.
     * @param first "double" number
     * @param second "double" number
     */
    public void div (double first, double second) {
        this.result = first / second;
    }
    /**
     * Method multiple.
     * @param first "double" number
     * @param second "double" number
     */
    public void multiple (double first, double second) {
        this.result = first * second;
    }
    /**
     * Method getResult.
     * @return value of result field
     */
    public double getResult() {
        return this.result;
    }
}
