package com.noscompany.snake.game.server.lobby;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class LobbyState {
    String admin;
    Map<String, SnakeNumber> joinedPlayers;
    boolean gameIsRunning;
}