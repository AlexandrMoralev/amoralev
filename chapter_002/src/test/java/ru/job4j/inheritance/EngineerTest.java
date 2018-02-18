package ru.job4j.inheritance;

import org.junit.Test;
import java.util.Stack;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * EngineerTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class EngineerTest extends ProfessionalTest {
    /**
     * Test. Engineer creates new Project
     */
    @Test
    public void whenEngineerDesignProjectThenNewSpecializedProject() {
        Engineer engineer = new Engineer(
                "Denis Richie",
                54,
                true,
                "gold mining engineer",
                10,
                new Stack<String>(),
                100
        );
        String projectName = "Mining automation";
        String description = "robotic gold mining system for deep space";

        Project project = engineer.design(projectName, description);

        String expected = engineer.getSpecialization()
                + " "
                + engineer.getName()
                + " started research "
                + projectName
                + " - "
                + description
                + " 0 percent completed";

        String result = project.getSpecialization()
                + " "
                + project.getAuthor().getName()
                + " started research "
                + project.getName()
                + " - "
                + project.getDescription()
                + " "
                + project.getPercentageOfCompleted()
                + " percent completed";

        assertThat(result, is(expected));
    }

    /**
     * Test. Engineer works on Project and makes it closer to the final
     */
    @Test
    public void whenEngineerWorksOnProjectThenProjectBecomesCloserToTheFinal() {
        Engineer engineer = new Engineer(
                "Denis Richie",
                54,
                true,
                "gold mining engineer",
                10,
                new Stack<String>(),
                100
        );

        Project project = new Project(
                "Gold mining",
                "automation for gold mining",
                engineer
        );

        byte completedBefore = project.getPercentageOfCompleted();

        engineer.makeChanges(project);

        byte completedAfter = project.getPercentageOfCompleted();

        assertThat((completedAfter > completedBefore) & (completedAfter < 101), is(true));

    }

    /**
     * Test. Engineer supports completed Project
     */
    @Test
    public void whenEngineerSupportsProjectThenProjectBecomesMoreExpensive() {
        Engineer engineer = new Engineer(
                "Denis Richie",
                54,
                true,
                "gold mining engineer",
                10,
                new Stack<String>(),
                100
        );

        Project project = new Project(
                "Gold mining",
                "automation for gold mining",
                engineer
        );

        long costBefore = project.getCost();

        engineer.technicalSupport(project);

        long costAfter = project.getCost();

        assertThat((costAfter > costBefore), is(true));
    }

    /**
     * Test. Engineer asks assistant Engineer to help with Project.
     * Assistant Engineer makes changes to the Project
     */
    @Test
    public void whenEngineerJointUnfinishedProjectThenEngineerMakesChanges() {
        Engineer engineer = new Engineer(
                "Denis Richie",
                54,
                true,
                "gold mining engineer",
                10,
                new Stack<String>(),
                100
        );

        Engineer assistant = new Engineer(
                "Richie Blackmore",
                44,
                true,
                "mechanical vibration engineer",
                15,
                new Stack<String>(),
                70
        );

        Project project = new Project(
                "Gold mining",
                "automation for gold mining",
                engineer
        );

        engineer.jointProject(assistant, project);

        String result = project.getChangeLog().pop();

        String expected =
                assistant.getSpecialization()
                        + " "
                        + assistant.getName()
                        + " worked on project";

        assertThat(result, is(expected));
    }

    /**
     * Test. Engineer asks assistant Engineer to help with completed Project.
     * Assistant Engineer supports the Project
     */
    @Test
    public void whenEngineerJointToFinishedProjectThenEngineerSupportsProject() {
        Engineer engineer = new Engineer(
                "Denis Richie",
                54,
                true,
                "gold mining engineer",
                10,
                new Stack<String>(),
                100
        );

        Engineer assistant = new Engineer(
                "Richie Blackmore",
                44,
                true,
                "mechanical vibration engineer",
                15,
                new Stack<String>(),
                70
        );

        Project project = new Project(
                "Gold mining",
                "automation for gold mining",
                engineer
        );

        project.setComplete();

        engineer.jointProject(assistant, project);

        String result = project.getChangeLog().pop();

        String expected =
                assistant.getName()
                        + " "
                        + assistant.getSpecialization()
                        + " supported project";

        assertThat(result, is(expected));
    }
}
