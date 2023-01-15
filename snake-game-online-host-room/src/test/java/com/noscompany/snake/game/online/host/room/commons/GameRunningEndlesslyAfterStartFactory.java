package com.noscompany.snake.game.online.host.room.commons;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameEventHandler;
import snake.game.gameplay.SnakeGameplayBuilder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *Creates a mock game that runs endlessly without emitting any events after invoking SnakeGame.start()
 */
public class GameRunningEndlesslyAfterStartFactory implements SnakeGameplayBuilder {

    @Override
    public SnakeGameplayBuilder set(@NonNull PlayerNumber player) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull Collection<PlayerNumber> players) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull GameSpeed gameSpeed) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull GridSize gridSize) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull Walls walls) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull CountdownTime countdownTime) {
        return this;
    }

    @Override
    public SnakeGameplayBuilder set(@NonNull SnakeGameEventHandler eventHandler) {
        return this;
    }

    @Override
    public Either<Error, SnakeGameplay> createGame() {
        return Either.right(new AlwaysRunningGameplay(GridSize._10x10, Walls.ON));
    }

    @RequiredArgsConstructor
    private class AlwaysRunningGameplay implements SnakeGameplay {
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