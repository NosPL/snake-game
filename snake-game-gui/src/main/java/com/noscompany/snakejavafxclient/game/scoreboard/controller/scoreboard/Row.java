package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class Row {
    private final Cell[] cells;

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

    public void update(int place, int score, Collection<Scoreboard.Player> players) {
        clear();
        printPlace(place);
        printScore(score);
        print(players);
    }

    private void printScore(int score) {
        cells[1].setText(String.valueOf(score));
    }

    private void printPlace(int place) {
        cells[0].setText(String.valueOf(place));
    }

    private void print(Collection<Scoreboard.Player> players) {
        int cell = 2;
        for (var player : players) {
            print(cell, player);
            cell++;
        }
    }

    private void print(int cellNumber, Scoreboard.Player player) {
        if (cellNumber < cells.length) {
            Cell cell = cells[cellNumber];
            cell.setText(player.getName());
            cell.setTextFill(player.getColor());
            cell.setStrikethrough(player.isDead());
        }
    }
}