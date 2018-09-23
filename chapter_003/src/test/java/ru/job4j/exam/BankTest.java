package ru.job4j.exam;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * BankTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class BankTest {

    //adding user
    /**
     * Test. whenAddingUserCorrectlyThenNewUser
     */
    @Test
    public void whenAddingUserCorrectlyThenNewUser() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxcvbn");
        bank.addUser(user);
        bank.addAccountToUser(user.getPassport(), account);
        assertThat(bank.getUserAccounts(user.getPassport()).contains(account), is(true));
    }

    /**
     * Test. whenAddingNullUserThenContinueRuntimeWithoutAdding
     */
    @Test
    public void whenAddingNullUserThenContinueRuntimeWithoutAdding() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User nullUser = null;
        bank.addUser(user);
        bank.addUser(nullUser);
        List<User> users = bank.getUsers();
        assertThat(users.contains(nullUser), is(false));
    }

    /**
     * Test. whenAddingSameUserThenContinueRuntimeWithoutAdding
     */
    @Test
    public void whenAddingSameUserThenContinueRuntimeWithoutAdding() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User sameUser = new User("Den", "qwerty");
        bank.addUser(user);
        bank.addUser(sameUser);
        List<User> users = bank.getUsers();
        assertThat(users.size() == 1, is(true));
    }

    //user deleting
    /**
     * Test. whenDeletingExistingUserThenUserGone
     */
    @Test
    public void whenDeletingExistingUserThenUserGone() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User otherUser = new User("Zen", "zxcvbn");
        bank.addUser(user);
        bank.addUser(otherUser);
        bank.deleteUser(user);
        assertThat(bank.getUsers().get(0), is(otherUser));
    }

    /**
     * Test. whenTryingDeleteNotExistingUserThenContinueRuntime
     */
    @Test
    public void whenTryingDeleteNotExistingUserThenContinueRuntime() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User otherUser = new User("Zen", "zxcvbn");
        bank.addUser(user);
        bank.deleteUser(otherUser);
        assertThat(bank.getUsers().size() == 1, is(true));
    }

    /**
     * Test. whenDeleteNullUserThenContinueRuntime
     */
    @Test
    public void whenDeleteNullUserThenContinueRuntime() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User otherUser = null;
        bank.addUser(user);
        bank.deleteUser(otherUser);
        assertThat(bank.getUsers().size() == 1, is(true));
    }

    //adding accounts
    /**
     * Test. whenAddingAccountToExistingUserCorrectlyThenUserHasNewAccount
     */
    @Test
    public void whenAddingAccountToExistingUserCorrectlyThenUserHasNewAccount() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxc");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", account);
        assertThat(bank.getUserAccounts("qwerty").indexOf(account) >= 0, is(true));
    }

    /**
     * Test. whenTryingToAddAccountToNotExistingUserThenIAException
     */
    @Test
    public void whenTryingToAddAccountToNotExistingUserThenIAException() {
        Bank bank = new Bank();
        Account account = new Account(0L, "zxc");
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.addAccountToUser("qwerty", account);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTryingToAddAccountByWrongPassportThenIAException
     */
    @Test
    public void whenTryingToAddAccountByWrongPassportThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxc");
        bank.addUser(user);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.addAccountToUser("qwe", account);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTryingToAddWrongAccountThenIAException
     */
    @Test
    public void whenTryingToAddWrongAccountThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        bank.addUser(user);
        String expectedErrMsg = "Wrong account";
        String eMsg = "";
        try {
            bank.addAccountToUser("qwerty", null);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    //deleting accounts
    /**
     * Test. whenDeleteUserAccountCorrectlyThenAccountGone
     */
    @Test
    public void whenDeleteUserAccountCorrectlyThenAccountGone() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account firstAccount = new Account(0L, "zxc");
        Account secondAccount = new Account(10000L, "asd");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addAccountToUser("qwerty", secondAccount);
        bank.deleteAccountFromUser("qwerty", firstAccount);
        assertThat(bank.getUserAccounts("qwerty").get(0), is(secondAccount));
    }

    /**
     * Test. whenTryingToDeleteUserAccountByWrongPassportThenIAException
     */
    @Test
    public void whenTryingToDeleteUserAccountByWrongPassportThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxc");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", account);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.deleteAccountFromUser("qwe", account);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTryingToDeleteWrongUserAccountThenIAException
     */
    @Test
    public void whenTryingToDeleteWrongUserAccountThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxc");
        Account wrongAccount = new Account(10000L, "asd");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", account);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.deleteAccountFromUser("qwe", wrongAccount);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTryingToDeleteAccountOfNotExistingUserThenIAException
     */
    @Test
    public void whenTryingToDeleteAccountOfNotExistingUserThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        User otherUser = new User("Yan", "asdfgh");
        Account account = new Account(0L, "zxc");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", account);
        bank.addUser(otherUser);
        bank.deleteUser(user);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.deleteAccountFromUser("qwerty", account);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    //get User's List<Account>
    /**
     * Test. whenGettingAccountsOfExistingUserCorrectlyThenListOfAccounts
     */
    @Test
    public void whenGettingAccountsOfExistingUserCorrectlyThenListOfAccounts() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account firstAccount = new Account(0L, "zxc");
        Account secondAccount = new Account(100L, "asd");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addAccountToUser("qwerty", secondAccount);
        List<Account> accounts = bank.getUserAccounts("qwerty");
        assertThat(accounts.contains(firstAccount)
                && accounts.contains(secondAccount)
                && accounts.size() == 2, is(true)
        );
    }

    /**
     * Test. whenTryingToGetAccountsOfNonExistingUserThenIAException
     */
    @Test
    public void whenTryingToGetAccountsOfNonExistingUserThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.getUserAccounts(user.getPassport());
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTryingToGetAccountsByWrongPassportThenIAException
     */
    @Test
    public void whenTryingToGetAccountsByWrongPassportThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account account = new Account(0L, "zxc");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", account);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.getUserAccounts("asdfgh");
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    // money transfer
    /**
     * Test. whenTransferCorrectlyThenSuccess
     */
    @Test
    public void whenTransferCorrectlyThenSuccess() {
        Bank bank = new Bank();
        User srcUser = new User("Den", "qwerty");
        User destUser = new User("Yan", "asdfgh");
        Account firstAccount = new Account(1000L, "qaz");
        Account secondAccount = new Account(1000L, "wsx");
        bank.addUser(srcUser);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addUser(destUser);
        bank.addAccountToUser("asdfgh", secondAccount);

        assertThat(bank.transferMoney(srcUser.getPassport(),
                firstAccount.getRequisites(),
                destUser.getPassport(),
                secondAccount.getRequisites(),
                1000L),
                is(true)
        );
    }

    /**
     * Test. whenTransferWithWrongPassportsThenIAException
     */
    @Test
    public void whenTransferWithWrongPassportsThenIAException() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account firstAccount = new Account(1000L, "qaz");
        Account secondAccount = new Account(1000L, "wsx");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addAccountToUser("qwerty", secondAccount);
        String expectedErrMsg = "User verification failed";
        String eMsg = "";
        try {
            bank.transferMoney(user.getPassport(),
                    firstAccount.getRequisites(),
                    "zxcvbn",
                    secondAccount.getRequisites(),
                    1000L);
        } catch (IllegalArgumentException e) {
            eMsg = e.getMessage();
        } finally {
            assertThat(eMsg, is(expectedErrMsg));
        }
    }

    /**
     * Test. whenTransferWithWrongRequisitesThenTransferIsFalseAndContinueRuntime
     */
    @Test
    public void whenTransferWithWrongRequisitesThenTransferIsFalseAndContinueRuntime() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account firstAccount = new Account(1000L, "qaz");
        Account secondAccount = new Account(1000L, "wsx");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addAccountToUser("qwerty", secondAccount);
        assertThat(
                bank.transferMoney(user.getPassport(),
                        firstAccount.getRequisites(),
                        user.getPassport(),
                        "zzzzzz",
                        1000L),
                is(false)
        );
    }

    /**
     * Test. whenTransferLessThanZeroAmountThenFail
     */
    @Test
    public void whenTransferLessThanZeroAmountThenFail() {
        Bank bank = new Bank();
        User user = new User("Den", "qwerty");
        Account firstAccount = new Account(1000L, "qaz");
        Account secondAccount = new Account(1000L, "wsx");
        bank.addUser(user);
        bank.addAccountToUser("qwerty", firstAccount);
        bank.addAccountToUser("qwerty", secondAccount);
        assertThat(
                bank.transferMoney(user.getPassport(),
                        firstAccount.getRequisites(),
                        user.getPassport(),
                        "qwerty",
                        -1000L),
                is(false)
        );
    }
}
