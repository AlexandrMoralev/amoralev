package ru.job4j.inheritance;

import java.util.Stack;

/**
 * Teacher
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Teacher extends Professional {

    private int averageStudentsScore;

    /**
     * Instance constructor of the Teacher class
     *
     * @param name String
     * @param age int
     * @param hasJob boolean
     * @param specialization String
     * @param experienceInYears int
     * @param qualificationImprovement String[]
     * @param averageStudentsScore int
     */
    public Teacher(
            String name,
            int age,
            boolean hasJob,
            String specialization,
            int experienceInYears,
            Stack<String> qualificationImprovement,
            int averageStudentsScore
    ) {
        super(name, age, hasJob, specialization, experienceInYears, qualificationImprovement);
        this.averageStudentsScore = averageStudentsScore;
    }

    /**
     * @param student Student instance to teach
     */
    public void teachStudent(Student student, String knowledge) {
        student.setKnowledge(knowledge);
    }

    /**
     * Method checkHomework
     * @param student Student instance to check homework
     * @return int score for the homework
     */
    public int checkHomework(Student student) {
        int exercisesOfHW = 0;
        int counter = 1;

        // !! simplified (incorrect) calculation of averageStudentsScore
        this.averageStudentsScore = (this.averageStudentsScore + student.getAverageScore()) * 2 / 3;

        for (String exercise : student.getHomework()) {
            exercisesOfHW += (int) (Math.random() * 5 + 1);
            counter++;
        }

        return exercisesOfHW / counter;
    }

    /**
     * Method testStudent - quizzing the Student instance,
     * set Student's score and calculates Teacher's averageStudentsScore
     * @param student Student
     * @return int score for the exam
     */
    public int testStudent(Student student) {
        int result;

        result = (student.isReadyForExam()) ? (int) (3 + Math.random() * (student.getAverageScore())) : (int) (1 + Math.random());

        student.setScore(result);

        if (result > 2) {
            student.setCurrentStageOfEducation((byte) (student.getCurrentStageOfEducation() + 1));
        }

        this.averageStudentsScore += result / 2;

        return result;
    }

    /**
     * @return int average Student's score of the Teacher
     */
    public int getAverageStudentsScore() {
        return averageStudentsScore;
    }

    /**
     * Method getQualificationImprovement
     * @return Stack<String> of qualification improvements of the Teacher
     */
    @Override
    public Stack<String> getQualificationImprovement() {
        return super.getQualificationImprovement();
    }

    /**
     * Method improveQualification - adding new qualification to Teacher's String[] qualificationImprovement
     * @param skill String, new qualification improvement
     */
    @Override
    public void improveQualification(String skill) {
        super.improveQualification(skill);
    }
}
