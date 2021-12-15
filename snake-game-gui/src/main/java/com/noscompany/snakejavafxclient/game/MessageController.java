package com.noscompany.snakejavafxclient.game;

import com.noscompany.snake.game.commons.messages.events.lobby.FailedToChangeGameOptions;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToStartGame;
import com.noscompany.snake.game.commons.messages.events.lobby.FailedToTakeASeat;
import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import snake.game.core.SnakeGameConfiguration;

import java.net.URL;
import java.util.ResourceBundle;

import static snake.game.core.SnakeGameConfiguration.Error.PLAYERS_SET_IS_EMPTY;

public class MessageController extends AbstractController {
    @FXML
    private Label messageLabel;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
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
}