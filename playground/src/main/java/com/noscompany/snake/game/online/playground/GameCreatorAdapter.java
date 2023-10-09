package com.noscompany.snake.game.online.playground;

import com.noscompany.snake.game.online.contract.messages.game.options.GameOptions;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.dto.GameplayParams;
import snake.game.gameplay.ports.GameplayEventHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class GameCreatorAdapter {
    private GameplayCreator gameplayCreator;
    private GameplayEventHandler gameplayEventHandler;

    Gameplay createGame(Set<PlayerNumber> players, GameOptions gameOptions) {
        GameplayParams gameplayParams = toParams(players, gameOptions);
        return gameplayCreator
                .createGame(gameplayParams, gameplayEventHandler)
                .fold(
                        error -> createNullGame(gameOptions),
                        snakeGame -> snakeGame);
    }

    private GameplayParams toParams(Set<PlayerNumber> playerNumbers, GameOptions gameOptions) {
        return new GameplayParams(playerNumbers, gameOptions.getGameSpeed(), gameOptions.getGridSize(), gameOptions.getWalls(), CountdownTime.inSeconds(3));
    }

    private Gameplay createNullGame(GameOptions gameOptions) {
        return new NullGameplay(gameOptions.getGridSize(), gameOptions.getWalls());
    }

    @AllArgsConstructor
    private class NullGameplay implements Gameplay {
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