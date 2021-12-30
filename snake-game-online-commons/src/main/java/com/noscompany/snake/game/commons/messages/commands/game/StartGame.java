package com.noscompany.snake.game.commons.messages.commands.game;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class StartGame implements OnlineMessage {
    MessageType messageType = MessageType.START_GAME;
}