package ru.job4j.lists;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * DynamicArrayTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DynamicArrayTest {
    DynamicArray<String> array = new DynamicArray<>(4);
    String first = "first";
    String second = "second";
    String third = "third";
    Iterator<String> it;

    @Test
    public void whenCreatingArrayOfWrongSizeThenArrayHasZeroLength() {
        DynamicArray<String> negativeArray = new DynamicArray<>(-1);
        DynamicArray<String> longArray = new DynamicArray<>(Integer.MAX_VALUE + 1);
        assertThat(negativeArray.capacity() == 0
                        && longArray.capacity() == 0,
                is(true)
        );
    }

    @Test
    public void whenAddElementThenElementIsInArray() {
        array.add(first);
        array.add(second);
        array.add(third);
        assertThat(array.size(), is(3));
    }

    @Test
    public void whenAddingMoreThanArraySizeElementsThenArrayIncreasingSize() {
        DynamicArray<String> array = new DynamicArray<>(4);
        for (int i = 0; i < 4; i++) {
            array.add("item " + i);
        }
        assertThat(array.capacity(), is(16));
    }

    @Test
    public void whenGettingExistingElementByIndexThenHasTheElement() {
        array.add(first);
        array.add(second);
        array.add(third);
        assertThat(array.get(0).equals(first)
                        && array.get(1).equals(second)
                        && array.get(2).equals(third)
                        && null == array.get(3),
                is(true)
        );
    }

    @Test
    public void whenGettingNonExistingElementByIndexThenGetNull() {
        array.add(first);
        assertThat(array.get(0).equals(first)
                        && null == array.get(1)
                        && null == array.get(2)
                        && null == array.get(3)
                        && null == array.get(4),
                is(true)
        );
    }

    @Test
    public void whenArrayIteratorHasNextThenReturnsTrue() {
        array.add(first);
        it = array.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(true));
    }

    @Test
    public void whenArrayIteratorHasntNextThenReturnsFalse() {
        it = array.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(false));
    }

    @Test
    public void whenArrayIteratorGetNextThenReturnsElement() {
        array.add(first);
        it = array.iterator();
        assertThat(it.next(), is(first));
    }

    @Test
    public void whenArrayIteratorHasBeenModifiedThenNextThrowsCMException() {
        array.add(first);
        it = array.iterator();
        array.add(second);
        String errMsg = "";
        try {
            it.next();
        } catch (ConcurrentModificationException e) {
            errMsg = e.getMessage();
        } finally {
            assertThat(errMsg, is("ConcurrentModification state"));
        }
    }

    @Test
    public void whenIteratorCallsNextWithoutArrayElementsThenNextThrowsNSEException() {
        it = array.iterator();
        String errMsg = "";
        try {
            it.next();
        } catch (NoSuchElementException e) {
            errMsg = e.getMessage();
        } finally {
            assertThat(errMsg, is("No such element"));
        }
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        array.add(first);
        array.add(second);
        it = array.iterator();
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        array.add(first);
        array.add(second);
        it = array.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        array.add(first);
        array.add(second);
        it = array.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(second));
        assertThat(it.hasNext(), is(false));
    }
}
