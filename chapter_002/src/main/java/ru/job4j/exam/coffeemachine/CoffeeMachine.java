package ru.job4j.exam.coffeemachine;

/**
 * CoffeeMachine
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CoffeeMachine {

    private final int[] coins = {10, 5, 2, 1};

    /**
     * Method changes
     * @param value @NonNull int value of banknotes
     * @param price @NonNull int price of the coffee
     * @return int[] of change coins, empty int[] if nothing to change
     */
    int[] changes(int value, int price) {
        int[] result = {};
        int[] coinCounterMask = {0, 0, 0, 0};
        int change = value - price;

        if (change >= 0) {
            int coinNumber = 0;

            for (int i = 0; i < coins.length;) {
                if (coins[i] <= change) {
                    coinCounterMask[i]++;
                    coinNumber++;
                    change -= coins[i];
                } else {
                    i++;
                }
            }
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
