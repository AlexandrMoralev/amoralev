package ru.job4j.inheritance;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Stack;

/**
 * Project
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Project {

    private String name;
    private String description;
    private String specialization;
    private Professional author;
    private String customer;
    private Calendar creationDate;
    private Calendar deadline;
    private byte percentageOfCompleted; // minimum = 0, maximum = 100
    private boolean isComplete;
    private Stack<String> changeLog;
    private long cost;

    /**
     * Instance constructor of the Project class
     *
     * @param name String
     * @param description String
     * @param author Professional instance
     */
    public Project(String name, String description, Professional author) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.specialization = author.getSpecialization();
        this.creationDate = GregorianCalendar.getInstance();
        this.changeLog = new Stack<String>();
        boolean isComplete = false;
    }

    /**
     * Instance constructor of the Project class
     *
     * @param name String
     * @param description String
     * @param author Professional instance
     * @param customer String
     * @param percentageOfCompleted byte, minimum = 0, maximum = 100
     * @param cost long
     */
    public Project(
            String name,
            String description,
            Professional author,
            String customer,
            byte percentageOfCompleted,
            long cost
    ) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.specialization = author.getSpecialization();
        this.customer = customer;
        this.creationDate = GregorianCalendar.getInstance();
        this.percentageOfCompleted = percentageOfCompleted;
        this.cost = cost;
        this.changeLog = new Stack<String>();
        boolean isComplete = false;
    }

    /**
     * @return String name of the Project
     */
    public String getName() {
        return name;
    }

    /**
     * @return String short description of the Project
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description String short description of the Project
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Professional Project's author
     */
    public Professional getAuthor() {
        return author;
    }

    /**
     * @return String Project's customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Method setCustomer - changes customer of the Project
     *
     * @param customer String, new customer
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * @return Project specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Method getCreationDate - Project's creation date
     *
     * @return Calendar creationDate
     */
    public Calendar getCreationDate() {
        return creationDate;
    }

    /**
     * Method getDeadline - Project's deadline date
     *
     * @return Calendar deadline date
     */
    public Calendar getDeadline() {
        return deadline;
    }

    /**
     * Method setDeadline - changes Project'a deadline date
     *
     * @param deadline Calendar date
     */
    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    /**
     * @return byte, the percentage of Project's completion
     * minimum = 0, maximum = 100
     */
    public byte getPercentageOfCompleted() {
        return percentageOfCompleted;
    }

    /**
     * Method setPercentageOfCompleted - changes Project'a percentage of completion
     * @param percentageOfCompleted
     */
    void setPercentageOfCompleted(byte percentageOfCompleted) {
        this.percentageOfCompleted = percentageOfCompleted;
        if (percentageOfCompleted == 100) {
            this.setComplete();
        }
    }

    /**
     * @return boolean, the Project is complete?
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Method setComplete - completion of the Project
     */
    void setComplete() {
        isComplete = true;
        this.percentageOfCompleted = 100;
    }

    /**
     * @return Stack<String> changeLog of the Project
     */
    public Stack<String> getChangeLog() {
        return changeLog;
    }

    /**
     * @param change String, short description of current change
     */
    void setChangeLog(String change) {
        this.changeLog.push(change);
    }

    /**
     * @return long cost of the Project
     */
    public long getCost() {
        return cost;
    }

    /**
     * @param cost long, new cost of the Project
     */
    void setCost(long cost) {
        this.cost = cost;
    }
}

