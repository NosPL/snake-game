package com.noscompany.snakejavafxclient.game;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToChangeGameOptions;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.Score;
import snake.game.core.dto.SnakeNumber;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static snake.game.core.SnakeGameConfiguration.Error.PLAYERS_SET_IS_EMPTY;

public class MessageController extends AbstractController {
    @FXML
    private Label messageLabel;
    private Map<SnakeNumber, String> nicknames;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        nicknames = defaultNickNames();
    }

    public void printSecondsLeftToStart(int secondsLeft) {
        messageLabel.setText("Game starts in " + secondsLeft + " seconds");
    }

    public void printGameCanceled() {
        messageLabel.setText("GAME CANCELED");
    }

    public void printGamePaused() {
        messageLabel.setText("GAME PAUSED");
    }

    public void printGameResumed() {
        messageLabel.setText("GAME RESUMED");
    }

    public void print(String msg) {
        messageLabel.setText(msg);
    }

    public void print(SnakeGameConfiguration.Error error) {
        if (error == PLAYERS_SET_IS_EMPTY)
            messageLabel.setText("SELECT AT LEAST 1 PLAYER");
    }

    public void printGameFinished() {
        messageLabel.setText("GAME FINISHED");
    }

    public void print(FailedToStartGame.Reason reason) {
        messageLabel.setText(reason.toString());
    }

    public void print(FailedToTakeASeat.Reason reason) {
        messageLabel.setText(reason.toString());
    }

    public void print(FailedToChangeGameOptions.Reason reason) {
        messageLabel.setText(reason.toString());
    }

    public void clear() {
        messageLabel.setText("");
    }

    public void printPressStartWhenReady() {
        messageLabel.setText("PRESS START WHEN READY");
    }

    public void printFinishScore(Score score) {
        if (score.getSnakes().size() == 1)
            printSinglePlayerFinish(score);
        else {
            printMultiplayerFinish(score);
        }
    }

    private void printSinglePlayerFinish(Score score) {
        int finishScore = score.getFirstPlace().getScore();
        messageLabel.setText("YOU FINISHED WITH SCORE " + finishScore);
    }

    private void printMultiplayerFinish(Score score) {
        List<Score.Snake> winners = score.getFirstPlace().getSnakes();
        if (winners.size() > 1) {
            printDrawBetween(winners);
        } else if (winners.size() == 1)
            printWinner(winners.get(0));
    }

    private void printWinner(Score.Snake snake) {
        String name = getNameOf(snake.getSnakeNumber());
        messageLabel.setText("WINNER: " + name);
    }

    private String getNameOf(SnakeNumber snakeNumber) {
        return nicknames.get(snakeNumber);
    }

    private void printDrawBetween(List<Score.Snake> winners) {
        messageLabel.setText("GAME FINISHED WITH DRAW");
    }


    public void updateSnakeName(String newName, SnakeNumber snakeNumber) {
        nicknames.put(snakeNumber, newName);
    }

    private Map<SnakeNumber, String> defaultNickNames() {
        return new HashMap<>(Map.of(
                SnakeNumber._1, "Snake 1",
                SnakeNumber._2, "Snake 2",
                SnakeNumber._3, "Snake 3",
                SnakeNumber._4, "Snake 4"));
    }
}