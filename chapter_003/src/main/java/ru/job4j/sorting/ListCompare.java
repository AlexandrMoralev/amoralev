package ru.job4j.sorting;

import java.util.Comparator;

/**
 * ListCompare
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ListCompare implements Comparator<String> {

    /**
     * Method compare - compares two Strings lexicographically.
     * @param o1 first String to compare
     * @param o2 second String to be compared
     * @return the value = 0, when the argument string is equal to
     *          this string
     *          a value less than 0, when this string
     *          is lexicographically less than the string argument
     *          a value greater than 0, when this string is
     *          lexicographically greater than the string argument.
     */
    @Override
    public int compare(String o1, String o2) {

        int result = 0;
        int firstLength = o1.length();
        int secondLength = o2.length();

        if (firstLength == secondLength && o1.contains(o2)) {
            result = 0;
        } else {
            int limiter = firstLength > secondLength ? secondLength : firstLength;
            int counter = 0;
            while (counter != limiter) {
                result = Character.compare(o1.charAt(counter), o2.charAt(counter++));
                if (result != 0) {
                    break;
                }
            }
            if (result == 0) {
                result = o1.length() - o2.length();
            }
        }
        return result;
    }
}
