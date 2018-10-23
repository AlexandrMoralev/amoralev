package ru.job4j.exam;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Bank
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Bank {

    private final Map<User, List<Account>> bank;
    private final DataValidator validator;

    /**
     * Bank instance constructor
     */
    public Bank() {
        this.bank = new HashMap<>();
        this.validator = new DataValidator();
    }

    /**
     * Method addUser - adds User to bank, and creates empty List of Account
     *
     * @param user User to add
     *             checking for a non-null User reference and exclude double-adding the same User
     */
    public void addUser(User user) {
            Stream.of(user).filter(Objects::nonNull).forEach(u -> this.bank.putIfAbsent(user, new ArrayList<>()));
    }

    /**
     * Method deleteUser - delete User and accounts from bank
     *
     * @param user User to delete
     */
    public void deleteUser(User user) {
        Stream.of(user).filter(Objects::nonNull).forEach(this.bank::remove);
    }

    /**
     * Method addAccountToUser - adding Account for User, by passport
     *
     * @param passport String User's passport
     * @param account  Account to be added
     */
    public void addAccountToUser(String passport, Account account) {
        if (validator.isPassportValid(passport) && validator.isAccountValid(account)) {
            Optional<User> userOptional = this.findUserByPassport(passport);
            userOptional.ifPresent(user -> this.bank.get(user).add(account));
        }
    }

    /**
     * Method deleteAccountFromUser - deletes User's Account by the passport
     *
     * @param passport String User's passport
     * @param account  Account to delete
     */
    public void deleteAccountFromUser(String passport, Account account) {
        if (validator.isPassportValid(passport) && validator.isAccountValid(account)) {
            Optional<User> userOptional = this.findUserByPassport(passport);
            userOptional.ifPresent(user -> this.bank.get(user).remove(account));
        }
    }

    /**
     * Method getUserAccounts - getting List of Accounts by User passport
     *
     * @param passport String User's passport
     * @return List<Account> of the User, or emptyList
     * @throws IllegalArgumentException if parameter failed validation or there is no such User in the bank
     */
    public List<Account> getUserAccounts(String passport) throws IllegalArgumentException {
        List<Account> result = Collections.emptyList();
        if (validator.isPassportValid(passport)) {
            Optional<User> userOptional = this.findUserByPassport(passport);
            if (userOptional.isPresent()) {
                result = this.bank.get(userOptional.get());
            }
        }
        return result;
    }

    /**
     * Method transferMoney - transferring money from one User to another, by passport and requisites
     *
     * @param srcPassport   String passport of the User, who transfers money
     * @param srcRequisite  String requisites of the User, who transfers money
     * @param destPassport  String passport of the User, to whom the money is transferred
     * @param destRequisite String requisites of the User, to whom the money is transferred
     * @param amount        Long value of the money to transfer, in cents
     * @return boolean true, if money transferred successfully, and false, if the transfer failed
     * @throws IllegalArgumentException if any of the parameters failed validation
     */
    public boolean transferMoney(String srcPassport,
                                 String srcRequisite,
                                 String destPassport,
                                 String destRequisite,
                                 long amount
    ) throws IllegalArgumentException {

        boolean result = false;

        boolean parametersIsValid = validator.isPassportValid(srcPassport)
                && validator.isPassportValid(destPassport)
                && validator.isRequisitesValid(srcRequisite)
                && validator.isRequisitesValid(destRequisite)
                && amount != 0;

        if (parametersIsValid) {
            Optional<User> srcUser = this.findUserByPassport(srcPassport);
            Optional<User> destUser = this.findUserByPassport(destPassport);

            if (srcUser.isPresent() && destUser.isPresent()
                    && validator.isUserValid(srcUser.get())
                    && validator.isUserValid(destUser.get())) {
                Optional<Account> srcAccount = this.findAccountByRequisites(srcUser.get(), srcRequisite);
                Optional<Account> destAccount = this.findAccountByRequisites(destUser.get(), destRequisite);

                if (srcAccount.isPresent() && destAccount.isPresent()
                        && validator.isAccountValid(srcAccount.get())
                        && validator.isAccountValid(destAccount.get())) {
                    result = srcAccount.get().transfer(destAccount.get(), amount);
                }
            }
        }
        return result;
    }

    /**
     * findUserByPassport
     *
     * @param passport String User's passport
     * @return Optional of User
     */
    private Optional<User> findUserByPassport(String passport) {
        Predicate<User> predicate = user -> passport.equals(user.getPassport());
        return this.bank.keySet().stream().filter(predicate).findFirst();
    }

    /**
     * findAccountByRequisites
     *
     * @param user       User to search for an account
     * @param requisites String requisites of the account
     * @return Account or Null
     */
    private Optional<Account> findAccountByRequisites(User user, String requisites) {
        Predicate<Account> predicate = account -> requisites.equals(account.getRequisites());
        return this.bank.get(user).stream().filter(predicate).findFirst();
    }

    List<User> getUsers() {
        List<User> result = new ArrayList<>();
        result.addAll(0, this.bank.keySet());
        return result;
    }

    private class DataValidator {

        /**
         * isUserVerified - User's reference null-checking and User's presence in the bank
         *
         * @param userToVerify User to verify
         * @return boolean true, if verified
         * @throws IllegalArgumentException if verification failed
         */
        private boolean isUserValid(User userToVerify) throws IllegalArgumentException {
            if (userToVerify == null || !bank.containsKey(userToVerify)) {
                throw new IllegalArgumentException("User verification failed");
            }
            return true;
        }

        /**
         * isRequisitesValid - requisites empty checking and requisites reference null-checking
         *
         * @param requisitesToValidate String requisites to validate
         * @return boolean true, if requisites is valid
         * @throws IllegalArgumentException if validation failed
         */
        private boolean isRequisitesValid(String requisitesToValidate) throws IllegalArgumentException {
            if (requisitesToValidate == null || requisitesToValidate.isEmpty()) {
                throw new IllegalArgumentException("Wrong requisites");
            }
            return true;
        }

        /**
         * isPassportValid - passport empty checking and passport reference null-checking
         *
         * @param passport String passport to validate
         * @return boolean true, if passport is valid
         * @throws IllegalArgumentException if validation failed
         */
        private boolean isPassportValid(String passport) throws IllegalArgumentException {
            if (passport == null || passport.isEmpty()) {
                throw new IllegalArgumentException("Wrong passport");
            }
            return true;
        }

        /**
         * isAccountVerified - Account reference null-checking and empty requisites checking
         *
         * @param account Account to verify
         * @return boolean true, if verified
         * @throws IllegalArgumentException if verification failed
         */
        private boolean isAccountValid(Account account) throws IllegalArgumentException {
            if (account == null || account.getRequisites().isEmpty()) {
                throw new IllegalArgumentException("Wrong account");
            }
            return true;
        }
    }
}
