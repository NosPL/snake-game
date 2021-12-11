package com.noscompany.snakejavafxclient.game.grid.controller;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import snake.game.core.dto.Point;

import static com.noscompany.snakejavafxclient.game.grid.controller.GameGridSigns.EMPTY_CELL;

class Cell extends Label {
    private static final String FONT_FAMILY = "Serif";
    private static final FontWeight FONT_WEIGHT = FontWeight.BOLD;
    private static final int FONT_SIZE = 15;

    private Cell(String text) {
        super(text);
    }

    static Cell empty() {
        var label = new Cell(EMPTY_CELL);
        label.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        return label;
    }

    void clear() {
        super.setText(EMPTY_CELL);
    }

    Point getPoint() {
        var rowIndex = GridPane.getRowIndex(this);
        var columnIndex = GridPane.getColumnIndex(this);
        return Point.point(columnIndex, rowIndex);
    }
}