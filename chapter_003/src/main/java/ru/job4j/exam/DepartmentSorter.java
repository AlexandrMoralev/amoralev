package ru.job4j.exam;


import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * DepartmentSorter
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DepartmentSorter {

    private Comparator<String> descendingDeptComparator = (o1, o2) -> {
        int compared = o2.compareTo(o1);
        if (o1.indexOf(o2) == 0) {
            compared = 1;
        }
        if (o2.indexOf(o1) == 0) {
            compared = -1;
        }
        return compared;
    };

    private String[] parsedDepartments;

    /**
     * Method ascendingSort - sorting an String array in hierarchical ascending order.
     *
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] ascendingSort(String[] departments) throws IllegalArgumentException {
        this.validate(departments);
        return Stream.of(parsedDepartments).sorted().toArray(String[]::new);
    }

    /**
     * Method descendingSort - sorting an array in hierarchical descending order
     *
     * @param departments String[] without null elements
     * @return sorted String[]
     */
    public String[] descendingSort(String[] departments) {
        this.validate(departments);
        return Stream.of(parsedDepartments).sorted(this.descendingDeptComparator).toArray(size -> new String[size]);
    }

    /**
     * Method parse - String[] departments parsing.
     * Constructs a new String array with hierarchy recovering.
     *
     * @param departments String[] departments to be parsed
     */
    private void parse(String[] departments) {
        Set<String> deptSet = new TreeSet<>();
        char delimiter = '\\';

        Stream.of(departments).filter(Objects::nonNull).forEach(dept -> {
            deptSet.add(dept);
            for (int i = 0; i < dept.length(); i++) {
                if (dept.charAt(i) == delimiter) {
                    deptSet.add(dept.substring(0, i));
                }
            }
        });
        this.parsedDepartments = deptSet.toArray(new String[0]);
    }

    /**
     * Method validate - tests input String array for null-reference and doubling
     *
     * @param arr String[] array to be sorted
     * @throws IllegalArgumentException if String[] reference is null
     */
    private void validate(String[] arr) throws IllegalArgumentException {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Parameter is empty or null-reference");
        }
        if (this.parsedDepartments == null || !Arrays.equals(this.parsedDepartments, arr)) {
            this.parse(arr);
        }
    }
}
