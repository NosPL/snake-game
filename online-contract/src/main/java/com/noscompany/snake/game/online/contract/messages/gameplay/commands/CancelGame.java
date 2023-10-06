package com.noscompany.snake.game.online.contract.messages.gameplay.commands;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CancelGame implements OnlineMessage {
    MessageType messageType = MessageType.CANCEL_GAME;
    UserId userId;
}