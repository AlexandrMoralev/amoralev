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
     * Test. Right triangle
     */
    @Test
    public void whenPyramidRight() {
        Paint paint = new Paint();
        String result = paint.rightTrl(4);
        System.out.println(result);
        assertThat(result,
                is(
                        new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                                .add("^   ")
                                .add("^^  ")
                                .add("^^^ ")
                                .add("^^^^")
                                .toString()
                )
        );
    }

    /**
     * Test. Left triangle
     */
    @Test
    public void whenPyramidLeft() {
        Paint paint = new Paint();
        String result = paint.leftTrl(4);
        System.out.println(result);
        assertThat(result,
                is(
                        new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                                .add("   ^")
                                .add("  ^^")
                                .add(" ^^^")
                                .add("^^^^")
                                .toString()
                )
        );
    }

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
}
