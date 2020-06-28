package ru.job4j;

import ru.job4j.common.NamedArgs;

import java.io.IOException;
import java.util.Collection;

public interface Search<T> {

    Collection<T> findFiles(final NamedArgs params) throws IOException;

}
