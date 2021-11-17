package com.noscompany.snakejavafxclient;

import com.noscompany.snakejavafxclient.commons.Stages;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SnakeGameApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Stages
                .getGameModeSelectionStage()
                .show();
    }
}