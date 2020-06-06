package ru.job4j.monitorsync;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ThreadSafeListTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ThreadSafeListTest {

    private ThreadSafeList<String> list;
    private final String first = "first";
    private final String second = "second";
    private final String third = "third";
    private final String fourth = "fourth";
    private Iterator<String> it;

    private final Runnable addFirst = () -> list.add(first);
    private final Runnable addSecond = () -> list.add(second);
    private final Runnable dontAdd = Thread::yield;
    private final Runnable addThird = () -> list.add(third);
    private final Runnable addFourth = () -> list.add(fourth);
    private final Runnable addPair = () -> {
        addFirst.run();
        addSecond.run();
        waitingFiftyMs();
    };
    private final Runnable addNextPair = () -> {
        waitingFiftyMs();
        addThird.run();
        addFourth.run();
    };

    @BeforeEach
    public void init() {
        list = new ThreadSafeList<>();
    }

    private void waitingFiftyMs() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addElements(Runnable a, Runnable b) {
        Thread threadA = new Thread(a);
        Thread threadB = new Thread(b);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void whenAddElementsThenListContainsElements() {
        this.addElements(addPair, addNextPair);
        assertThat(list.size(), is(4));
    }

    @Test
    public void whenGetElementsFromNotEmptyListThenGetElements() {
        this.addElements(addPair, addNextPair);
        assertThat(list.get(0), is(first));
        assertThat(list.get(1), is(second));
        assertThat(list.get(2), is(third));
        assertThat(list.get(3), is(fourth));
    }

    @Test
    public void whenGetFromEmptyListThenGetElements() {
        assertThat(list.get(0) == null, is(true));
        assertThat(list.get(1) == null, is(true));
        assertThat(list.get(2) == null, is(true));
        assertThat(list.get(3) == null, is(true));
    }

    //Iterator tests
    @Test
    public void whenIteratorHasNextThenReturnsTrue() {
        this.addElements(addFirst, addSecond);
        it = list.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(true));
    }

    @Test
    public void whenIteratorHasntNextThenReturnsFalse() {
        it = list.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(false));
    }

    @Test
    public void whenIteratorGetNextThenReturnsElement() {
        this.addElements(addFirst, dontAdd);
        it = list.iterator();
        assertThat(it.next(), is(first));
    }

    @Test
    public void whenSourceListHasBeenModifiedThenFailSafeNextReturnsElement() {
        this.addElements(addPair, dontAdd);
        it = list.iterator();
        this.addElements(addThird, addFourth);
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void whenIteratorNextFromEmptyListThenNextThrowsNSEException() {
        assertThrows(NoSuchElementException.class,
                () -> {
                    it = list.iterator();
                    it.next();
                }
        );

    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        this.addElements(addPair, dontAdd);
        it = list.iterator();
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        this.addElements(addPair, addNextPair);
        it = list.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.next(), is(second));
        assertThat(it.next(), is(third));
        assertThat(it.next(), is(fourth));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        this.addElements(addPair, addNextPair);
        it = list.iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(first));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(second));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(third));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(fourth));
        assertThat(it.hasNext(), is(false));
    }
}
