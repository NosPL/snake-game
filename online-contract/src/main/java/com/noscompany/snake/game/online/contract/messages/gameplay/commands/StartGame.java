package com.noscompany.snake.game.online.contract.messages.gameplay.commands;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
public class StartGame implements OnlineMessage {
    MessageType messageType = MessageType.START_GAME;
}