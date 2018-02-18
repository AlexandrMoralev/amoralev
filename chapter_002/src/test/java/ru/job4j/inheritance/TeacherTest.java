package ru.job4j.inheritance;


import org.junit.Test;
import java.util.Stack;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

/**
 * TeacherTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TeacherTest extends ProfessionalTest {
    /**
     * Test. Teacher gives knowledge to the Student
     */
    @Test
    public void whenTeachesStudentThenStudentGetsKnowledge() {
        Teacher teacher = new Teacher(
                "Grigory",
                56,
                true,
                "Math",
                20,
                new Stack<>(),
                0
        );
        Student student = new Student(
                "Ivan",
                20,
                0,
                "MGIMO",
                "lawyer",
                (byte) 3,
                3
        );

        String knowledge = "this.knowledge is the power";

        teacher.teachStudent(student, knowledge);

        assertThat(student.getKnowledge().pop(), is(knowledge));

    }

    /**
     * Test. Teacher evaluates Student's homework
     */
    @Test
    public void whenTeacherChecksHomeworkThenStudentGetScore() {
        Teacher teacher = new Teacher(
                "Grigory",
                56,
                true,
                "Math",
                20,
                new Stack<>(),
                1
        );
        Student student = new Student(
                "Ivan",
                20,
                0,
                "MGIMO",
                "lawyer",
                (byte) 3,
                3
        );

        String knowledge = "this.knowledge is the power";

        teacher.teachStudent(student, knowledge);

        student.doHomework();

        assertThat((teacher.checkHomework(student) > 0 & teacher.checkHomework(student) < 6), is(true));
    }

    /**
     * Test. When Student is ready for exam, he pass it and go to next education stage
     */
    @Test
    public void whenTeacherTestsStudentThenStudentGetScoreAndNextStageOfEducation() {
        Teacher teacher = new Teacher(
                "Grigory",
                56,
                true,
                "Math",
                20,
                new Stack<>(),
                0
        );
        Student student = new Student(
                "Ivan",
                20,
                0,
                "MGIMO",
                "lawyer",
                (byte) 0,
                4
        );

        student.prepareForExam();

        boolean result = (teacher.testStudent(student) > 0) & (student.getCurrentStageOfEducation() > 0);

        assertThat(result, is(true));

    }
}
