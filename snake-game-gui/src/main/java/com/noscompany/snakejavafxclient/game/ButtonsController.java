package com.noscompany.snakejavafxclient.game;

import com.noscompany.snakejavafxclient.commons.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonsController extends AbstractController {
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

    @Override
    protected void doInitialize(URL location, ResourceBundle resources) {
        cancelButton.setDisable(true);
        pauseButton.setDisable(true);
        resumeButton.setDisable(true);
    }

    public ButtonsController onStart(Runnable runnable) {
        this.onStart = runnable;
        return this;
    }

    public ButtonsController onCancel(Runnable runnable) {
        this.onCancel = runnable;
        return this;
    }

    public ButtonsController onPause(Runnable runnable) {
        this.onPause = runnable;
        return this;
    }

    public ButtonsController onResume(Runnable runnable) {
        this.onResume = runnable;
        return this;
    }

    public void enableStart() {
        startButton.setDisable(false);
    }

    public void disableStart() {
        startButton.setDisable(true);
    }

    public void enableCancel() {
        cancelButton.setDisable(false);
    }

    public void disableCancel() {
        cancelButton.setDisable(true);
    }

    public void enablePause() {
        pauseButton.setDisable(false);
    }

    public void disablePause() {
        pauseButton.setDisable(true);
    }

    public void enableResume() {
        resumeButton.setDisable(false);
    }

    public void disableResume() {
        resumeButton.setDisable(true);
    }
}