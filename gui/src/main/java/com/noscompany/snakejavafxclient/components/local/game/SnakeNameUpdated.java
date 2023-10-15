package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import lombok.Value;

@Value
public class SnakeNameUpdated {
    PlayerNumber playerNumber;
    String newName;
}