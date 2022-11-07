package com.noscompany.snakejavafxclient.components.local.game;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGame;
import snake.game.gameplay.SnakeGameCreator;
import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

@AllArgsConstructor
public class LocalSnakeGame {
    private final GuiGameEventHandler eventHandler;
    private SnakeGame snakeGame;

    public void start() {
        if (snakeGame.isRunning())
            return;
        createNewGame().peek(SnakeGame::start);
    }

    public void updateGameView() {
        createNewGame();
    }

    public void cancel() {
        snakeGame.cancel();
    }

    public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {
        snakeGame.changeSnakeDirection(playerNumber, direction);
    }

    public void pause() {
        snakeGame.pause();
    }

    public void resume() {
        snakeGame.resume();
    }

    private Either<SnakeGameCreator.Error, SnakeGame> createNewGame() {
        return LocalGameConfiguration
                .createGame()
                .peek(game -> this.snakeGame = game)
                .peek(game -> eventHandler.gameCreated(game.getGameState()))
                .peekLeft(eventHandler::handle);
    }
}