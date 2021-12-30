package com.noscompany.snakejavafxclient.game.local;

import com.noscompany.snakejavafxclient.game.GuiGameEventHandler;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.Direction;
import snake.game.core.dto.SnakeNumber;

@AllArgsConstructor
public class LocalSnakeGame {
    private final GuiGameEventHandler eventHandler;
    private SnakeGame snakeGame;

    public void start() {
        if (snakeGame.isRunning())
            return;
        createNewGame()
                .peek(SnakeGame::start);
    }

    public void updateGameView() {
        createNewGame();
    }

    public void cancel() {
        snakeGame.cancel();
    }

    public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {
        snakeGame.changeSnakeDirection(snakeNumber, direction);
    }

    public void pause() {
        snakeGame.pause();
    }

    public void resume() {
        snakeGame.resume();
    }

    private Either<SnakeGameConfiguration.Error, SnakeGame> createNewGame() {
        return LocalGameConfiguration
                .createGame()
                .peek(game -> this.snakeGame = game)
                .peek(game -> eventHandler.gameCreated(game.getGameState()))
                .peekLeft(eventHandler::handle);
    }
}