package com.noscompany.snakejavafxclient.components.online.game.commons;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.DOWN;
import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.LEFT;
import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.RIGHT;
import static com.noscompany.snake.game.online.contract.messages.game.dto.Direction.UP;

@AllArgsConstructor
public class KeyPressedHandler implements EventHandler<KeyEvent> {
    private final Consumer<Direction> directionConsumer;
    @Override
    public void handle(KeyEvent event) {
        var keyCode = event.getCode();
        if (keyCode == KeyCode.W)
            directionConsumer.accept(DOWN);
        else if (keyCode == KeyCode.S)
            directionConsumer.accept(UP);
        else if (keyCode == KeyCode.A)
            directionConsumer.accept(LEFT);
        else if (keyCode == KeyCode.D)
            directionConsumer.accept(RIGHT);
    }
}