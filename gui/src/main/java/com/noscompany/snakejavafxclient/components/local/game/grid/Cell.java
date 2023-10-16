package com.noscompany.snakejavafxclient.components.local.game.grid;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;

import static com.noscompany.snakejavafxclient.components.local.game.grid.GameGridSigns.EMPTY_CELL;

class Cell extends Label {
    private static final String FONT_FAMILY = "Serif";
    private static final FontWeight FONT_WEIGHT = FontWeight.BOLD;

    private Cell(String text) {
        super(text);
    }

    static Cell ofSize(int cellSize) {
        Cell cell = new Cell(EMPTY_CELL);
        cell.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, cellSize));
        return cell;
    }

    void clear() {
        super.setText(EMPTY_CELL);
    }

    Position getPosition() {
        var rowIndex = GridPane.getRowIndex(this);
        var columnIndex = GridPane.getColumnIndex(this);
        return Position.position(columnIndex, rowIndex);
    }

    void setBackground(Color color) {
        super.setBackground(new Background(new BackgroundFill(color, new CornerRadii(0), new Insets(0))));
    }
}