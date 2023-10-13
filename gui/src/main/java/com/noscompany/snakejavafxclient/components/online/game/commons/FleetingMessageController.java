package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToPauseGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToResumeGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToStartGame;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import io.vavr.control.Try;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.*;

public class FleetingMessageController extends AbstractController {
    @FXML
    private Label messageLabel;
    private Duration messageDuration;
    private ExecutorService executorService;
    private Future<?> currentTask;

    public void print(Enum<?> enumMessage) {
        var stringMsg = asString(enumMessage);
        currentTask.cancel(true);
        currentTask = executorService.submit(() -> printMessage(stringMsg));
    }

    private void printMessage(String message) {
        Platform.runLater(() -> messageLabel.setText(message));
        Try.run(() -> Thread.sleep(messageDuration.toMillis()));
        Platform.runLater(() -> messageLabel.setText(""));
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    private String asString(Enum<?> anEnum) {
        return anEnum.toString().replace("_", " ");
    }

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
        messageDuration = Duration.ofSeconds(5);
        executorService = Executors.newSingleThreadExecutor();
        currentTask = completedFuture(new Object());
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription()
                .toMessage(FailedToTakeASeat.class, this::failedToTakeASeat)
                .toMessage(FailedToFreeUpSeat.class, this::failedToFreeUpASeat)
                .toMessage(FailedToChangeGameOptions.class, this::failedToChangeGameOptions)
                .toMessage(FailedToStartGame.class, this::failedToStartGame)
                .toMessage(FailedToCancelGame.class, this::failedCancelGame)
                .toMessage(FailedToPauseGame.class, this::failedToPauseGame)
                .toMessage(FailedToResumeGame.class, this::failedToResumeGame)
                .subscriberName("fleeting-message-gui");
    }

    private void failedToTakeASeat(FailedToTakeASeat event) {
        print(event.getReason());
    }

    private void failedToFreeUpASeat(FailedToFreeUpSeat event) {
        print(event.getReason());
    }

    private void failedToChangeGameOptions(FailedToChangeGameOptions event) {
        print(event.getReason());
    }

    private void failedToStartGame(FailedToStartGame event) {
        print(event.getReason());
    }

    private void failedCancelGame(FailedToCancelGame event) {
        print(event.getReason());
    }

    private void failedToPauseGame(FailedToPauseGame event) {
        print(event.getReason());
    }

    private void failedToResumeGame(FailedToResumeGame event) {
        print(event.getReason());
    }
}