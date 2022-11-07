package com.noscompany.snakejavafxclient.components.commons.scoreboard;

import com.noscompany.snake.game.online.contract.messages.lobby.LobbyState;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import com.noscompany.snakejavafxclient.utils.SnakesColors;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import com.noscompany.snake.game.online.contract.messages.game.dto.Score;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

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

    public void update(LobbyState lobbyState) {
        lobbyState
                .getSeats()
                .forEach(seat -> updateSnakeName(seat.getUserName().getOrElse(""), seat.getPlayerNumber()));
        print(lobbyState.getGameState().getScore());
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