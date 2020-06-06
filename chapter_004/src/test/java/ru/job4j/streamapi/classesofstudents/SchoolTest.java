package ru.job4j.streamapi.classesofstudents;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SchoolTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SchoolTest {

    private final School school = new School();

    private final Predicate<Student> conditionA = student -> student.getScore() >= 70 && student.getScore() <= 100;
    private final Predicate<Student> conditionB = student -> student.getScore() >= 50 && student.getScore() < 70;
    private final Predicate<Student> conditionC = student -> student.getScore() < 50 && student.getScore() >= 0;

    private final Set<String> names = Set.of("Alexey", "Andrey", "Denis", "Sergey", "Ivan", "Artem", "Alexander", "Student");
    private final Set<String> surnames = Set.of("Ivanov", "Petrov", "Sidorov", "Sergeev", "Alexeev", "Denisov", "Artemov", "Sashin", "Studentovich");

    private final List<Student> classA = List.of(
            new Student("Alexey", "Ivanov", 70),
            new Student("Andrey", "Petrov", 72),
            new Student("Denis", "Sidorov", 100));
    private final List<Student> classB = List.of(
            new Student("Sergey", "Sergeev", 50),
            new Student("Alexey", "Alexeev", 60),
            new Student("Ivan", "Denisov", 69));
    private final List<Student> classC = List.of(
            new Student("Artem", "Artemov", 0),
            new Student("Alexander", "Sashin", 20),
            new Student("Student", "Studentovich", 49));
    private final List<Student> outOfBounds = List.of(
            new Student("", "", -1),
            new Student("", "", 101),
            new Student("", "", Integer.MAX_VALUE),
            new Student("", "", Integer.MIN_VALUE));
    private final List<Student> allStudents = Stream.of(classA, classB, classC, outOfBounds).flatMap(Collection::stream).collect(Collectors.toList());

    @Test
    public void collectStudentsIntoClassesTest() {
        assertThat(school.collect(allStudents, conditionA), is(classA));
        assertThat(school.collect(allStudents, conditionB), is(classB));
        assertThat(school.collect(allStudents, conditionC), is(classC));
    }

    @Test
    public void doesntCollectWrongStudentsTest() {
        assertThat(school.collect(allStudents, conditionA), not(outOfBounds));
        assertThat(school.collect(allStudents, conditionB), not(outOfBounds));
        assertThat(school.collect(allStudents, conditionC), not(outOfBounds));
    }

    @Test
    public void convertStudentsTest() {
        Map<String, Student> studentsMap = school.convert(allStudents);
        assertThat(studentsMap.size(), is(10));
        assertTrue(studentsMap.keySet().containsAll(surnames));
        List<Student> validStudents = Stream.of(classA, classB, classC).flatMap(Collection::stream).collect(Collectors.toList());
        assertTrue(studentsMap.values().containsAll(validStudents));
    }

}
