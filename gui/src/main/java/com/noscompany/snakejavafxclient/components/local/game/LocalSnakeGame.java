package com.noscompany.snakejavafxclient.components.local.game;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.GameplayCreator;

@AllArgsConstructor
public class LocalSnakeGame {
    private final GuiGameplayEventHandler eventHandler;
    private Gameplay gameplay;

    public void start() {
        if (gameplay.isRunning())
            return;
        createNewGame().peek(Gameplay::start);
    }

    public void updateGameView() {
        createNewGame();
    }

    public void cancel() {
        gameplay.cancel();
    }

    public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {
        gameplay.changeSnakeDirection(playerNumber, direction);
    }

    public void pause() {
        gameplay.pause();
    }

    public void resume() {
        gameplay.resume();
    }

    private Either<GameplayCreator.Error, Gameplay> createNewGame() {
        return LocalGameConfiguration
                .createGame()
                .peek(game -> this.gameplay = game)
                .peek(game -> eventHandler.gameCreated(game.getGameState()))
                .peekLeft(eventHandler::handle);
    }
}