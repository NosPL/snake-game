package com.noscompany.snake.game.online.contract.messages.gameplay.commands;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class ChangeSnakeDirection implements OnlineMessage {
    MessageType messageType = MessageType.CHANGE_SNAKE_DIRECTION;
    Direction direction;
}