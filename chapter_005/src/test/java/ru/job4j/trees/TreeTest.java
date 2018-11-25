package ru.job4j.trees;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * TreeTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TreeTest {

    private Tree<Integer> tree;
    private Iterator it;

    @Before
    public void init() {
        tree = new Tree<>(1);
    }

    @Test
    public void when6ElFindLastThen6() {
        assertThat(tree.add(1, 2), is(true));
        assertThat(tree.add(1, 3), is(true));
        assertThat(tree.add(1, 4), is(true));
        assertThat(tree.add(4, 5), is(true));
        assertThat(tree.add(5, 6), is(true));
        assertThat(
                tree.findBy(1).isPresent(),
                is(true)
        );
        assertThat(
                tree.findBy(2).isPresent(),
                is(true)
        );
        assertThat(
                tree.findBy(3).isPresent(),
                is(true)
        );
        assertThat(
                tree.findBy(4).isPresent(),
                is(true)
        );
        assertThat(
                tree.findBy(5).isPresent(),
                is(true)
        );
        assertThat(
                tree.findBy(6).isPresent(),
                is(true)
        );
    }

    @Test
    public void when6ElFindNotExitThenOptionEmpty() {
        tree.add(1, 2);
        assertThat(
                tree.findBy(7).isPresent(),
                is(false)
        );
    }

    @Test
    public void whenAddThreeEqualElementsThenRootHasOneLeaf() {
        assertThat(tree.add(4, 5), is(false));
        assertThat(
                tree.findBy(5).isPresent(),
                is(false)
        );
    }

    @Test
    public void whenAddingElementsToNonExistingParentThenElementIsNotAdded() {
        assertThat(tree.add(1, 2), is(true));
        assertThat(tree.add(1, 2), is(false));
        assertThat(tree.add(1, 2), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddNullShouldThrowIAException() {
        tree.add(1, null);
    }

    @Test
    public void whenTreeIteratorHasNextThenReturnsTrue() {
        tree.add(1, 2);
        it = tree.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(true));
    }

    @Test
    public void whenTreeIteratorHasntNextThenReturnsFalse() {
        it = tree.iterator();
        it.next();
        assertThat(it.hasNext() && it.hasNext(), is(false));
    }

    @Test
    public void whenTreeIteratorGetNextThenReturnsElement() {
        tree.add(1, 2);
        it = tree.iterator();
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenTreeIteratorHasBeenModifiedThenNextThrowsCMException() {
        tree.add(1, 2);
        it = tree.iterator();
        tree.add(1, 3);
        it.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void whenIteratorCallsNextWithoutArrayElementsThenNextThrowsNSEException() {
        it = tree.iterator();
        assertThat(it.next(), is(1));
        it.next();
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        tree.add(1, 2);
        tree.add(1, 3);
        tree.add(2, 4);
        it = tree.iterator();
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        tree.add(1, 2);
        tree.add(1, 3);
        tree.add(2, 4);
        it = tree.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        tree.add(1, 2);
        tree.add(1, 3);
        tree.add(2, 4);
        it = tree.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(1));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(4));
        assertThat(it.hasNext(), is(false));
    }
}