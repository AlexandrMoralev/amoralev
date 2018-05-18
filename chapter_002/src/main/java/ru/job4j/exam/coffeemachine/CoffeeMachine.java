package ru.job4j.exam.coffeemachine;

import java.util.Arrays;

/**
 * CoffeeMachine
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CoffeeMachine {

    // available coins
    private final int[] coins = {10, 5, 2, 1};

    /**
     * Method changes
     * @param value @NonNull int value of banknotes
     * @param price @NonNull int price of the coffee
     * @return int[] of change coins, empty int[] if nothing to change
     */
    int[] changes(int value, int price) {

        // default initialisation
        int[] result = {};
        // contains number of every change coin
        int[] coinCounterMask = {0, 0, 0, 0};
        // value to change
        int change = value - price;

        // if need to change
        if (change >= 0) {

            // number of coins to changing
            int coinNumber = 0;

            // calculating count of every change coin, using greedy algorithm
            for (int i = 0; i < coins.length;) {

                if (coins[i] <= change) {
                    coinCounterMask[i]++;
                    coinNumber++;          //  counting all change coins - sum of coinCounterMask[]
                    change -= coins[i];
                } else {
                    i++;
                }
            }

            // filling int[] changes, in accordance to coinCounterMask
            result = new int[coinNumber];

            for (int i = 0; i < coinCounterMask.length; i++) {
                for (int j = 0; j < coinCounterMask[i]; j++) {
                    result[--coinNumber] = coins[i];
                }
            }

        } else {
            System.out.println("Not enough money. Insert banknote.");
        }

        return result;
    }
}
