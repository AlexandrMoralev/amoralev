package ru.job4j.test;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * DeadLockTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DeadLockTest {

    private DeadLock dLInstance = new DeadLock();
    private Thread direct;
    private Thread reversed;
    private CountDownLatch latch;

    @Before
    public void init() {
        direct = new Thread(dLInstance.getDirectBlock());
        reversed = new Thread(dLInstance.getReversedBlock());
        latch = dLInstance.getLatch();
    }

    @Test
    public void whenUseSharedSynchronizedResourcesThenCanOccurDeadLock() throws InterruptedException {
        direct.start();
        reversed.start();
        Thread.sleep(2000);
        assertThat(dLInstance.getResult() != null, is(true));
        assertThat(dLInstance.getResult().contains("forwardBlock")
                        || dLInstance.getResult().contains("reversedBlock"),
                is(false));
    }

    @Test
    public void whenDeadlockOccursThenLatchIsNotZero() throws InterruptedException {
        direct.start();
        reversed.start();
        Thread.sleep(2000);
        assertThat(latch.getCount() > 0,
                is(true));
    }

    @Ignore
    @Test
    public void whenRepeatingTestsThenResultIsGuaranteed() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            init();
            whenDeadlockOccursThenLatchIsNotZero();
        }
        for (int i = 0; i < 20; i++) {
            init();
            whenUseSharedSynchronizedResourcesThenCanOccurDeadLock();
        }
    }
}
