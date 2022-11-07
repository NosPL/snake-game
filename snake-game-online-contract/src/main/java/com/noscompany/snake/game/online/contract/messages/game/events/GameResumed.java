package com.noscompany.snake.game.online.contract.messages.game.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.Getter;

@Getter
public class GameResumed implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_RESUMED;
}