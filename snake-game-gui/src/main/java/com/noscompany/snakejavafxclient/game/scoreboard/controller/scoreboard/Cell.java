package com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard;

import com.noscompany.snakejavafxclient.game.SnakesColors;
import com.sun.javafx.scene.control.LabeledText;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import snake.game.core.dto.Point;
import snake.game.core.dto.Score;

import static snake.game.core.dto.Point.point;

class Cell extends Label {
    private static final String FONT_FAMILY = "Serif";
    private static final FontWeight FONT_WEIGHT = FontWeight.BOLD;
    private static final int FONT_SIZE = 11;
    private static final String SNAKE_PREFIX = "Snake ";
    private static final String SNAKE_SUFFIX = " ";

    private Cell(String text) {
        super(text);
    }

    static Cell empty() {
        var label = new Cell("");
        label.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        return label;
    }

    public static Cell create(String text) {
        var label = new Cell(text);
        label.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        return label;
    }

    void set(int value) {
        super.setText(String.valueOf(value));
    }

    void set(Score.Snake snake) {
        super.setText(nameOf(snake));
        super.setTextFill(colorOf(snake));
        setStrikethrough(!snake.isAlive());
    }

    private String nameOf(Score.Snake snake) {
        return SNAKE_PREFIX + snake.getSnakeNumber().toInt() + SNAKE_SUFFIX;
    }

    private Paint colorOf(Score.Snake snake) {
        return SnakesColors.get(snake.getSnakeNumber());
    }

    void clear() {
        super.setText("");
    }

    void setStrikethrough(boolean strikethrough) {
        LabeledText text = (LabeledText) super.lookup(".text");
        if (text != null)
            text.setStrikethrough(strikethrough);
    }

    Point getPoint() {
        var rowIndex = GridPane.getRowIndex(this);
        var columnIndex = GridPane.getColumnIndex(this);
        return point(columnIndex, rowIndex);
    }
}