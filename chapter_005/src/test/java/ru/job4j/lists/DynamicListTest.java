package ru.job4j.lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * DynamicListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DynamicListTest {
    DynamicList<String> list;
    Iterator<String> it;

    @BeforeEach
    public void init() {
        list = new DynamicList<>();
        list.add("first");
        list.add("second");
        list.add("third");
        it = list.iterator();
    }

    // adding tests
    @Test
    public void whenAddThreeElementsShouldGetThreeElements() {
        assertThat(list.get(0), is("first"));
        assertThat(list.get(1), is("second"));
        assertThat(list.get(2), is("third"));
        assertThat(list.size(), is(3));
    }

    @Test
    public void whenAddFirstThreeElementsShouldGetThreeElements() {
        DynamicList<String> dynamicList = new DynamicList<>();
        dynamicList.add("first");
        dynamicList.add("second");
        dynamicList.add("third");
        assertThat(dynamicList.get(0), is("first"));
        assertThat(dynamicList.get(1), is("second"));
        assertThat(dynamicList.get(2), is("third"));
        assertThat(dynamicList.size(), is(3));
    }

    @Test
    public void whenAddElementsShouldGetFirstAndLastCorrectly() {
        assertThat(list.get(0), is("first"));
        assertThat(list.get(list.size() - 1), is("third"));
    }

    //deleting tests
    @Test
    public void whenDeleteSingleElementThenListIsEmpty() {
        DynamicList<String> dynamicList = new DynamicList<>();
        dynamicList.add("first");
        dynamicList.deleteFirst();
        assertThat(dynamicList.size(), is(0));
    }

    @Test
    public void whenDeleteAllElementsThenListIsEmpty() {
        list.deleteFirst();
        list.deleteFirst();
        list.deleteFirst();
        assertThat(list.size(), is(0));
    }

    @Test
    public void whenDeleteElementFromEmptyListShouldThrowNSEException() {
        DynamicList<String> anotherList = new DynamicList<>();
        assertThrows(NoSuchElementException.class, anotherList::deleteFirst);

    }

    // getting tests
    @Test
    public void whenGetNonExistingElementThenThrowsNSEException() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    list.get(-1);
                    list.get(list.size() + 1);
                }
        );
    }

    @Test
    public void whenGetFirstElementShouldReturnFirstElement() {
        assertThat(list.getFirst(), is("first"));
        assertThat(list.size(), is(3));
    }

    @Test
    public void whenGettingFirstElementOfEmptyListShouldThrowNSEException() {
        DynamicList<String> anotherList = new DynamicList<>();
        assertThrows(NoSuchElementException.class, anotherList::getFirst);
    }

    //iterator tests
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
