package ru.job4j.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * CycledListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class CycledListTest {

    private CycledList<Integer> list;

    private CycledList.Node first;
    private CycledList.Node two;
    private CycledList.Node third;
    private CycledList.Node four;

    @BeforeEach
    public void init() {
        list = new CycledList<>();
        first = list.new Node<>(1);
        two = list.new Node<>(2);
        third = list.new Node<>(3);
        four = list.new Node<>(4);
    }

    @Test
    public void whenListWithoutCycleShouldReturnFalse() {
        first.next = two;
        two.next = third;
        third.next = four;
        four.next = null;
        assertThat(list.hasCycle(first), is(false));
    }

    @Test
    public void whenListHasEndToStartCycleShouldReturnTrue() {
        first.next = two;
        two.next = third;
        third.next = four;
        four.next = first;
        assertThat(list.hasCycle(first), is(true));
    }

    @Test
    public void whenListHasLoopFromAnyToAnyElementShouldReturnTrue() {
        first.next = two;
        two.next = third;
        third.next = first;
        four.next = null;
        assertThat(list.hasCycle(first), is(true));
    }

    @Test
    public void whenListHasLongLoopShouldReturnTrue() {
        first.next = four;
        two.next = third;
        third.next = first;
        four.next = two;
        assertThat(list.hasCycle(first), is(true));
    }
}
