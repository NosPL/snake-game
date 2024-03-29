package com.noscompany.snake.game.online.local.game.scpr.buttons;

import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ScprButtonsController extends AbstractController {
    @FXML
    private Button startButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;

    private Runnable onStart = () -> {
    };
    private Runnable onCancel = () -> {
    };
    private Runnable onPause = () -> {
    };
    private Runnable onResume = () -> {
    };

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        super.doInitialize(location, resources);
    }

    @FXML
    public void startGame() {
        onStart.run();
    }

    @FXML
    public void cancelGame() {
        onCancel.run();
    }

    @FXML
    public void pauseGame() {
        onPause.run();
    }

    @FXML
    public void resumeGame() {
        onResume.run();
    }


    public ScprButtonsController onStartButtonPress(Runnable runnable) {
        this.onStart = runnable;
        return this;
    }

    public ScprButtonsController onCancelButtonPress(Runnable runnable) {
        this.onCancel = runnable;
        return this;
    }

    public ScprButtonsController onPauseButtonPress(Runnable runnable) {
        this.onPause = runnable;
        return this;
    }

    public ScprButtonsController onResumeButtonPress(Runnable runnable) {
        this.onResume = runnable;
        return this;
    }

    @Override
    public Subscription getSubscription() {
        return new Subscription();
    }
}