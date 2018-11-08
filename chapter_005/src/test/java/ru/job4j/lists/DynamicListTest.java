package ru.job4j.lists;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * DynamicListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DynamicListTest {
    DynamicList<String> list = new DynamicList<>();
    Iterator<String> it;

    @Before
    public void init() {
        list.add("first");
        list.add("second");
        list.add("third");
        it = list.iterator();
    }

    @Test
    public void whenAddThreeElementsShouldGetThreeElements() {
        assertThat(list.get(0), is("first"));
        assertThat(list.get(1), is("second"));
        assertThat(list.get(2), is("third"));
        assertThat(list.size(), is(3));
    }

    @Test
    public void whenAddElementsShouldGetFirstAndLastCorrectly() {
        assertThat(list.get(0), is("first"));
        assertThat(list.get(list.size() - 1), is("third"));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenGetNonExistingElementThenThrowsNSEException() {
        list.get(-1);
        list.get(list.size() + 1);
    }

    @Test
    public void whenListIteratorGetNextThenReturnsElement() {
        assertThat(it.next(), is("first"));
    }

    @Test
    public void whenListIteratorHasBeenModifiedThenNextThrowsCMException() {
        list.add("second");
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
    public void whenIteratorCallsNextForEmptyListThenNextThrowsNSEException() {
        DynamicList<String> anotherList = new DynamicList<>();
        Iterator iterator = anotherList.iterator();
        String errMsg = "";
        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            errMsg = e.getMessage();
        } finally {
            assertThat(errMsg, is("No such element"));
        }
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(it.next(), is("first"));
        assertThat(it.next(), is("second"));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is("first"));
        assertThat(it.next(), is("second"));
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is("first"));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is("second"));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is("third"));
        assertThat(it.hasNext(), is(false));
    }
}
