package ru.job4j.inheritance;

import java.util.Stack;

/**
 * Doctor
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Doctor extends Professional {

    private byte percentageOfCuredPatients;

    /**
     * Instance constructor of the Doctor class
     *
     * @param name String
     * @param age int
     * @param hasJob boolean
     * @param specialization String
     * @param experienceInYears int
     * @param qualificationImprovement String[]
     * @param percentageOfCuredPatients byte, MAX = 100%
     */
    public Doctor(
            String name,
            int age,
            boolean hasJob,
            String specialization,
            int experienceInYears,
            Stack<String> qualificationImprovement,
            byte percentageOfCuredPatients
    ) {
        super(name, age, hasJob, specialization, experienceInYears, qualificationImprovement);
        this.percentageOfCuredPatients = percentageOfCuredPatients;
    }

    /**
     * Method helpPatient - Doctor determines diagnose of Patient, his disease and disease severity
     * then Doctor heals the Patient
     * @param patient Patient that needs help
     */
    public void helpPatient(Patient patient) {

        if (patient.isSick()) {
            patient.setCurrentDisease("diagnose: " + patient.getName() + " is ill");
            patient.setDiseaseSeverity((byte) (Math.random() * 126));

            for (int diseaseSeverityLevel = patient.getDiseaseSeverity(); diseaseSeverityLevel >= 0; diseaseSeverityLevel--) {
                patient.sneeze();
                patient.fallAsleep();
            }
            this.updatePercentageOfCuredPatients();
        }
    }

    /**
     * Method updatePercentageOfCuredPatients - calculates it
     * max percentage == 100, minimum == 0;
     */
    private void updatePercentageOfCuredPatients() {
        this.percentageOfCuredPatients = (this.percentageOfCuredPatients < 101 & this.percentageOfCuredPatients > 0) ? this.percentageOfCuredPatients++ : 100;
    }

    /**
     * @return percentage of cured Patients
     */
    public byte getPercentageOfCuredPatients() {
        return percentageOfCuredPatients;
    }

    /**
     * Method workTogether - by default improves Doctor qualification
     * !!! need to rewrite with multithreading
     * @param assistant another Doctor
     */
    public void workTogether(Doctor assistant) {
        assistant.improveQualification("now can do CPR!");
        this.improveQualification("work in team");
    }

    /**
     * Method researchWork - Doctor creates new research Project
     * @param projectName name of the Project
     * @param projectDescription - short description of the Project
     * @return Project
     */
    public Project researchWork(String projectName, String projectDescription) {
        return new Project(projectName, projectDescription, this);
    }

    /**
     * @return Stack<String> qualification improvements of the Doctor
     */
    @Override
    public Stack<String> getQualificationImprovement() {
        return super.getQualificationImprovement();
    }

    /**
     * @param skill String, new qualification improvement
     */
    @Override
    public void improveQualification(String skill) {
        super.improveQualification(skill);
    }
}

