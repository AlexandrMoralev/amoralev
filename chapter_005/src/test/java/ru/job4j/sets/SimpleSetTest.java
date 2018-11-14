package ru.job4j.sets;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleSetTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleSetTest {

    SimpleSet<String> set;
    Iterator it;
    String first = "first";
    String second = "second";
    String third = "third";

    @Before
    public void init() {
        set = new SimpleSet<>();
    }

    @Test
    public void whenAddTreeDifferentItemsThenSetHasThreeItems() {
        set.add(first);
        set.add(second);
        set.add(third);
        assertThat(set.size(), is(3));
    }

    @Test
    public void whenAddThreeEqualItemsThenSetHasOneItem() {
        set.add(first);
        set.add(first);
        set.add(first);
        assertThat(set.size(), is(1));
        assertThat(set.iterator().next(), is(first));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddNullShouldThrowIAException() {
        set.add(null);
    }

    @Test
    public void whenSetIteratorHasNextThenReturnsTrue() {
        set.add(first);
        it = set.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(true));
    }

    @Test
    public void whenSetIteratorHasntNextThenReturnsFalse() {
        it = set.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(false));
    }

    @Test
    public void whenSetIteratorGetNextThenReturnsElement() {
        set.add(first);
        it = set.iterator();
        assertThat(it.next(), is(first));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenSetIteratorHasBeenModifiedThenNextThrowsCMException() {
        set.add(first);
        it = set.iterator();
        set.add(second);
        it.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void whenIteratorCallsNextWithoutArrayElementsThenNextThrowsNSEException() {
        it = set.iterator();
        it.next();
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        set.add(first);
        set.add(second);
        set.add(third);
        it = set.iterator();
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
        assertThat(it.next(), is(third));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        set.add(first);
        set.add(second);
        set.add(third);
        it = set.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
        assertThat(it.next(), is(third));
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        set.add(first);
        set.add(second);
        set.add(third);
        it = set.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(second));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(third));
        assertThat(it.hasNext(), is(false));
    }
}
