package ru.job4j.exam;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * DepartmentSorterTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DepartmentSorterTest {

    /**
     * Test. parsingAndHierarchyRecoveringTest.
     */
    @Test
    public void parsingAndHierarchyRecoveringTest() {
        String[] departments = {
                "K1\\SK2",
                "K1\\SK1",
                "K2\\SK1\\SSK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1",
                "K2\\SK1\\SSK2",
        };
        String[] expected = {
                "K1",
                "K1\\SK1",
                "K1\\SK1\\SSK1",
                "K1\\SK1\\SSK2",
                "K1\\SK2",
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK1",
                "K2\\SK1\\SSK2"
        };
        DepartmentSorter departmentSorter = new DepartmentSorter();
        String[] result = departmentSorter.ascendingSort(departments);
        assertThat(result, is(expected));
    }

    /**
     * Test. ascendingSortTest.
     */
    @Test
    public void ascendingSortTest() {
        String[] departments = {
                "K1",
                "K1\\SK2",
                "K1\\SK1",
                "K2\\SK1\\SSK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1",
                "K2\\SK1\\SSK2",
                "K2\\SK1",
                "K2"
        };
        String[] expected = {
                "K1",
                "K1\\SK1",
                "K1\\SK1\\SSK1",
                "K1\\SK1\\SSK2",
                "K1\\SK2",
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK1",
                "K2\\SK1\\SSK2"
        };
        DepartmentSorter departmentSorter = new DepartmentSorter();
        String[] result = departmentSorter.ascendingSort(departments);
        assertThat(result, is(expected));
    }

    /**
     * Test. descendingSortTest.
     */
    @Test
    public void descendingSortTest() {
        String[] departments = {
                "K2\\SK1",
                "K1\\SK1\\SSK2",
                "K2",
                "K2\\SK1\\SSK1",
                "K1",
                "K2\\SK1\\SSK2",
                "K1\\SK1",
                "K1\\SK2",
                "K1\\SK1\\SSK1"
        };
        String[] expected = {
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK2",
                "K2\\SK1\\SSK1",
                "K1",
                "K1\\SK2",
                "K1\\SK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1"
        };
        DepartmentSorter departmentSorter = new DepartmentSorter();
        String[] result = departmentSorter.descendingSort(departments);
        assertThat(result, is(expected));
    }

    @Test
    public void whenParsingNullArrayThenIAException() {
        String[] departments = null;
        String[] expected = {
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK2",
                "K2\\SK1\\SSK1",
                "K1",
                "K1\\SK2",
                "K1\\SK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1"
        };
        String[] result = null;
        String errMsg = "";
        String exp = "Parameter is empty or null-reference";
        DepartmentSorter departmentSorter = new DepartmentSorter();
        try {
            result = departmentSorter.descendingSort(departments);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        } finally {
            assertThat(Arrays.equals(expected, result)
                            & errMsg.equals(exp),
                    is(false)
            );
        }
    }

    @Test
    public void whenParsingEmptyArrayThenAIException() {
        String[] departments = new String[10];
        String[] expected = {
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK2",
                "K2\\SK1\\SSK1",
                "K1",
                "K1\\SK2",
                "K1\\SK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1"
        };
        String[] result = null;
        String errMsg = "";
        String exp = "Parameter is empty or null-reference";
        DepartmentSorter departmentSorter = new DepartmentSorter();
        try {
            result = departmentSorter.descendingSort(departments);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        } finally {
            assertThat(Arrays.equals(expected, result)
                            & errMsg.equals(exp),
                    is(false)
            );
        }
    }
}
