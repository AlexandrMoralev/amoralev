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

    private Comparator<String> ascendingDeptComparator = new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    };

    private Comparator<String> descendingDeptComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int compared = o1.compareTo(o2);
            if (compared != 0) {
                compared = -compared;
            }
            if (o1.indexOf(o2) == 0) {
                compared = 1;
            }
            if (o2.indexOf(o1) == 0) {
                compared = -1;
            }
            return compared;
        }
    };

    private String[] parsedDepartments;

    /**
     * Method ascendingSort - sorting an String array in hierarchical ascending order.
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] ascendingSort(String[] departments) throws IllegalArgumentException {
        this.validate(departments);
        Arrays.sort(this.parsedDepartments, this.ascendingDeptComparator);
        return this.parsedDepartments;
    }

    /**
     * Method descendingSort - sorting an array in hierarchical descending order
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] descendingSort(String[] departments) {
        this.validate(departments);
        Arrays.sort(this.parsedDepartments, this.descendingDeptComparator);
        return this.parsedDepartments;
    }

    /**
     * Method parse - String[] departments parsing.
     * Constructs a new String array with hierarchy recovering.
     * @param departments String[] departments to be parsed
     */
    private void parse(String[] departments) {
        Set<String> deptSet = new TreeSet<>();
        char delimiter = '\\';

        for (String dept : departments) {
            deptSet.add(dept);
            for (int i = 0; i < dept.length(); i++) {
                if (dept.charAt(i) == delimiter) {
                    deptSet.add(dept.substring(0, i));
                }
            }
        }
        this.parsedDepartments = deptSet.toArray(new String[0]);
    }

    /**
     * Method validate - tests input String array for null-reference and doubling
     * @param arr String[] array to be sorted
     * @throws IllegalArgumentException if String[] reference is null
     */
    private void validate(String[] arr) throws IllegalArgumentException {
        if (arr == null) {
            throw new IllegalArgumentException("Null-reference of the String[]");
        }
        if (this.parsedDepartments == null | this.parsedDepartments != arr) {
            this.parse(arr);
        }
    }
}
