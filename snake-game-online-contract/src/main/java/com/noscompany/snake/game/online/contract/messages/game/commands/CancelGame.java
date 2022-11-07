package com.noscompany.snake.game.online.contract.messages.game.commands;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class CancelGame implements OnlineMessage {
    MessageType messageType = MessageType.CANCEL_GAME;
}