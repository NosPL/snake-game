package com.noscompany.snakejavafxclient.components.commons.scoreboard;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.gui.commons.SnakesColors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class ScoreboardController extends AbstractController {
    private static final String SPACE = "   ";
    @FXML
    private VBox vBox;
    private Scoreboard scoreBoard;
    private Score currentScore;
    private Map<PlayerNumber, String> nickNames;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        nickNames = defaultNickNames();
        currentScore = new Score(new LinkedList<>());
        scoreBoard = ScoreboardCreator.create();
        vBox.getChildren().add(scoreBoard);
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(GameStartCountdown.class, (GameStartCountdown e) -> print(e.getScore()))
                .toMessage(GameStarted.class, (GameStarted e) -> print(e.getScore()))
                .toMessage(SnakesMoved.class, (SnakesMoved e) -> print(e.getScore()))
                .toMessage(GameFinished.class, (GameFinished e) -> print(e.getScore()))
                .subscriberName("scoreboard-gui");
    }

    public void update(GameState gameState) {
        print(gameState.getScore());
    }

    public void clear() {
        Platform.runLater(() -> scoreBoard.clear());
    }

    public void print(Score score) {
        Platform.runLater(() -> {
            this.currentScore = score;
            Collection<Scoreboard.Entry> scoreEntries = toScoreboardEntries(score);
            scoreBoard.update(scoreEntries);
        });
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
        String name = nickNames.get(snake.getPlayerNumber());
        Color color = SnakesColors.get(snake.getPlayerNumber());
        return new Scoreboard.Player(
                name,
                color,
                snake.isAlive());
    }

    public void updateSnakeName(String newName, PlayerNumber playerNumber) {
        nickNames.put(playerNumber, newName + SPACE);
        print(currentScore);
    }

    private Map<PlayerNumber, String> defaultNickNames() {
        return new HashMap<>(Map.of(
                PlayerNumber._1, "Snake 1" + SPACE,
                PlayerNumber._2, "Snake 2" + SPACE,
                PlayerNumber._3, "Snake 3" + SPACE,
                PlayerNumber._4, "Snake 4" + SPACE));
    }
}