package com.noscompany.snakejavafxclient;

import com.noscompany.snake.game.online.main.menu.GameModeSelectionStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class SnakeGameApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        GameModeSelectionStage.get().show();
    }
}