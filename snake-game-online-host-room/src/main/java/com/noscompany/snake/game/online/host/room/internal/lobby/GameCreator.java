package com.noscompany.snake.game.online.host.room.internal.lobby;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameplay;
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;
import snake.game.gameplay.dto.GameplayParams;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class GameCreator {
    private SnakeGameplayCreator snakeGameplayCreator;
    private SnakeGameplayEventHandler snakeGameplayEventHandler;

    SnakeGameplay createGame(Set<PlayerNumber> players, GameOptions gameOptions) {
        GameplayParams gameplayParams = toParams(players, gameOptions);
        return snakeGameplayCreator
                .createGame(gameplayParams, snakeGameplayEventHandler)
                .fold(
                        error -> createNullGame(gameOptions),
                        snakeGame -> snakeGame);
    }

    private GameplayParams toParams(Set<PlayerNumber> playerNumbers, GameOptions gameOptions) {
        return new GameplayParams(playerNumbers, gameOptions.getGameSpeed(), gameOptions.getGridSize(), gameOptions.getWalls(), CountdownTime.inSeconds(3));
    }

    private SnakeGameplay createNullGame(GameOptions gameOptions) {
        return new NullGameplay(gameOptions.getGridSize(), gameOptions.getWalls());
    }

    @AllArgsConstructor
    private class NullGameplay implements SnakeGameplay {
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