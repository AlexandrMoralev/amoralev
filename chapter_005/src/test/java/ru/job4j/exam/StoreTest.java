package ru.job4j.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * StoreTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreTest {

    private Store store;
    private List<Store.User> previous;
    private List<Store.User> current;

    private Store.User first;
    private Store.User second;
    private String secondDiff;
    private Store.User third;
    private String thirdDiff;

    private final Info zeroInfo = new Info(0, 0, 0);

    @BeforeEach
    public void init() {
        store = new Store();
        previous = new ArrayList<>();
        current = new LinkedList<>();

        first = new Store.User(1, "first");
        second = new Store.User(2, "second");
        secondDiff = "secondDiff";
        third = new Store.User(3, "third");
        thirdDiff = "thirdDiff";
    }

    @Test
    public void whenListsAreEqualsShouldReturnZeroInfo() {
        assertThat(store.diff(previous, current),
                is(zeroInfo)
        );
        previous.add(first);
        previous.add(second);
        previous.add(third);
        current.addAll(previous);
        assertThat(store.diff(previous, current),
                is(zeroInfo)
        );
    }

    @Test
    public void whenPrevListsEmptyShouldInfoWCreated() {
        current.add(first);
        current.add(second);
        current.add(third);
        assertThat(store.diff(previous, current),
                is(new Info(3, 0, 0))
        );
    }

    @Test
    public void whenCurrentListEmptyShouldReturnInfoWDeleted() {
        previous.add(first);
        previous.add(second);
        previous.add(third);
        assertThat(store.diff(previous, current),
                is(new Info(0, 0, 3))
        );
    }

    @Test
    public void whenPreviousListEmptyShouldReturnInfoWCreated() {
        previous.add(first);
        previous.add(second);
        previous.add(third);
        assertThat(store.diff(previous, current),
                is(new Info(0, 0, 3))
        );
    }

    @Test
    public void whenListsDiffersWNewElementsShouldReturnInfoWCreated() {
        previous.add(first);
        previous.add(second);
        current.addAll(previous);
        current.add(third);
        assertThat(store.diff(previous, current),
                is(new Info(1, 0, 0))
        );
    }

    @Test
    public void whenListsDiffersWUpdatedElementsShouldReturnInfoWUpdated() {
        previous.add(first);
        previous.add(second);
        previous.add(third);
        current.addAll(previous);
        current.set(1, new Store.User(2, secondDiff));
        current.set(2, new Store.User(3, thirdDiff));
        assertThat(store.diff(previous, current),
                is(new Info(0, 2, 0))
        );
    }

    @Test
    public void whenListsDiffersWDeletedElementsShouldReturnInfoWDeleted() {
        previous.add(first);
        previous.add(second);
        previous.add(third);
        current.add(first);
        current.add(second);
        current.remove(first);
        current.remove(second);
        assertThat(store.diff(previous, current),
                is(new Info(0, 0, 3))
        );
    }

    @Test
    public void whenListsHasMixedDiffersShouldReturnInfo() {
        previous.add(first);
        previous.add(second);
        current.addAll(previous);
        current.add(third);
        current.remove(first);
        current.set(0, new Store.User(2, secondDiff));
        assertThat(store.diff(previous, current),
                is(new Info(1, 1, 1))
        );
    }

    @Test
    public void whenAnyListIsNullShouldThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> store.diff(null, null)
        );
    }
}
