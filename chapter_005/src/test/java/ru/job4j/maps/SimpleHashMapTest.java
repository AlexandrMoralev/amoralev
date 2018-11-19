package ru.job4j.maps;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SimpleHashMapTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SimpleHashMapTest {

    private SimpleHashMap<String, String> table;
    private Iterator it;
    private String fst = "fst";
    private String snd = "snd";
    private String thrd = "thrd";
    private String first = "first";
    private String second = "second";
    private String third = "third";
    private String result;
    private SimpleHashMap.Bucket bucket;

    @Before
    public void init() {
        table = new SimpleHashMap<>(3);
        result = "";
        bucket = null;
    }

    @Test
    public void whenInsertSomeValuesShouldReturnThemWithIterator() {
        SimpleHashMap<Integer, String> table = new SimpleHashMap<>();
        table.insert(5, "5");
        table.insert(7, "7");
        table.insert(9, "9");
        table.insert(11, "11");
        Iterator<SimpleHashMap.Bucket> iterator = table.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getValue());
        }
    }

    // Inserting tests
    @Test
    public void whenInsertTwoElementsThenSizeIsTwo() {
        boolean result = table.insert(first, fst);
        result &= table.insert(second, snd);
        result &= table.insert(third, thrd);
        assertThat(table.getSize(), is(3));
        assertThat(result, is(true));
    }

    @Test
    public void whenInsertTwoEqualElementsThenMapHasOnlyOne() {
        boolean result = table.insert(first, fst);
        result &= table.insert(first, fst);
        assertThat(table.getSize(), is(1));
        assertThat(result, is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInsertElementWithNullKeyShouldThrowIAException() {
        table.insert(null, fst);
    }

    @Test
    public void whenInsertTooMuchElementsShouldResizeMap() {
        boolean result = table.insert(first, fst);
        result &= table.insert(second, snd);
        result &= table.insert(third, thrd);
        result &= table.insert("even eleven", "is fourth");
        assertThat(result, is(true));
        assertThat(table.getSize(), is(4));
    }

    // Get tests
    @Test
    public void whenInsertTwoElementsShouldGetTwoElements() {
        boolean result = table.insert(first, fst);
        result &= table.insert(second, snd);
        assertThat(result, is(true));
        assertThat(table.getSize() == 2, is(true));
        assertThat(table.get(first), is(fst));
        assertThat(table.get(second), is(snd));
    }

    @Test
    public void whenGetNonExistingElementShouldReturnNull() {
        table.get(first);
        assertThat(table.get("fourth") == null, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGetElementByNullKeyShouldThrowIAException() {
        table.get(null);
    }

    // Delete tests
    @Test
    public void whenDeleteTwoOfTwoElementsThenTableSizeIsZero() {
        boolean result = table.insert(first, fst);
        result &= table.insert(second, snd);
        assertThat(result, is(true));
        result = table.delete(first);
        result &= table.delete(second);
        assertThat(result, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteElementByNullKeyShouldThrowIAException() {
        table.delete(null);
    }

    @Test
    public void whenDeleteElementByWrongKeyShouldReturnFalse() {
        table.insert(first, fst);
        assertThat(table.delete(first), is(true));
        assertThat(table.delete(first), is(false));
    }

    // Iterator tests
    @Test
    public void whenSetIteratorHasNextThenReturnsTrue() {
        table.insert(first, fst);
        it = table.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(true));
    }

    @Test
    public void whenSetIteratorHasntNextThenReturnsFalse() {
        it = table.iterator();
        assertThat(it.hasNext() && it.hasNext(), is(false));
    }

    @Test
    public void whenIteratorGetNextThenReturnsElement() {
        table.insert(first, fst);
        it = table.iterator();
        SimpleHashMap.Bucket bucket = (SimpleHashMap.Bucket) it.next();
        assertThat(bucket.getKey(), is(first));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void whenIteratorHasBeenModifiedThenNextThrowsCMException() {
        table.insert(first, fst);
        it = table.iterator();
        table.insert(second, snd);
        it.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void whenIteratorCallsNextWithoutArrayElementsThenNextThrowsNSEException() {
        it = table.iterator();
        it.next();
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        table.insert(first, fst);
        table.insert(second, snd);
        table.insert(third, thrd);
        it = table.iterator();

        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";

        assertThat(result.contains(first)
                        && result.contains(second)
                        && result.contains(third),
                is(true)
        );
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        table.insert(first, fst);
        table.insert(second, snd);
        table.insert(third, thrd);
        it = table.iterator();

        assertThat(it.hasNext()
                        && it.hasNext()
                        && it.hasNext(),
                is(true)
        );

        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";

        assertThat(result.contains(first)
                        && result.contains(second)
                        && result.contains(third),
                is(true)
        );
    }

    @Test
    public void hasNextNextSequentialInvocation() {
        table.insert(first, fst);
        table.insert(second, snd);
        table.insert(third, thrd);
        it = table.iterator();

        assertThat(it.hasNext(), is(true));
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";

        assertThat(it.hasNext(), is(true));
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";

        assertThat(it.hasNext(), is(true));
        bucket = (SimpleHashMap.Bucket) it.next();
        result += bucket.getKey() + " ";

        assertThat(it.hasNext(), is(false));

        assertThat(result.contains(first)
                        && result.contains(second)
                        && result.contains(third),
                is(true)
        );
    }
}
