package com.noscompany.snake.game.commons.messages.commands.game;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class PauseGame implements OnlineMessage {
    MessageType messageType = MessageType.PAUSE_GAME;
}