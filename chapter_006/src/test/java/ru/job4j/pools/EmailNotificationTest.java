package ru.job4j.pools;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * EmailNotificationTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class EmailNotificationTest {

    private EmailNotification notification;
    private User firstUser = new User("firstUser", "first@gmail.com");
    private User secondUser = new User("secondUser", "second@gmail.com");
    private User emptyUser = new User(null, null);

    private final PrintStream stdOut = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /**
     * Method setBufferedOutput - replaces stdOut to buffered out
     */
    @BeforeEach
    public void setBufferedOutput() {
        System.setOut(new PrintStream(out));
    }

    @BeforeEach
    public void init() {
        this.notification = new EmailNotification();
    }

    /**
     * Method setStdOutputBack - replaces buffered out back to std System.out after test
     */
    @AfterEach
    public void setStdOutputBack() {
        System.setOut(stdOut);
    }

    @Test
    public void whenEmailToUserThenConstructsEmail() {
        String firstUserTemplate = "Notification firstUser to email first@gmail.com Add a new event to firstUser (first@gmail.com)";
        String secondUserTemplate = "Notification secondUser to email second@gmail.com Add a new event to secondUser (second@gmail.com)";
        notification.emailTo(firstUser);
        notification.emailTo(secondUser);
        notification.close();
        String result = new String(out.toByteArray());
        assertThat(result.contains(firstUserTemplate)
                        && result.contains(secondUserTemplate),
                is(true)
        );
    }

    @Test
    public void whenEmailToInvalidUserShouldThrowIAException() {
        assertThrows(IllegalArgumentException.class,
                () -> notification.emailTo(emptyUser)
        );
    }
}
