package com.noscompany.snake.game.commons.messages.commands.game;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.Direction;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChangeSnakeDirection implements OnlineMessage {
    MessageType messageType = MessageType.CHANGE_SNAKE_DIRECTION;
    Direction direction;
}