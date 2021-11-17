package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;

@Value(staticConstructor = "of")
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TakeASeat implements MessageDto {
    MessageType messageType = MessageType.TAKE_A_SEAT;
    SnakeNumber playerNumber;
}
