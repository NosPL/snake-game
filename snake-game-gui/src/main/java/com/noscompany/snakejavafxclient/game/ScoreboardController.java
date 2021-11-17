package com.noscompany.snakejavafxclient.game;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ScoreboardController extends AbstractController {
    @FXML
    private Label scoresLabel;

    public void reset() {
        scoresLabel.setText("");
    }

    public void print(Score score) {
        final String string = toString(score);
        scoresLabel.setText(string);
    }

    private String toString(Score score) {
        return score
                .toMap()
                .entrySet()
                .stream()
                .map(this::toScore)
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");

    }

    private String toScore(Map.Entry<SnakeNumber, Integer> entry) {
        return "#" + entry.getKey().toInt() + ": " + entry.getValue() + " points";
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {

    }
}
