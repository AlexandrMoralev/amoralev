package ru.job4j.exam;

/**
 * StringChecking
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StringChecking {

    /**
     * Method contains - check the sequence of characters of the substring in the origin string
     * @param origin - original string
     * @param sub - substring
     * @return true, if string contains substring, false if not
     */
    boolean contains(String origin, String sub) {

        boolean isContains = false;
        char[] originCharArray = origin.toLowerCase().toCharArray();
        char[] subCharArray = sub.toLowerCase().toCharArray();

        // iterate through the originCharArray elements
        for (int indexOrigin = 0; indexOrigin <= originCharArray.length - subCharArray.length; indexOrigin++) {

            // when first letter of substring is matching with some letter of the origin string,
            // then check the remaining letters of the substring
            if (subCharArray[0] == originCharArray[indexOrigin]) {
                int countMatches = 1;
                for (int indexSub = 1; indexSub < subCharArray.length; indexSub++) {
                    if (originCharArray[indexOrigin + indexSub] == subCharArray[indexSub]) {
                        countMatches++;
                    }
                }
                // if sequence of all letters of the substring is matching to sequence of letters in origin string, isContains = true
                isContains = (countMatches == subCharArray.length);
            }
        }

        return isContains;
    }
}
