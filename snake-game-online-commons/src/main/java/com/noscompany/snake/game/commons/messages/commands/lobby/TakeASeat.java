package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TakeASeat implements OnlineMessage {
    MessageType messageType = MessageType.TAKE_A_SEAT;
    SnakeNumber snakeNumber;
}