package com.noscompany.snake.game.commons.messages.dto;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameLobbyState {
    GridSize gridSize;
    GameSpeed gameSpeed;
    Walls walls;
    Option<LobbyAdmin> admin;
    Map<String, SnakeNumber> joinedPlayers;
    boolean gameIsRunning;
    GameState gameState;
}