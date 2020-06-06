package ru.job4j.inheritance;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * ProfessionalTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ProfessionalTest {

    /**
     * Test. Checks Stack<String> qualificationImprovement
     */
    @Test
    public void whenImproveQualificationOfProfessionalThenNewSkill() {
        Professional professional = new Professional(
                "Andrey",
                25,
                "secret specialization",
                false
        );

        String skill = "already overqualified";
        String result = "Andrey, 25, secret specialization is already overqualified";

        professional.improveQualification(skill);

        String expected = (professional.getName()
                + ", "
                + professional.getAge()
                + ", "
                + professional.getSpecialization()
                + " is "
                + professional.getQualificationImprovement().pop()
        );

        assertThat(result, is(expected));
    }
}
