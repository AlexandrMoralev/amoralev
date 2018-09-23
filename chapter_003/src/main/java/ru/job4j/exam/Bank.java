package ru.job4j.exam;

import java.util.*;

/**
 * Bank
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Bank {

    private final Map<User, List<Account>> bank;

    /**
     * Bank instance constructor
     */
    public Bank() {
        this.bank = new HashMap<>();
    }

    /**
     * Method addUser - adds User to bank, and creates empty List of Account
     * @param user User to add
     * checking for a non-null User reference and exclude double-adding the same User
     */
    public void addUser(User user) {
        if (user != null) {
            this.bank.putIfAbsent(user, new ArrayList<Account>());
        }
    }

    /**
     * Method deleteUser - delete User and accounts from bank
     * @param user User to delete
     * @throws IllegalArgumentException if User failed verification
     */
    public void deleteUser(User user) {
        if (user != null) {
            this.bank.remove(user);
        }
    }

    /**
     * Method addAccountToUser - adding Account for User, by passport
     * @param passport String User's passport
     * @param account Account to add
     * @throws IllegalArgumentException if any of the parameters failed validation
     */
    public void addAccountToUser(String passport, Account account) throws IllegalArgumentException {
        if (this.isPassportValid(passport) & this.isAccountVerified(account)) {
            User aUser = this.findUserByPassport(passport);
            if (this.isUserVerified(aUser)) {
                this.bank.get(aUser).add(account);
            }
        }
    }

    /**
     * Method deleteAccountFromUser - deletes User's Account by the passport
     * @param passport String User's passport
     * @param account Account to delete
     * @throws IllegalArgumentException if any of the parameters failed validation
     */
    public void deleteAccountFromUser(String passport, Account account) throws IllegalArgumentException {
        if (this.isPassportValid(passport) && this.isAccountVerified(account)) {
            User aUser = this.findUserByPassport(passport);
            if (this.isUserVerified(aUser)) {
                this.bank.get(aUser).remove(account);
            }
        }
    }

    /**
     * Method getUserAccounts - getting List of Accounts by User passport
     * @param passport String User's passport
     * @return List<Account> of the User, or emptyList
     * @throws IllegalArgumentException if parameter failed validation or there is no such User in the bank
     */
    public List<Account> getUserAccounts(String passport) throws IllegalArgumentException {
        List<Account> result = Collections.emptyList();
        if (this.isPassportValid(passport)) {
            User aUser = this.findUserByPassport(passport);
            if (this.isUserVerified(aUser)) {
                result = this.bank.get(aUser);
            }
        }
        return result;
    }

    /**
     * Method transferMoney - transferring money from one User to another, by passport and requisites
     * @param srcPassport String passport of the User, who transfers money
     * @param srcRequisite String requisites of the User, who transfers money
     * @param destPassport String passport of the User, to whom the money is transferred
     * @param destRequisite String requisites of the User, to whom the money is transferred
     * @param amount Long value of the money to transfer, in cents
     * @return boolean true, if money transferred successfully, and false, if the transfer failed
     * @throws IllegalArgumentException if any of the parameters failed validation
     */
    public boolean transferMoney(String srcPassport,
                                 String srcRequisite,
                                 String destPassport,
                                 String destRequisite,
                                 long amount
    ) throws IllegalArgumentException {
        User srcUser = this.findUserByPassport(srcPassport);
        User destUser = this.findUserByPassport(destPassport);
        return  this.isUserVerified(srcUser)
                && this.isUserVerified(destUser)
                && this.isRequisitesValid(srcRequisite)
                && this.isRequisitesValid(destRequisite)
                && this.findAccountByRequisites(srcUser, srcRequisite)
                .transfer(this.findAccountByRequisites(destUser, destRequisite), amount);
    }

    /**
     * findUserByPassport
     * @param passport String User's passport
     * @return User or Null
     */
    private User findUserByPassport(String passport) {
        User result = null;
        for (User user : bank.keySet()) {
            if (passport.equals(user.getPassport())) {
                result = user;
            }
        }
        return result;
    }

    /**
     * findAccountByRequisites
     * @param user User to search for an account
     * @param requisites String requisites of the account
     * @return Account or Null
     */
    private Account findAccountByRequisites(User user, String requisites) {
        Account result = null;
        for (Account account : this.bank.get(user)) {
            if (requisites.equals(account.getRequisites())) {
                result = account;
            }
        }
        return result;
    }

    /**
     * isUserVerified - User's reference null-checking and User's presence in the bank
     * @param userToVerify User to verify
     * @return boolean true, if verified
     * @throws IllegalArgumentException if verification failed
     */
    private boolean isUserVerified(User userToVerify) throws IllegalArgumentException {
        if (userToVerify == null || !this.bank.containsKey(userToVerify)) {
            throw new IllegalArgumentException("User verification failed");
        }
        return true;
    }

    /**
     * isRequisitesValid - requisites empty checking and requisites reference null-checking
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
     * @param account Account to verify
     * @return boolean true, if verified
     * @throws IllegalArgumentException if verification failed
     */
    private boolean isAccountVerified(Account account) throws IllegalArgumentException {
        if (account == null || account.getRequisites().isEmpty()) {
            throw new IllegalArgumentException("Wrong account");
        }
        return true;
    }

    List<User> getUsers() {
        List<User> result = new ArrayList<>();
        result.addAll(0, this.bank.keySet());
        return result;
    }
}
