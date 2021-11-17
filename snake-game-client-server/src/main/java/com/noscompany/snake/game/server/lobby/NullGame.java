package com.noscompany.snake.game.server.lobby;

import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.dto.*;

import java.util.HashMap;
import java.util.LinkedList;

@AllArgsConstructor
class NullGame implements SnakeGame {
    private GridSize gridSize;

    @Override
    public void start() {

    }

    @Override
    public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {

    }

    @Override
    public void changeSnakesDirection(Direction direction) {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public GameState getGameState() {
        return new GameState(
                new LinkedList<>(),
                gridSize,
                Point.point(-1, -1),
                new Score(new HashMap<>()));
    }
}
