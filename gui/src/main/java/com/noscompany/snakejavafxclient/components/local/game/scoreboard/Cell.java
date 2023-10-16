package com.noscompany.snakejavafxclient.components.local.game.scoreboard;

import com.sun.javafx.scene.control.LabeledText;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class Cell extends Label {
    private static final String FONT_FAMILY = "Serif";
    private static final FontWeight FONT_WEIGHT = FontWeight.BOLD;
    private static final int FONT_SIZE = 11;

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

    void clear() {
        super.setText("");
    }

    void setStrikethrough(boolean strikethrough) {
        LabeledText text = (LabeledText) super.lookup(".text");
        if (text != null)
            text.setStrikethrough(strikethrough);
    }
}