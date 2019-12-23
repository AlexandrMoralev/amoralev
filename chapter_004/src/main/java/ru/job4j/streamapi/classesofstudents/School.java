package ru.job4j.streamapi.classesofstudents;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * School
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class School {

    public List<Student> collect(List<Student> students, Predicate<Student> predicate) {
        return students.stream().filter(predicate::test).collect(Collectors.toList());
    }
}
