package com.noscompany.snakejavafxclient.components.local.game;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction.*;
import static com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber.*;

@AllArgsConstructor
class KeyPressedHandler implements EventHandler<KeyEvent> {
    private LocalSnakeGame localSnakeGame;

    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            localSnakeGame.changeSnakeDirection(_1, DOWN);
        else if (keyCode == KeyCode.S)
            localSnakeGame.changeSnakeDirection(_1, UP);
        else if (keyCode == KeyCode.A)
            localSnakeGame.changeSnakeDirection(_1, LEFT);
        else if (keyCode == KeyCode.D)
            localSnakeGame.changeSnakeDirection(_1, RIGHT);

        if (keyCode == KeyCode.G)
            localSnakeGame.changeSnakeDirection(_2, DOWN);
        else if (keyCode == KeyCode.B)
            localSnakeGame.changeSnakeDirection(_2, UP);
        else if (keyCode == KeyCode.V)
            localSnakeGame.changeSnakeDirection(_2, LEFT);
        else if (keyCode == KeyCode.N)
            localSnakeGame.changeSnakeDirection(_2, RIGHT);

        if (keyCode == KeyCode.L)
            localSnakeGame.changeSnakeDirection(_3, DOWN);
        else if (keyCode == KeyCode.PERIOD)
            localSnakeGame.changeSnakeDirection(_3, UP);
        else if (keyCode == KeyCode.COMMA)
            localSnakeGame.changeSnakeDirection(_3, LEFT);
        else if (keyCode == KeyCode.SLASH)
            localSnakeGame.changeSnakeDirection(_3, RIGHT);

        if (keyCode == KeyCode.NUMPAD8)
            localSnakeGame.changeSnakeDirection(_4, DOWN);
        else if (keyCode == KeyCode.NUMPAD5)
            localSnakeGame.changeSnakeDirection(_4, UP);
        else if (keyCode == KeyCode.NUMPAD4)
            localSnakeGame.changeSnakeDirection(_4, LEFT);
        else if (keyCode == KeyCode.NUMPAD6)
            localSnakeGame.changeSnakeDirection(_4, RIGHT);

        else if (keyCode == KeyCode.OPEN_BRACKET)
            localSnakeGame.pause();
        else if (keyCode == KeyCode.CLOSE_BRACKET)
            localSnakeGame.resume();
    }
}