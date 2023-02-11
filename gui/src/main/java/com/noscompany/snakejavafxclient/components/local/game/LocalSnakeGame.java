package com.noscompany.snakejavafxclient.components.local.game;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.SnakeGameplay;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.SnakeGameplayCreator;

@AllArgsConstructor
public class LocalSnakeGame {
    private final GuiGameplayEventHandler eventHandler;
    private SnakeGameplay snakeGameplay;

    public void start() {
        if (snakeGameplay.isRunning())
            return;
        createNewGame().peek(SnakeGameplay::start);
    }

    public void updateGameView() {
        createNewGame();
    }

    public void cancel() {
        snakeGameplay.cancel();
    }

    public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {
        snakeGameplay.changeSnakeDirection(playerNumber, direction);
    }

    public void pause() {
        snakeGameplay.pause();
    }

    public void resume() {
        snakeGameplay.resume();
    }

    private Either<SnakeGameplayCreator.Error, SnakeGameplay> createNewGame() {
        return LocalGameConfiguration
                .createGame()
                .peek(game -> this.snakeGameplay = game)
                .peek(game -> eventHandler.gameCreated(game.getGameState()))
                .peekLeft(eventHandler::handle);
    }
}