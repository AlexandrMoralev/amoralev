package ru.job4j.namedargs;

import org.junit.jupiter.api.Test;
import ru.job4j.inputoutput.namedargs.ArgsName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArgsNameTest {

    @Test
    public void whenGetFirst() {
        ArgsName jvm = ArgsName.of(new String[]{"-Xmx=512", "-encoding=UTF-8"});
        assertEquals("512", jvm.get("Xmx"));
    }

    @Test
    public void whenGetFirstReorder() {
        ArgsName jvm = ArgsName.of(new String[]{"-encoding=UTF-8", "-Xmx=512"});
        assertEquals("512", jvm.get("Xmx"));
    }

    @Test
    public void whenGetNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    ArgsName jvm = ArgsName.of(new String[]{});
                    jvm.get("Xmx");
                }
        );
    }
}
