package com.noscompany.snakejavafxclient.game.scoreboard.controller;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import com.noscompany.snakejavafxclient.game.SnakesColors;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard.Scoreboard;
import com.noscompany.snakejavafxclient.game.scoreboard.controller.scoreboard.ScoreboardCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ScoreboardController extends AbstractController {
    private static final String SPACE = "   ";
    @FXML
    private VBox vBox;
    private Scoreboard scoreBoard;
    private Score currentScore;
    private Map<SnakeNumber, String> nickNames;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        nickNames = defaultNickNames();
        currentScore = new Score(new LinkedList<>());
        scoreBoard = ScoreboardCreator.create();
        vBox.getChildren().add(scoreBoard);
    }

    public void clear() {
        scoreBoard.clear();
    }

    public void print(Score score) {
        this.currentScore = score;
        Collection<Scoreboard.Entry> scoreEntries = toScoreboardEntries(score);
        scoreBoard.update(scoreEntries);
    }

    private Collection<Scoreboard.Entry> toScoreboardEntries(Score score) {
        return score.getEntries()
                .stream()
                .map(this::toScoreboardEntry)
                .collect(toList());
    }

    private Scoreboard.Entry toScoreboardEntry(Score.Entry entry) {
        return new Scoreboard.Entry(entry.getPlace(), entry.getScore(), toPlayers(entry.getSnakes()));
    }

    private Collection<Scoreboard.Player> toPlayers(Collection<Score.Snake> snakes) {
        return snakes.stream()
                .map(this::toPlayer)
                .collect(toList());
    }

    private Scoreboard.Player toPlayer(Score.Snake snake) {
        String name = nickNames.get(snake.getSnakeNumber());
        Color color = SnakesColors.get(snake.getSnakeNumber());
        return new Scoreboard.Player(
                name,
                color,
                snake.isAlive());
    }

    public void snakeNameUpdated(SnakeNumber snakeNumber, String newName) {
        nickNames.put(snakeNumber, newName + SPACE);
        print(currentScore);
    }

    private Map<SnakeNumber, String> defaultNickNames() {
        return new HashMap<>(Map.of(
                SnakeNumber._1, "Snake 1" + SPACE,
                SnakeNumber._2, "Snake 2" + SPACE,
                SnakeNumber._3, "Snake 3" + SPACE,
                SnakeNumber._4, "Snake 4" + SPACE));
    }
}