package com.noscompany.snakejavafxclient.game.online.client;

import com.noscompany.snake.game.client.SnakeOnlineClient;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static snake.game.core.dto.Direction.*;

@AllArgsConstructor
class KeyPressedHandler implements EventHandler<KeyEvent> {
    private final SnakeOnlineClient snakeOnlineClient;

    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            snakeOnlineClient.changeSnakeDirection(DOWN);
        else if (keyCode == KeyCode.S)
            snakeOnlineClient.changeSnakeDirection(UP);
        else if (keyCode == KeyCode.A)
            snakeOnlineClient.changeSnakeDirection(LEFT);
        else if (keyCode == KeyCode.D)
            snakeOnlineClient.changeSnakeDirection(RIGHT);

        else if (keyCode == KeyCode.P)
            snakeOnlineClient.pauseGame();
        else if (keyCode == KeyCode.R)
            snakeOnlineClient.resumeGame();
        else if (keyCode == KeyCode.C)
            snakeOnlineClient.cancelGame();
        else if (keyCode == KeyCode.SHIFT)
            snakeOnlineClient.startGame();
    }
}