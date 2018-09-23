package ru.job4j.exam;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * AccountTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class AccountTest {

    /**
     * Test. successfulTransferTest
     */
    @Test
    public void successfulTransferTest() {
        Account src = new Account(1000L, "qwerty");
        Account dest = new Account(0L, "qaws");
        src.transfer(dest, 900L);
        assertThat(src.getValue() == 100
                        && dest.getValue() == 900,
                is(true)
        );
    }

    /**
     * Test. whenDestAccountIsNullThenFail
     */
    @Test
    public void whenDestAccountIsNullThenFail() {
        Account src = new Account(1000L, "qwerty");
        Account dest = null;
        assertThat(src.transfer(dest, 1000L), is(false));
    }

    /**
     * Test. whenTransferValueLessOrEqualZeroThenFail
     */
    @Test
    public void whenTransferValueLessOrEqualZeroThenFail() {
        Account src = new Account(1000L, "qwerty");
        Account dest = new Account(2000L, "qaws");
        assertThat(src.transfer(dest, 0L)
                        | src.transfer(dest, -1000L),
                is(false)
        );
    }

    /**
     * Test. whenTransferValueGreaterThanSrcAccountValueThenFail
     */
    @Test
    public void whenTransferValueGreaterThanSrcAccountValueThenFail() {
        Account src = new Account(1000L, "qwerty");
        Account dest = new Account(0L, "qaws");
        assertThat(src.transfer(dest, 1001L), is(false));
    }
}
