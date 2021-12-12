package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import snake.game.core.dto.Score;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class Row {
    private final Cell[] cells;

    public static Row createFor(Score.Entry entry) {
        Row row = Row.ofSize(entry.getSnakes().size() + 2);
        row.update(entry);
        return row;
    }

    public static Row ofSize(int size) {
        return new Row(
                IntStream.range(0, size)
                        .mapToObj(i -> Cell.empty())
                        .toArray(Cell[]::new));
    }

    public static Row of(String... values) {
        return new Row(
                Stream.of(values)
                        .map(Cell::create)
                        .toArray(Cell[]::new));
    }

    void clear() {
        for (Cell cell : cells)
            cell.clear();
    }

    void update(Score.Entry scoreEntry) {
        clear();
        print(scoreEntry.getPlace(), scoreEntry.getScore(), scoreEntry.getSnakes());
    }

    private void print(int place, int score, List<Score.Snake> snakes) {
        cells[0].set(place);
        cells[1].set(score);
        print(snakes);
    }

    private void print(List<Score.Snake> snakes) {
        int cell = 2;
        for (var snake : snakes) {
            print(cell, snake);
            cell++;
        }
    }

    private void print(int cellNumber, Score.Snake snake) {
        if (cellNumber < cells.length)
            cells[cellNumber].set(snake);
    }
}