package com.noscompany.snake.game.online.local.game.message;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Score;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import snake.game.gameplay.GameplayCreator;

import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class MessageController extends AbstractController {
    @FXML
    private Label messageLabel;
    private Map<PlayerNumber, String> nicknames;

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        nicknames = defaultNickNames();
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }

    public void printSecondsLeftToStart(int secondsLeft) {
        Platform.runLater(() -> messageLabel.setText("GAME STARTS IN " + secondsLeft + " SECONDS"));
    }

    public void printGameStarted() {
        Platform.runLater(() -> messageLabel.setText("GAME STARTED"));
    }

    public void printGameCanceled() {
        Platform.runLater(() -> messageLabel.setText("GAME CANCELED"));
    }

    public void printGamePaused() {
        Platform.runLater(() -> messageLabel.setText("GAME PAUSED"));
    }

    public void printGameResumed() {
        Platform.runLater(() -> messageLabel.setText("GAME RESUMED"));
    }

    public void print(String msg) {
        Platform.runLater(() -> messageLabel.setText(msg));
    }

    public void printGameFinished(GameFinished event) {
        Platform.runLater(() -> {
            var score = event.getScore();
            if (numberOfSnakes(score) == 1)
                printSinglePlayerFinish(score);
            else {
                printMultiplayerFinish(score);
            }
        });
    }

    public void print(FailedToStartGame.Reason reason) {
        Platform.runLater(() -> messageLabel.setText(reason.toString()));
    }

    public void print(FailedToTakeASeat.Reason reason) {
        Platform.runLater(() -> messageLabel.setText(reason.toString()));
    }

    public void print(FailedToChangeGameOptions.Reason reason) {
        Platform.runLater(() -> messageLabel.setText(reason.toString()));
    }

    public void clear() {
        Platform.runLater(() -> messageLabel.setText(""));
    }

    public void printPressStartWhenReady() {
        Platform.runLater(() -> messageLabel.setText("PRESS START WHEN READY"));
    }

    private int numberOfSnakes(Score score) {
        return (int) score
                .getEntries()
                .stream()
                .map(Score.Entry::getSnakes)
                .flatMap(List::stream)
                .count();
    }

    private void printSinglePlayerFinish(Score score) {
        Platform.runLater(() ->
                findFirstPlace(score).ifPresent(finishScore -> messageLabel.setText("YOU FINISHED WITH SCORE " + finishScore)));
    }

    private Optional<Integer> findFirstPlace(Score score) {
        return score
                .getEntries()
                .stream()
                .filter(entry -> entry.getPlace() == 1)
                .map(Score.Entry::getScore)
                .findFirst();
    }

    private void printMultiplayerFinish(Score score) {
        List<Score.Snake> winners = getWinners(score);
        if (winners.size() > 1) {
            printDrawBetween(winners);
        } else if (winners.size() == 1)
            printWinner(winners.get(0));
    }

    public void print(GameplayCreator.Error error) {
        messageLabel.setText(error.toString().replace("_", " "));
    }

    public void snakeNameUpdated(PlayerNumber playerNumber, String newName) {
        nicknames.put(playerNumber, newName);
    }

    private List<Score.Snake> getWinners(Score score) {
        return score
                .getEntries()
                .stream()
                .filter(entry -> entry.getPlace() == 1)
                .map(Score.Entry::getSnakes)
                .flatMap(List::stream)
                .collect(toList());
    }

    private void printWinner(Score.Snake snake) {
        Platform.runLater(() -> {
            String name = getNameOf(snake.getPlayerNumber());
            messageLabel.setText("WINNER: " + name);
        });
    }

    private String getNameOf(PlayerNumber playerNumber) {
        return nicknames.get(playerNumber);
    }

    private void printDrawBetween(List<Score.Snake> winners) {
        Platform.runLater(() -> messageLabel.setText("GAME FINISHED WITH DRAW"));
    }

    private Map<PlayerNumber, String> defaultNickNames() {
        return new HashMap<>(Map.of(
                PlayerNumber._1, "Snake 1",
                PlayerNumber._2, "Snake 2",
                PlayerNumber._3, "Snake 3",
                PlayerNumber._4, "Snake 4"));
    }
}