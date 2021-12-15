package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import lombok.NoArgsConstructor;
import snake.game.core.dto.Score;

import java.util.*;

@NoArgsConstructor
class ScoreboardImpl extends Scoreboard {
    private final Map<Integer, Row> rows = new HashMap<>();

    public void clear() {
        rows.values().forEach(Row::clear);
    }

    @Override
    public void update(Collection<Entry> scoreEntries) {
        clear();
        scoreEntries.forEach(this::print);
    }

    private void print(Scoreboard.Entry entry) {
        Row row = rows.get(entry.getPlace());
        if (row != null)
            row.update(entry.getPlace(), entry.getScore(), entry.getPlayers());
    }

    void setHeaders(String...headers) {
        super.addRow(0, Row.of(headers).getCells());
    }

    void addRowOfSize(int rowIndex, int rowSize) {
        Row row = Row.ofSize(rowSize);
        add(rowIndex, row);
    }

    private void add(int rowIndex, Row row) {
        super.addRow(rowIndex, row.getCells());
        rows.put(rowIndex, row);
    }
}