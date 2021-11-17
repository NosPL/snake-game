package com.noscompany.snake.game.commons.messages.events.lobby;

import com.noscompany.snake.game.commons.MessageDto;
import com.noscompany.snake.game.server.lobby.GameLobbyState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Walls;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameOptionsChanged implements MessageDto {
    MessageType messageType = MessageType.GAME_OPTIONS_CHANGED;
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
    GameLobbyState gameLobbyState;
}