package com.noscompany.snake.game.commons.messages.commands.game;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
@NoArgsConstructor(force = true)
public class PauseGame implements MessageDto {
    MessageType messageType = MessageType.PAUSE_GAME;
}