package com.noscompany.snakejavafxclient.components.online.game;

import com.noscompany.snakejavafxclient.components.online.game.client.SnakeMoving;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.*;

@AllArgsConstructor
public class KeyPressedHandler implements EventHandler<KeyEvent> {

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