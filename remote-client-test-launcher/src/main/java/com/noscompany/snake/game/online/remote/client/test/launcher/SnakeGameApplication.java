package com.noscompany.snake.game.online.remote.client.test.launcher;

import com.noscompany.snake.game.online.remote.client.gui.SnakeOnlineGuiClientConfiguration;
import com.noscompany.snake.game.test.client.SnakeOnlineTestClientConfiguration;
import javafx.application.Application;
import javafx.stage.Stage;

public class SnakeGameApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        new SnakeOnlineGuiClientConfiguration().configure(
                () -> {},
                messagePublisher -> new SnakeOnlineTestClientConfiguration().snakeOnlineTestClient());
    }
}