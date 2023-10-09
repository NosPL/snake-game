package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snakejavafxclient.utils.AbstractController;
import io.vavr.control.Try;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
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
        messageDuration = Duration.ofSeconds(3);
        executorService = Executors.newSingleThreadExecutor();
        currentTask = completedFuture(new Object());
    }
}