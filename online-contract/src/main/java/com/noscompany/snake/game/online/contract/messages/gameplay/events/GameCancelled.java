package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import lombok.Value;

@Value
public class GameCancelled implements GameEvent, PublicClientMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_CANCELLED;
}