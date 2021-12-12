package com.noscompany.snakejavafxclient.game.scoreboard.controller;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard.Scoreboard;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard.ScoreboardCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import snake.game.core.dto.Score;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreboardController extends AbstractController {
    @FXML
    private VBox vBox;
    private Scoreboard scoreBoard;

    public void clear() {
        scoreBoard.clear();
    }

    public void print(Score score) {
        scoreBoard.update(score);
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        scoreBoard = ScoreboardCreator.create();
        vBox.getChildren().add(scoreBoard);
    }
}