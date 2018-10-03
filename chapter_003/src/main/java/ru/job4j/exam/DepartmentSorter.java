package ru.job4j.exam;

import java.util.*;

/**
 * DepartmentSorter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DepartmentSorter {

    /**
     * Method ascendingSort -
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] ascendingSort(String[] departments) {
        Arrays.sort(departments);
        return departments;
    }

    /**
     * Method descendingSort - sorting an array in hierarchical descending order
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] descendingSort(String[] departments) {
        String[] result;
        String[] indexes = getDeptIndexes(departments);

        TreeMap<String, String> tm = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int compareResult = 0;
                int firstLength = o1.length();
                int secondLength = o2.length();

                if (firstLength == secondLength) {
                    compareResult = compareDescendingIndex(o1, o2);
                }
                if (firstLength > secondLength) {
                    if (o1.startsWith(o2)) {
                        compareResult = 1;
                    } else {
                        compareResult = compareDescendingIndex(o1, o2);
                    }
                }
                if (firstLength < secondLength) {
                    if (o2.startsWith(o1)) {
                        compareResult = -1;
                    } else {
                        compareResult =  compareDescendingIndex(o1, o2);
                    }
                }
                return compareResult;
            }
        });
        for (int i = 0; i < departments.length; i++) {
            tm.put(indexes[i], departments[i]);
        }
        result = tm.values().toArray(new String[tm.values().size()]);
        return result;
    }

    /**
     * compareDescendingIndex - character-by-character string comparison
     * @param o1 non-null String to compare
     * @param o2 non-null String to compare
     * @return int the value = 0, when both string arguments are equals
     *          int value -1, when the first string
     *          is less than the second string argument
     *          int value 1, when the first string is
     *          greater than the second  string argument.
     */
    private int compareDescendingIndex(String o1, String o2) {
        int result = 0;
        int firstLength = o1.length();
        int secondLength = o2.length();
        int limiter = firstLength > secondLength ? secondLength : firstLength;

        for (int i = 0; i < limiter; i++) {
            if (o1.charAt(i) > o2.charAt(i)) {
                result = -1;
            }
            if (o1.charAt(i) < o2.charAt(i)) {
                result = 1;
            }
            if (o1.charAt(i) == o2.charAt(i)) {
                if (firstLength == limiter - 1) {
                    result = -1;
                }
                if (secondLength == limiter - 1) {
                    result = 1;
                }
            }
        }
        return result;
    }

    /**
     * getDeptIndexes
     * @param departments String[] to extract indexes array
     * @return String[] filled with department indexes only
     */
    private String[] getDeptIndexes(String[] departments) {
        String[] result = new String[departments.length];
        int counter = 0;
        for (String dept : departments) {
            result[counter++] = dept.replaceAll("[^0-9]", "");
        }
        return result;
    }
}
