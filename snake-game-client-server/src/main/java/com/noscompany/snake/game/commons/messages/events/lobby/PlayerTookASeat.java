package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.server.lobby.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class PlayerTookASeat implements MessageDto {
    MessageType messageType = MessageType.PLAYER_TOOK_A_SEAT;
    String userId;
    SnakeNumber snakeNumber;
    GameLobbyState gameLobbyState;
}