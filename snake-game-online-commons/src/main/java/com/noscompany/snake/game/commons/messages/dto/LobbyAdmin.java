package com.noscompany.snake.game.commons.messages.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class LobbyAdmin {
    String userName;
    SnakeNumber snakeNumber;
}
