package ru.job4j.loop;


import org.junit.Test;
import java.util.StringJoiner;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

/**
 * PaintTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PaintTest {

    /**
     * Test. Full pyramid
     */
    @Test
    public void whenPyramidFull() {
        Paint paint = new Paint();
        String result = paint.pyramid(4);
        System.out.println(result);
        assertThat(result,
                is(
                        new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                                .add("   ^   ")
                                .add("  ^^^  ")
                                .add(" ^^^^^ ")
                                .add("^^^^^^^")
                                .toString()
                )
        );
    }

    /**
     * Test. Zero pyramid
     */
    @Test
    public void whenPyramidZeroHeight() {
        Paint paint = new Paint();
        String result = paint.pyramid(0);
        System.out.println(result);
        assertThat(result, is(""));
    }
}
