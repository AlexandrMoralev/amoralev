package ru.job4j.generics;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * RoleStoreTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class RoleStoreTest {

    private final RoleStore<Role> roleStore = new RoleStore<>(3);
    private final Role first = new Role("first");
    private final Role second = new Role("second");
    private final Role third = new Role("third");

    @Test
    public void whenAddingRoleThenStoreHasTheRole() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        assertThat(roleStore.toString().contains("first")
                        && roleStore.toString().contains("second")
                        && roleStore.toString().contains("third"),
                is(true)
        );
    }

    @Test
    public void whenAddingRoleOverStorageSizeThenArrIndexOfBoundsException() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        String err = "";
        try {
            roleStore.add(new Role("fourth"));
        } catch (ArrayIndexOutOfBoundsException e) {
            err = e.getMessage();
        } finally {
            assertThat(err.contains("Array size reached"),
                    is(true)
            );
        }
    }

    @Test
    public void whenReplacingRoleThenStoreHasReplacedRole() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.replace("first", third);
        assertThat(roleStore.toString().contains("third")
                        && roleStore.toString().contains("second")
                        && !roleStore.toString().contains("first"),
                is(true)
        );
    }

    @Test
    public void whenReplacingNonExistingRoleThenStoreHasntNewRole() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.replace("fourth", third);
        assertThat(roleStore.toString().contains("third"),
                is(false)
        );
    }

    @Test
    public void whenDeletingRoleThenStoreHasntRole() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        roleStore.delete("first");
        roleStore.delete("third");
        assertThat(!roleStore.toString().contains("first")
                        && roleStore.toString().contains("second")
                        && !roleStore.toString().contains("third"),
                is(true)
        );
    }

    @Test
    public void whenDeletingNonExistingRoleThenContinueRuntime() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        roleStore.delete("fourth");
        assertThat(roleStore.toString().contains("first")
                        && roleStore.toString().contains("second")
                        && roleStore.toString().contains("third"),
                is(true)
        );
    }

    @Test
    public void whenFindingRoleByIdThenGetRole() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        Role expectedRole = roleStore.findById("third");
        Role anotherExpectedRole = roleStore.findById("first");
        assertThat(expectedRole.equals(third)
                        && anotherExpectedRole.equals(first),
                is(true)
        );
    }

    @Test
    public void whenFindingNonExistingRoleByIdThenGetNull() {
        roleStore.add(first);
        roleStore.add(second);
        roleStore.add(third);
        Role expectedRole = roleStore.findById("fourth");
        assertThat(expectedRole == null,
                is(true)
        );
    }
}
