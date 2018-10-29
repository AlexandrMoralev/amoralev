package ru.job4j.exam.tictactoe;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Logic3T {
    private final Figure3T[][] table;

    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    public boolean fillBy(Predicate<Figure3T> predicate,
                          int startX,
                          int startY,
                          int deltaX,
                          int deltaY
    ) {
        boolean result = true;

        for (int i = 0; i < this.table.length; i++) {
            Figure3T cell = this.table[startX][startY];
            startX += deltaX;
            startY += deltaY;
            if (!predicate.test(cell)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public boolean isWinnerX() {
        return this.fillBy(Figure3T::hasMarkX, 0, 0, 1, 0)
                || this.fillBy(Figure3T::hasMarkX, 0, 0, 0, 1)
                || this.fillBy(Figure3T::hasMarkX, 0, 0, 1, 1)
                || this.fillBy(Figure3T::hasMarkX, this.table.length - 1, 0, -1, 1)
                || this.fillBy(Figure3T::hasMarkX, this.table.length - 1, this.table.length - 1, -1, 0)
                || this.fillBy(Figure3T::hasMarkX, this.table.length - 1, this.table.length - 1, 0, -1)
                || this.fillBy(Figure3T::hasMarkX, (this.table.length - 1) / 2, 0, 0, 1)
                || this.fillBy(Figure3T::hasMarkX, 0, (this.table.length - 1) / 2, 1, 0);
    }

    public boolean isWinnerO() {
        return this.fillBy(Figure3T::hasMarkO, 0, 0, 1, 0)
                || this.fillBy(Figure3T::hasMarkO, 0, 0, 0, 1)
                || this.fillBy(Figure3T::hasMarkO, 0, 0, 1, 1)
                || this.fillBy(Figure3T::hasMarkO, this.table.length - 1, 0, -1, 1)
                || this.fillBy(Figure3T::hasMarkO, this.table.length - 1, this.table.length - 1, -1, 0)
                || this.fillBy(Figure3T::hasMarkO, this.table.length - 1, this.table.length - 1, 0, -1)
                || this.fillBy(Figure3T::hasMarkO, (this.table.length - 1) / 2, 0, 0, 1)
                || this.fillBy(Figure3T::hasMarkO, 0, (this.table.length - 1) / 2, 1, 0);
    }

    public boolean hasGap() {
        Predicate<Figure3T> isEmpty = figure3T -> !figure3T.hasMarkO() && !figure3T.hasMarkX();
        return Stream.of(this.table).flatMap(figure3T -> Arrays.stream(figure3T)).anyMatch(isEmpty);
    }
}