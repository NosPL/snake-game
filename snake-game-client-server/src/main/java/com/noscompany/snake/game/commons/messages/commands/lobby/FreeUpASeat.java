package com.noscompany.snake.game.commons.messages.commands.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
@NoArgsConstructor(force = true)
public class FreeUpASeat implements MessageDto {
    MessageType messageType = MessageType.FREE_UP_A_SEAT;
}
