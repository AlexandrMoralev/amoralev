package ru.job4j.exam.tictactoe;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Logic3T {
    private final Figure3T[][] table;

    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    public boolean isWinnerX() {
        boolean result = false;

        boolean isWinCondition;
        boolean isAnotherWinCondition;

        for (int i = 0; i < this.table.length; i++) {
            isWinCondition = true;
            isAnotherWinCondition = true;
            for (int j = 0; j < this.table[i].length; j++) {
                isWinCondition &= table[i][j].hasMarkX();
                isAnotherWinCondition &= table[j][i].hasMarkX();
            }
            if (isWinCondition || isAnotherWinCondition) {
                result = true;
                break;
            }
        }
        if (!result) {
            isWinCondition = true;
            isAnotherWinCondition = true;
            for (int i = 0; i < this.table.length; i++) {
                isWinCondition &= table[i][i].hasMarkX();
                isAnotherWinCondition &= table[table.length - 1 - i][i].hasMarkX();
            }
            result = isWinCondition || isAnotherWinCondition;
        }
        return result;
    }

    public boolean isWinnerO() {
        boolean result = false;

        boolean isWinCondition;
        boolean isAnotherWinCondition;

        for (int i = 0; i < this.table.length; i++) {
            isWinCondition = true;
            isAnotherWinCondition = true;
            for (int j = 0; j < this.table[i].length; j++) {
                isWinCondition &= table[i][j].hasMarkO();
                isAnotherWinCondition &= table[j][i].hasMarkO();
            }
            if (isWinCondition || isAnotherWinCondition) {
                result = true;
                break;
            }
        }
        if (!result) {
            isWinCondition = true;
            isAnotherWinCondition = true;
            for (int i = 0; i < this.table.length; i++) {
                isWinCondition &= table[i][i].hasMarkO();
                isAnotherWinCondition &= table[table.length - 1 - i][i].hasMarkO();
            }
            result = isWinCondition || isAnotherWinCondition;
        }
        return result;
    }

    public boolean hasGap() {
        Predicate<Figure3T> isEmpty = figure3T -> !figure3T.hasMarkO() && !figure3T.hasMarkX();
        return Stream.of(this.table).flatMap(figure3T -> Arrays.stream(figure3T)).anyMatch(isEmpty);
    }
}