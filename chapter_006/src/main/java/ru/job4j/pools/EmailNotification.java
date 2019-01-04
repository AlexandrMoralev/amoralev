package ru.job4j.pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * EmailNotification
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class EmailNotification {

    private final ExecutorService pool;

    /**
     * Constructs EmailNotification instance
     */
    public EmailNotification() {
        this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Method emailTo - creates an email to send, using User's data and email-template,
     * then submit task to the executor service
     *
     * @param user notnull User to whom the email is created
     */
    public void emailTo(final User user) {
        validateUser(user);
        String email = user.getEmail();
        String username = user.getName();
        String subject = String.format("Notification %s to email %s", username, email);
        String body = String.format("Add a new event to %s", username);
        this.pool.submit(() -> send(subject, body, email));
    }

    private void validateUser(User user) {
        if (user == null
                || user.getName().isEmpty()
                || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void send(final String subject, final String body, final String email) {
        System.out.println(subject + " " + body + " (" + email + ")"); // for unit testing
    }

    /**
     * Method close - shuts down the executor service
     */
    public void close() {
        this.pool.shutdown();
        while (!this.pool.isTerminated()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
