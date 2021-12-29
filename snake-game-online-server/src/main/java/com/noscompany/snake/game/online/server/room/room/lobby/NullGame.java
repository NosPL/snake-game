package com.noscompany.snake.game.online.server.room.room.lobby;

import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.dto.*;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
class NullGame implements SnakeGame {
    private final GridSize gridSize;

    @Override
    public void start() {

    }

    @Override
    public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {

    }

    @Override
    public void kill(SnakeNumber snakeNumber) {

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
                new Score(List.of()));
    }
}
