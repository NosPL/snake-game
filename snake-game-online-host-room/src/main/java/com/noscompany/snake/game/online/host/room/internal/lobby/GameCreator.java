package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGame;
import snake.game.gameplay.SnakeGameCreator;
import snake.game.gameplay.SnakeGameEventHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class GameCreator {
    private SnakeGameCreator snakeGameCreator;
    private SnakeGameEventHandler snakeGameEventHandler;

    SnakeGame createGame(Set<PlayerNumber> players, GameOptions gameOptions) {
        return snakeGameCreator
                .set(gameOptions.getGameSpeed())
                .set(gameOptions.getGridSize())
                .set(gameOptions.getWalls())
                .set(players)
                .set(snakeGameEventHandler)
                .set(CountdownTime.inSeconds(3))
                .createGame()
                .fold(
                        error -> createNullGame(gameOptions),
                        snakeGame -> snakeGame);
    }

    private SnakeGame createNullGame(GameOptions gameOptions) {
        return new NullGame(gameOptions.getGridSize(), gameOptions.getWalls());
    }

    @AllArgsConstructor
    private class NullGame implements SnakeGame {
        private final GridSize gridSize;
        private final Walls walls;

        @Override
        public void start() {
        }

        @Override
        public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {

        }

        @Override
        public void killSnake(PlayerNumber playerNumber) {

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
                    walls,
                    Option.none(),
                    new Score(List.of()));
        }
    }
}