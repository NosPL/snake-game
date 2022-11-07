package com.noscompany.snakejavafxclient.components.commons.message;

import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.lobby.event.FailedToTakeASeat;
import com.noscompany.snakejavafxclient.utils.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.noscompany.snake.game.online.contract.messages.game.dto.Score;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

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
        if (numberOfSnakes(score) == 1)
            printSinglePlayerFinish(score);
        else {
            printMultiplayerFinish(score);
        }
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
        findFirstPlace(score)
                .ifPresent(finishScore -> messageLabel.setText("YOU FINISHED WITH SCORE " + finishScore));
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
        String name = getNameOf(snake.getPlayerNumber());
        messageLabel.setText("WINNER: " + name);
    }

    private String getNameOf(PlayerNumber playerNumber) {
        return nicknames.get(playerNumber);
    }

    private void printDrawBetween(List<Score.Snake> winners) {
        messageLabel.setText("GAME FINISHED WITH DRAW");
    }

    public void updateSnakeName(String newName, PlayerNumber playerNumber) {
        nicknames.put(playerNumber, newName);
    }

    private Map<PlayerNumber, String> defaultNickNames() {
        return new HashMap<>(Map.of(
                PlayerNumber._1, "Snake 1",
                PlayerNumber._2, "Snake 2",
                PlayerNumber._3, "Snake 3",
                PlayerNumber._4, "Snake 4"));
    }
}