package ru.job4j.iterators;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * IteratorOfIteratorsTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class IteratorOfIteratorsTest {
    Iterator<Integer> it;
    Iterator<Integer> it1;
    Iterator<Integer> it2;
    Iterator<Integer> it3;
    Iterator<Iterator<Integer>> its;
    Converter iteratorOfIterators;

    @Before
    public void setUp() {
        it1 = Arrays.asList(1, 2, 3).iterator();
        it2 = Arrays.asList(4, 5, 6).iterator();
        it3 = Arrays.asList(7, 8, 9).iterator();
        its = Arrays.asList(it1, it2, it3).iterator();
        iteratorOfIterators = new Converter();
        it = iteratorOfIterators.convert(its);
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(4));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(5));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(6));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(7));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(8));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(9));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
        assertThat(it.next(), is(5));
        assertThat(it.next(), is(6));
        assertThat(it.next(), is(7));
        assertThat(it.next(), is(8));
        assertThat(it.next(), is(9));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
        assertThat(it.next(), is(5));
        assertThat(it.next(), is(6));
        assertThat(it.next(), is(7));
        assertThat(it.next(), is(8));
        assertThat(it.next(), is(9));
    }

    @Test
    public void hasNextShouldReturnFalseInCaseOfEmptyIterators() {
        it1 = (new ArrayList<Integer>()).iterator();
        it2 = (new ArrayList<Integer>()).iterator();
        it3 = (new ArrayList<Integer>()).iterator();
        its = Arrays.asList(it1, it2, it3).iterator();
        iteratorOfIterators = new Converter();
        it = iteratorOfIterators.convert(its);
        assertThat(it.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void invocationOfNextMethodShouldThrowNoSuchElementException() {
        it1 = Arrays.asList(1, 2, 3).iterator();
        its = Arrays.asList(it1).iterator();
        iteratorOfIterators = new Converter();
        it = iteratorOfIterators.convert(its);
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        it.next();
    }

    @Test
    public void hasNextTest() {
        it1 = (new ArrayList<Integer>()).iterator();
        it2 = (new ArrayList<Integer>()).iterator();
        it3 = (Arrays.asList(1)).iterator();
        its = Arrays.asList(it1, it2, it3).iterator();
        iteratorOfIterators = new Converter();
        it = iteratorOfIterators.convert(its);
        assertThat(it.hasNext(), is(true));
    }
}
