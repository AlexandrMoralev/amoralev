package ru.job4j.inheritance;

import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * DoctorTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class DoctorTest extends ProfessionalTest {
    /**
     * Test. Doctor heals Patient
     */
    @Test
    public void whenDoctorCuresPatientThenPatientRecovers() {
        Doctor doctor = new Doctor(
                "Antony",
                37,
                true,
                "surgery",
                12,
                new Stack<String>(),
                (byte) 80
        );
        Patient patient = new Patient(
                "Olga",
                false,
                40,
                170,
                70,
                "",
                true
        );
        patient.becomeSick();
        doctor.helpPatient(patient);
        assertThat((patient.isSick() & (patient.getDiseaseSeverity() == 0)), is(true));
    }

    /**
     * Test. Doctor does research work
     */
    @Test
    public void whenDoctorResearchesThenNewMedicalProject() {
        Doctor doctor = new Doctor(
                "Antony",
                37,
                true,
                "surgeon",
                12,
                new Stack<String>(),
                (byte) 80
        );

        String projectName = "NewCure";
        String description = "revolutionary nanotech medicine";

        Project project = doctor.researchWork(projectName, description);

        String result = project.getSpecialization()
                + " "
                + project.getAuthor().getName()
                + " created new project "
                + project.getName()
                + " - "
                + project.getDescription();

        String expected = doctor.getSpecialization()
                + " "
                + doctor.getName()
                + " created new project "
                + projectName
                + " - "
                + description;

        assertThat(result, is(expected));
    }

    /**
     * Test. Joint work of two Doctors
     */
    @Test
    public void whenDoctorNeedsHelpThenImproveQualificationByWorkingTogether() {
        Doctor doctor = new Doctor(
                "Antony",
                37,
                true,
                "surgeon",
                12,
                new Stack<String>(),
                (byte) 80
        );
        Doctor assistant = new Doctor(
                "Bill",
                27,
                false,
                "anesthesiologist",
                2,
                new Stack<String>(),
                (byte) 50
        );

        doctor.workTogether(assistant);

        String result = doctor.getName()
                + " "
                + doctor.getQualificationImprovement().pop()
                + " and "
                + assistant.getName()
                + " "
                + assistant.getQualificationImprovement().pop();

        String expected = "Antony work in team and Bill now can do CPR!";

        assertThat(result, is(expected));

    }
}
