package ru.job4j.inheritance;

import java.util.Stack;

/**
 * Professional
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Professional {

    private String name;
    private int age;
    private long telNumber;
    private boolean hasJob;
    private int experienceInYears;
    private String specialization;
    private Stack<String> qualificationImprovement;

    /**
     * Instance constructor of the Professional class
     * @param name String
     * @param age int
     * @param specialization String
     * @param hasJob boolean
     */
    public Professional(
            String name,
            int age,
            String specialization,
            boolean hasJob
    ) {
        this.name = name;
        this.age = age;
        this.specialization = specialization;
        this.hasJob = hasJob;
        this.telNumber = 0;
        this.experienceInYears = 0;
        this.qualificationImprovement = new Stack<String>();
    }

    /**
     * Overloaded instance constructor of the Professional class
     * @param name String
     * @param age int
     * @param specialization String
     * @param experienceInYears int
     * @param qualificationImprovement String[]
     */
    public Professional(
            String name,
            int age,
            String specialization,
            int experienceInYears,
            Stack<String> qualificationImprovement
    ) {
        this.name = name;
        this.age = age;
        this.specialization = specialization;
        this.experienceInYears = experienceInYears;
        this.qualificationImprovement = qualificationImprovement;
    }

    /**
     * Overloaded instance constructor of the Professional class
     * @param name String
     * @param age int
     * @param telNumber long, without "+" and braces
     * @param hasJob String
     * @param specialization String
     * @param experienceInYears int
     * @param qualificationImprovement String[]
     */
    public Professional(
            String name,
            int age,
            long telNumber,
            boolean hasJob,
            String specialization,
            int experienceInYears,
            Stack<String> qualificationImprovement
    ) {
        this.name = name;
        this.age = age;
        this.telNumber = telNumber;
        this.hasJob = hasJob;
        this.specialization = specialization;
        this.experienceInYears = experienceInYears;
        this.qualificationImprovement = qualificationImprovement;
    }

    /**
     * @return String name - name of the Professional
     */
    public String getName() {
        return name;
    }

    /**
     * @return int age - age of the Professional
     */
    public int getAge() {
        return age;
    }

    /**
     * @return long telMumber - telephone number of the Professional
     */
    public long getTelNumber() {
        return telNumber;
    }

    /**
     * @param telNumber - long telephone number of the Professional
     */
    public void setTelNumber(long telNumber) {
        this.telNumber = telNumber;
    }

    /**
     * @return boolean isHasJob - if the Professional has work
     */
    public boolean isHasJob() {
        return hasJob;
    }

    /**
     * @return int experience of the Professional in years
     */
    public int getExperienceInYears() {
        return experienceInYears;
    }

    /**
     * @return String specialization of the Professional
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Method getQualificationImprovement -
     * @return Stack<String> of Professional skills
     */
    public Stack<String> getQualificationImprovement() {
        return this.qualificationImprovement;
    }

    /**
     * Method improveQualification - Professional has better skills now
     * @param skill - new qualification improvement
     */
    public void improveQualification(String skill){
        this.qualificationImprovement.push(skill);
    }

    /**
     * Method getNewJob - Professional has job now
     */
    public void getNewJob(){
        this.hasJob = true;
    }

    /**
     * Method quitJob - Professional is fired now
     */
    public void quitJob() {
        this.hasJob = false;
    }

}
