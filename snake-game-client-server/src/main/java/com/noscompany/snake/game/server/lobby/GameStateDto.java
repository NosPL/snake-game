package com.noscompany.snake.game.server.lobby;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameStateDto {
    Collection<SnakeDto> snakes;
    GridSize gridSize;
    Point foodPoint;
    Score score;

    public static GameStateDto asDto(GameState gameState) {
        return new GameStateDto(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getFoodPoint(),
                gameState.getScore());
    }
}