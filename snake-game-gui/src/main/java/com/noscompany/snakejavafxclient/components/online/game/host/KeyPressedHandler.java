package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snakejavafxclient.components.online.game.client.SnakeMoving;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.*;

@AllArgsConstructor
class KeyPressedHandler implements EventHandler<KeyEvent> {
    private final SnakeOnlineHost snakeOnlineHost;
    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            snakeOnlineHost.changeSnakeDirection(DOWN);
        else if (keyCode == KeyCode.S)
            snakeOnlineHost.changeSnakeDirection(UP);
        else if (keyCode == KeyCode.A)
            snakeOnlineHost.changeSnakeDirection(LEFT);
        else if (keyCode == KeyCode.D)
            snakeOnlineHost.changeSnakeDirection(RIGHT);
    }
}