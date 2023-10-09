package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.Value;

@Value
public class GameResumed implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_RESUMED;
}