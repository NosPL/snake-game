package com.noscompany.snake.game.online.gameplay.gui.scoreboard;

import io.vavr.control.Option;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Scoreboard extends GridPane {
    private final Map<Integer, Row> rows = new HashMap<>();

    public void clear() {
        rows.values().forEach(Row::clear);
    }

    public void update(Collection<Entry> scoreEntries) {
        clear();
        scoreEntries.forEach(this::print);
    }

    private void print(Entry entry) {
        updateRow(entry.getPlace(), entry.getScore(), entry.getPlayers());
    }

    void setHeaders(String... headers) {
        Row headersRow = Row.of(headers);
        super.addRow(0, headersRow.getCells());
    }

    void addRowOfSize(int rowIndex, int rowSize) {
        Row row = Row.ofSize(rowSize);
        add(rowIndex, row);
    }

    private void updateRow(int place, int score, Collection<Player> players) {
        findRow(place)
                .peek(row -> row.update(place, score, players));
    }

    private void add(int rowIndex, Row row) {
        super.addRow(rowIndex, row.getCells());
        rows.put(rowIndex, row);
    }

    private Option<Row> findRow(int place) {
        return Option.of(rows.get(place));
    }

    @Value
    public static class Entry {
        int place;
        int score;
        Collection<Player> players;
    }

    @Value
    public static class Player {
        String name;
        Color color;
        boolean isAlive;

        public boolean isDead() {
            return !isAlive;
        }
    }
}