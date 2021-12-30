package com.noscompany.snakejavafxclient;

import com.noscompany.snakejavafxclient.commons.Stages;
import javafx.application.Application;
import javafx.stage.Stage;

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