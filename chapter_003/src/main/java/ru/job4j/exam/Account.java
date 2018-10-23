package ru.job4j.exam;

import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * Account
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Account {

    private Long value;
    private String requisites;

    /**
     * Account instance constructor
     *
     * @param value      Long value, in cents
     * @param requisites String requisites
     */
    public Account(Long value, String requisites) {
        this.value = value;
        this.requisites = requisites;
    }

    /**
     * getValue
     *
     * @return Long value, in cents
     */
    Long getValue() {
        return value;
    }

    /**
     * setValue
     *
     * @param value long, in cents
     */
    private void setValue(Long value) {
        this.value = value;
    }

    /**
     * getRequisites
     *
     * @return String or Null
     */
    String getRequisites() {
        return requisites;
    }

    /**
     * setRequisites
     *
     * @param requisites String
     */
    private void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    /**
     * Method transfer
     *
     * @param account destination Account for transfer
     * @param value   Long amount, in cents, for transfer
     * @return boolean true, if transfer successful, false, if failed
     */
    boolean transfer(Account account, Long value) {

        BiPredicate<Account, Long> transferPredicate = (acc, num) -> (acc != null && num > 0 && this.getValue() >= num);

        boolean isTransferExecutable = transferPredicate.test(account, value);

        if (isTransferExecutable) {
            this.setValue(this.getValue() - value);
            account.setValue(account.getValue() + value);
        }
        return isTransferExecutable;
    }
}
