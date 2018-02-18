package ru.job4j.inheritance;

import java.util.Stack;

/**
 * Engineer
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Engineer extends Professional {

    private int averageKPI;

    /**
     * Instance constructor of the Engineer class
     *
     * @param name String
     * @param age int
     * @param hasJob booleam
     * @param specialization String
     * @param experienceInYears int
     * @param qualificationImprovement String[]
     * @param averageKPI int
     */
    public Engineer(
            String name,
            int age,
            boolean hasJob,
            String specialization,
            int experienceInYears,
            Stack<String> qualificationImprovement,
            int averageKPI
    ) {
        super(name, age, hasJob, specialization, experienceInYears, qualificationImprovement);
        this.averageKPI = averageKPI;
    }

    /**
     * @return int average KPI of the Engineer
     */
    public int getAverageKPI() {
        return averageKPI;
    }

    /**
     * Method setAverageKPI - simplified calculation of the Engineer's---- KPI
     */
    void setAverageKPI() {
        this.averageKPI = this.getExperienceInYears() * this.getExperienceInYears();
    }

    /**
     * Method design
     * @param projectName String name of new Project
     * @param projectDescription String short project description
     * @return new Project
     */
    public Project design(String projectName, String projectDescription) {
        return new Project(projectName, projectDescription, this);
    }

    /**
     * Method makeChanges - emulates engineer's work on project
     * @param project Project to work on
     */
    public void makeChanges(Project project) {
        project.setCost(project.getCost() + this.getExperienceInYears() * this.getAverageKPI());
        project.setPercentageOfCompleted((byte) (project.getPercentageOfCompleted() + 1));
        project.setChangeLog(
                this.getSpecialization()
                        + " "
                        + this.getName()
                        + " worked on project"
        );
    }

    /**
     * Method technicalSupport - emulates engineer's support
     * @param project Project to work with
     */
    public void technicalSupport(Project project) {
        if (!project.isComplete()) {
            project.setCost(this.getExperienceInYears() + this.averageKPI);
        } else {
            project.setChangeLog(this.getName()
                    + " "
                    + this.getSpecialization()
                    + " supported project"
            );
        }
    }

    /**
     * Method jointProject - Engineer ask to help another Engineer, to work on Project
     * @param engineer Engineer who is asked to help
     * @param project Project to work together
     */
    public void jointProject(Engineer engineer, Project project) {
        if (project.isComplete()) {
            engineer.technicalSupport(project);
        } else {
            engineer.makeChanges(project);
        }
    }

    /**
     * @return Stack<String> qualification improvements of the Engineer
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
