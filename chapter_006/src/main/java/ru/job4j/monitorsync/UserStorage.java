package ru.job4j.monitorsync;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * UserStorage
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@ThreadSafe
public class UserStorage {

    @GuardedBy("this")
    private final Map<Integer, User> storage = new HashMap<>();

    /**
     * Method add - adds the User to the UserStorage
     *
     * @param user notnull User instance
     * @return boolean true, if the User is successfully added,
     * boolean false, if the User hasn't been added.
     */
    public synchronized boolean add(final User user) {
        validateUser(user);
        User result = this.storage.putIfAbsent(user.getId(), user);
        return result == null;
    }

    /**
     * Method update - updates the User in the UserStorage by Id
     *
     * @param user notnull User instance to replace
     * @return boolean true, if the User is successfully updated,
     * boolean false, if the User hasn't been updated.
     */
    public synchronized boolean update(final User user) {
        validateUser(user);
        boolean result = false;
        int userId = user.getId();
        if (this.storage.containsKey(userId)) {
            result = this.storage.replace(userId, this.storage.get(userId), user);
        }
        return result;
    }

    /**
     * Method delete - deletes the User from the UserStorage
     *
     * @param user notnull User instance to be deleted
     * @return boolean true, if the User is successfully deleted,
     * boolean false, if the User hasn't been deleted.
     */
    public synchronized boolean delete(final User user) {
        validateUser(user);
        return this.storage.remove(user.getId(), user);
    }

    /**
     * Method transfer - transfers amount from one User to another
     *
     * @param fromId int Id of the User, from which the transfer is made
     * @param toId   int User's Id, to which the transfer is made
     * @param amount int value to transfer
     */
    public synchronized void transfer(final int fromId, final int toId, final int amount) {
        User fromUser = this.storage.get(fromId);
        User toUser = this.storage.get(toId);
        if (amount >= 0
                && fromUser != null
                && toUser != null
                && isSolvent(fromUser, amount)) {
            fromUser.setAmount(fromUser.getAmount() - amount);
            toUser.setAmount(toUser.getAmount() + amount);
        }
    }

    public synchronized int size() {
        return this.storage.size();
    }

    public synchronized boolean isEmpty() {
        return this.storage.isEmpty();
    }

    public synchronized boolean contains(final User user) {
        return this.storage.containsValue(user);
    }

    private boolean isSolvent(final User user, final int amount) {
        return user.getAmount() >= amount;
    }

    private void validateUser(final User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
    }
}
