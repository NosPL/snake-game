package com.noscompany.snake.game.online.server.room.room.commons;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.*;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *Creates a mock game that runs endlessly without emitting any events after invoking SnakeGame.start()
 */
public class EndlesslyRunningGameConfiguration extends SnakeGameConfiguration {

    @Override
    public Either<Error, SnakeGame> create() {
        return super.create()
                .map(snakeGame -> new AlwaysRunningGame(GridSize._10x10));
    }

    @RequiredArgsConstructor
    private class AlwaysRunningGame implements SnakeGame {
        private final GridSize gridSize;
        private AtomicBoolean isRunning = new AtomicBoolean(false);

        @Override
        public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {

        }

        @Override
        public void kill(SnakeNumber snakeNumber) {

        }

        @Override
        public GameState getGameState() {
            return new GameState(new LinkedList<>(), gridSize, Point.point(-1, -1), new Score(new LinkedList<>()));
        }

        @Override
        public void start() {
            isRunning.set(true);
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
        public boolean isRunning() {
            return isRunning.get();
        }
    }
}
