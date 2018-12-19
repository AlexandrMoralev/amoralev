package ru.job4j.monitorsync;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * UserStorageTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class UserStorageTest {

    private UserStorage storage;
    private final User first = new User(100);
    private final User second = new User(1);
    private final User third = new User(999);
    private final User debtor = new User(-1000);

    @Before
    public void init() {
        storage = new UserStorage();
        storage.add(first);
        storage.add(second);
        storage.add(third);
    }

    // add() tests
    @Test
    public void whenAdd3UsersThenStorageHas3Users() {
        assertThat(storage.size(), is(3));
        assertThat(storage.contains(first), is(true));
        assertThat(storage.contains(second), is(true));
        assertThat(storage.contains(third), is(true));
    }

    @Test
    public void whenAddExistingUserThenUserIsNotAdded() {
        assertThat(storage.add(first), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddingNullShouldThrowIAException() {
        storage.add(null);
    }

    // update() tests
    @Test
    public void whenUpdateUserThenStorageHasUpdatedUser() {
        second.setAmount(2000);
        assertThat(storage.update(second), is(true));
        assertThat(storage.contains(second), is(true));
        assertThat(second.getAmount(), is(2000));
    }

    @Test
    public void whenUpdateNonExistingUserShouldReturnFalse() {
        assertThat(storage.update(debtor), is(false));
        assertThat(storage.update(debtor), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUpdateNullShouldThrowIAException() {
        storage.update(null);
    }

    // delete() tests
    @Test
    public void whenDeleteUserThenStorageHasNtDeletedUser() {
        assertThat(storage.delete(first), is(true));
        assertThat(storage.delete(second), is(true));
        assertThat(storage.delete(third), is(true));
        assertThat(storage.size(), is(0));
    }

    @Test
    public void whenDeleteNonExistingUserShouldReturnFalse() {
        assertThat(storage.delete(debtor), is(false));
        assertThat(storage.delete(new User()), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDeleteNullShouldThrowIAException() {
        storage.delete(null);
    }

    // transfer() tests
    @Test
    public void whenTransfer100ThenTransferredSuccessfully() {
        storage.transfer(first.getId(), second.getId(), 100);
        assertThat(first.getAmount() == 0
                        && second.getAmount() == 101,
                is(true)
        );
        storage.transfer(second.getId(), third.getId(), 100);
        assertThat(second.getAmount() == 1
                        && third.getAmount() == 1099,
                is(true)
        );
        storage.transfer(third.getId(), first.getId(), 100);
        assertThat(third.getAmount() == 999
                        && first.getAmount() == 100,
                is(true)
        );
    }

    @Test
    public void whenTransferFromNonExistingUserShouldNtTransfer() {
        storage.transfer(3, second.getId(), 1000);
        assertThat(second.getAmount() == 1001, is(false));
        storage.transfer(-10, second.getId(), 1000);
        assertThat(second.getAmount() == 1001, is(false));
        storage.transfer(99, first.getId(), 100);
        assertThat(first.getAmount() == 200, is(false));
    }

    @Test
    public void whenTransferToNonExistingUserShouldNtTransfer() {
        storage.transfer(first.getId(), debtor.getId(), 100);
        assertThat(first.getAmount() == 0
                        && debtor.getAmount() == -900,
                is(false)
        );
    }

    @Test
    public void whenFromUserIsNtSolventShouldNtTransfer() {
        this.storage.add(debtor);
        storage.transfer(debtor.getId(), first.getId(), 100);
        assertThat(first.getAmount() == 100
                        && debtor.getAmount() == -1000,
                is(true)
        );
    }
}
