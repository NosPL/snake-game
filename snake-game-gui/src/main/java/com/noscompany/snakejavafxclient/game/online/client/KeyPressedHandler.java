package com.noscompany.snakejavafxclient.game.online.client;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static snake.game.core.dto.Direction.*;

@AllArgsConstructor
class KeyPressedHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            SnakeMoving.changeSnakeDirection(DOWN);
        else if (keyCode == KeyCode.S)
            SnakeMoving.changeSnakeDirection(UP);
        else if (keyCode == KeyCode.A)
            SnakeMoving.changeSnakeDirection(LEFT);
        else if (keyCode == KeyCode.D)
            SnakeMoving.changeSnakeDirection(RIGHT);
    }
}