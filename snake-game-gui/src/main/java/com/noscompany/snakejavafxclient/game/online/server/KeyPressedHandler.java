package com.noscompany.snakejavafxclient.game.online.server;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static snake.game.core.dto.Direction.*;

@AllArgsConstructor
class KeyPressedHandler implements EventHandler<KeyEvent> {
    private final SnakeServerUserIdWrapper snakeServer;

    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            snakeServer.changeSnakeDirection(DOWN);
        else if (keyCode == KeyCode.S)
            snakeServer.changeSnakeDirection(UP);
        else if (keyCode == KeyCode.A)
            snakeServer.changeSnakeDirection(LEFT);
        else if (keyCode == KeyCode.D)
            snakeServer.changeSnakeDirection(RIGHT);

        else if (keyCode == KeyCode.P)
            snakeServer.pauseGame();
        else if (keyCode == KeyCode.R)
            snakeServer.resumeGame();
        else if (keyCode == KeyCode.C)
            snakeServer.cancelGame();
        else if (keyCode == KeyCode.SHIFT)
            snakeServer.startGame();
    }
}