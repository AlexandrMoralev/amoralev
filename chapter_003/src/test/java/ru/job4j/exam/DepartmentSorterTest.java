package ru.job4j.exam;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * DepartmentSorterTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DepartmentSorterTest {

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

    /**
     * Test. equalsTest.
     */
    @Test
    public void equalsTest() {
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
        String[] result = {
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
        assertThat(result, is(expected));
    }
}
