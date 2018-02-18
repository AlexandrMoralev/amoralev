package ru.job4j.inheritance;

import java.util.Stack;

/**
 * Student
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Student {

    private String name;
    private int age;
    private long telNumber;
    private String educationalInstitution;
    private String specialization;
    private byte currentStageOfEducation;
    private int averageScore;
    private boolean isReadyForExam;
    private String[] homework;
    private Stack<String> knowledge;

    /**
     * Instance constructor of the Student class
     *
     * @param name String
     * @param age int
     * @param telNumber long, without "+" and braces
     * @param educationalInstitution String
     * @param specialization String
     * @param currentStageOfEducation byte
     * @param averageScore int
     */
    public Student(
            String name,
            int age,
            long telNumber,
            String educationalInstitution,
            String specialization,
            byte currentStageOfEducation,
            int averageScore
    ) {
        this.name = name;
        this.age = age;
        this.telNumber = telNumber;
        this.educationalInstitution = educationalInstitution;
        this.specialization = specialization;
        this.currentStageOfEducation = currentStageOfEducation;
        this.averageScore = averageScore;
        this.homework = new String[10];
        this.knowledge = new Stack<>();
    }

    /**
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * @return int age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return long telephone number, without "+" and braces
     */
    public long getTelNumber() {
        return telNumber;
    }

    /**
     * Method setTelNumber - set new telephone number of the Student instance
     * @param telNumber long telephone number, without "+" and braces
     */
    public void setTelNumber(long telNumber) {
        this.telNumber = telNumber;
    }

    /**
     * @return String name of educational institution, where the Student is enrolled
     */
    public String getEducationalInstitution() {
        return educationalInstitution;
    }

    /**
     * Method setEducationalInstitution - enrollment of the Student
     * @param educationalInstitution String name of educational institution
     */
    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    /**
     * @return String short description of the Student's specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Metod setSpecialization - sets new specialization of the Student
     * @param specialization String short description of the Student's specialization
     */
    void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * @return String[] of Student's knowledge
     */
    public Stack<String> getKnowledge() {
        return this.knowledge;
    }

    /**
     * @return boolean Student's readiness for exam
     */
    public boolean isReadyForExam() {
        return isReadyForExam;
    }

    /**
     * @return String[] Student's homework to check
     */
    public String[] getHomework() {
        return homework;
    }

    /**
     * Method setKnowledge - emulation of learning process
     * @param knowledge String, some knowledge
     */
    void setKnowledge(String knowledge) {
        this.knowledge.push(knowledge);
    }

    /**
     * @return byte, current stage of Student's education
     */
    public byte getCurrentStageOfEducation() {
        return currentStageOfEducation;
    }

    /**
     * Method setCurrentStageOfEducation - transfer to the next course level
     * @param currentStageOfEducation byte, next level
     */
    void setCurrentStageOfEducation(byte currentStageOfEducation) {
        this.currentStageOfEducation = currentStageOfEducation;
    }

    /**
     * @return int average Student's score
     */
    public int getAverageScore() {
        return averageScore;
    }

    /**
     * Method setScore - simplified (incorrect) calculation of averageScore
     * !!! need to be refactored with correct algorhythm and int[] array of scores to calculate averageScore
     * @param score - int score for test or homework from teacher
     */
    public void setScore(int score) {
        this.averageScore = (this.averageScore + score) * 2 / 3;
    }

    /**
     * Method doHomework - Student will fill in String homework[]
     */
    void doHomework() {
        String knowledge = this.knowledge.pop();
        for (int index = 0; index < this.homework.length; index++) {
            this.homework[index] = "now i know that " + knowledge;
        }
    }

    /**
     * Method prepareForExam - Student prepares for exam
     * The student needs to work harder with every stage of education to be ready for the exam
     */
    void prepareForExam() {
        this.isReadyForExam = (averageScore > currentStageOfEducation);
    }
}
