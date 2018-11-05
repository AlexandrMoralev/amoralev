package ru.job4j.lists;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleArrayListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleArrayListTest {

    private SimpleArrayList<Integer> list;

    @Before
    public void beforeTest() {
        list = new SimpleArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
    }

    @Test
    public void whenAddThreeElementsThenUseGetOneResultTwo() {
        assertThat(list.get(1), is(2));
    }

    @Test
    public void whenAddThreeElementsThenUseGetSizeResultThree() {
        assertThat(list.getSize(), is(3));
    }

    @Test
    public void whenDeleteTwoElementsThenListSizeIsOne() {
        list.delete();
        list.delete();
        assertThat(list.getSize() == 1
                        && list.get(0).equals(1),
                is(true)
        );
    }

    @Test
    public void whenDeleteElementFromEmptyListThenReturnsNull() {
        Integer first = list.delete();
        Integer second = list.delete();
        Integer third = list.delete();
        Integer fourth = list.delete();
        assertThat(first == 3
                        && second == 2
                        && third == 1
                        && fourth == null,
                is(true)
        );
    }
}
