package com.noscompany.snake.game.online.contract.messages.playground;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.PublicClientMessage;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(access = PRIVATE, force = true)
@AllArgsConstructor
public class GameReinitialized implements PublicClientMessage {
    OnlineMessage.MessageType messageType = MessageType.GAME_REINITIALIZED;
    GameState gameState;
}