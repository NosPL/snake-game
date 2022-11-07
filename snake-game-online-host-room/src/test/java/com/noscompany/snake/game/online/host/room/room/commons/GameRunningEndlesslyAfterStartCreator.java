package com.noscompany.snake.game.online.host.room.room.commons;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import snake.game.gameplay.SnakeGame;
import snake.game.gameplay.SnakeGameCreator;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *Creates a mock game that runs endlessly without emitting any events after invoking SnakeGame.start()
 */
public class GameRunningEndlesslyAfterStartCreator extends SnakeGameCreator {

    @Override
    public Either<Error, SnakeGame> createGame() {
        return super.createGame()
                .map(snakeGame -> new AlwaysRunningGame(GridSize._10x10, Walls.ON));
    }

    @RequiredArgsConstructor
    private class AlwaysRunningGame implements SnakeGame {
        private final GridSize gridSize;
        private final Walls walls;
        private AtomicBoolean isRunning = new AtomicBoolean(false);

        @Override
        public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {

        }

        @Override
        public void killSnake(PlayerNumber playerNumber) {

        }

        @Override
        public GameState getGameState() {
            return new GameState(new LinkedList<>(), gridSize, walls, Option.none(), new Score(new LinkedList<>()));
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