package ru.job4j.exam.tictactoe;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class Logic3TTest {

    @Test
    public void whenHasXWinner() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(true)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerX(), is(true));
    }

    @Test
    public void whenHasXWinnerWithReversedDiagonal() {
        Figure3T[][] table = {
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerX(), is(true));
    }

    @Test
    public void whenHasXWinnerWithRow() {
        Figure3T[][] firstTable = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)}
        };
        Figure3T[][] secondTable = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T()}
        };
        Figure3T[][] thirdTable = {
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()}
        };
        Logic3T firstLogic = new Logic3T(firstTable);
        Logic3T secondLogic = new Logic3T(secondTable);
        Logic3T thirdLogic = new Logic3T(thirdTable);

        assertThat(firstLogic.isWinnerX()
                        & secondLogic.isWinnerX()
                        & thirdLogic.isWinnerX(),
                is(true)
        );
    }

    @Test
    public void whenHasXWinnerWithColumn() {
        Figure3T[][] firstTable = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(true), new Figure3T(), new Figure3T()}
        };
        Figure3T[][] secondTable = {
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()}
        };
        Figure3T[][] thirdTable = {
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T(true)},
                {new Figure3T(), new Figure3T(), new Figure3T(true)}
        };
        Logic3T firstLogic = new Logic3T(firstTable);
        Logic3T secondLogic = new Logic3T(secondTable);
        Logic3T thirdLogic = new Logic3T(thirdTable);

        assertThat(firstLogic.isWinnerX()
                        & secondLogic.isWinnerX()
                        & thirdLogic.isWinnerX(),
                is(true)
        );
    }

    @Test
    public void whenXNotWinner() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(), new Figure3T(true)},
                {new Figure3T(false), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(), new Figure3T(true), new Figure3T(false)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerX(), is(false));
    }

    @Test
    public void whenHasOWinner() {
        Figure3T[][] table = {
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(true), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T(true)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerO(), is(true));
    }

    @Test
    public void whenHasOWinnerWithReversedDiagonal() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(true), new Figure3T(false)},
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T(true)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerO(), is(true));
    }

    @Test
    public void whenHasOWinnerWithRow() {
        Figure3T[][] firstTable = {
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)},
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T(true)}
        };
        Figure3T[][] secondTable = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T()}
        };
        Figure3T[][] thirdTable = {
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)}
        };
        Logic3T firstLogic = new Logic3T(firstTable);
        Logic3T secondLogic = new Logic3T(secondTable);
        Logic3T thirdLogic = new Logic3T(thirdTable);

        assertThat(firstLogic.isWinnerO()
                        & secondLogic.isWinnerO()
                        & thirdLogic.isWinnerO(),
                is(true)
        );
    }

    @Test
    public void whenHasOWinnerWithColumn() {
        Figure3T[][] firstTable = {
                {new Figure3T(false), new Figure3T(true), new Figure3T()},
                {new Figure3T(false), new Figure3T(), new Figure3T()},
                {new Figure3T(false), new Figure3T(true), new Figure3T()}
        };
        Figure3T[][] secondTable = {
                {new Figure3T(), new Figure3T(false), new Figure3T()},
                {new Figure3T(), new Figure3T(false), new Figure3T(true)},
                {new Figure3T(), new Figure3T(false), new Figure3T()}
        };
        Figure3T[][] thirdTable = {
                {new Figure3T(), new Figure3T(), new Figure3T(false)},
                {new Figure3T(true), new Figure3T(), new Figure3T(false)},
                {new Figure3T(), new Figure3T(), new Figure3T(false)}
        };
        Logic3T firstLogic = new Logic3T(firstTable);
        Logic3T secondLogic = new Logic3T(secondTable);
        Logic3T thirdLogic = new Logic3T(thirdTable);

        assertThat(firstLogic.isWinnerO()
                        & secondLogic.isWinnerO()
                        & thirdLogic.isWinnerO(),
                is(true)
        );
    }

    @Test
    public void whenONotWinner() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(false), new Figure3T(true)},
                {new Figure3T(true), new Figure3T(true), new Figure3T()},
                {new Figure3T(false), new Figure3T(false), new Figure3T(true)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.isWinnerO(), is(false));
    }

    @Test
    public void whenHasGap() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(), new Figure3T()},
                {new Figure3T(), new Figure3T(true), new Figure3T()},
                {new Figure3T(), new Figure3T(), new Figure3T(true)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.hasGap(), is(true));
    }

    @Test
    public void whenHasNoGap() {
        Figure3T[][] table = {
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(true), new Figure3T(true), new Figure3T(true)},
                {new Figure3T(false), new Figure3T(false), new Figure3T(false)}
        };
        Logic3T logic = new Logic3T(table);
        assertThat(logic.hasGap(), is(false));
    }
}
