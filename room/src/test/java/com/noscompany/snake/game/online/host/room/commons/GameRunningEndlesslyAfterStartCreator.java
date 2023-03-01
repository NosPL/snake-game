package com.noscompany.snake.game.online.host.room.commons;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.GameplayEventHandler;
import snake.game.gameplay.dto.GameplayParams;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *Creates a mock game that runs endlessly without emitting any events after invoking SnakeGame.start()
 */
public class GameRunningEndlesslyAfterStartCreator implements GameplayCreator {

    @Override
    public Either<Error, Gameplay> createGame(GameplayParams gameplayParams, GameplayEventHandler gameplayEventHandler) {
        return Either.right(new AlwaysRunningGameplay(GridSize._10x10, Walls.ON));
    }

    @RequiredArgsConstructor
    private class AlwaysRunningGameplay implements Gameplay {
        private final GridSize gridSize;
        private final Walls walls;
        private final AtomicBoolean isRunning = new AtomicBoolean(false);

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