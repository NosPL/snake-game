package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.*;

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
    }
}