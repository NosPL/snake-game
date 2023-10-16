package com.noscompany.snake.game.online.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import com.noscompany.snake.game.online.local.game.grid.GameGridController;
import com.noscompany.snake.game.online.local.game.message.MessageController;
import com.noscompany.snake.game.online.local.game.scoreboard.ScoreboardController;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.gameplay.Gameplay;
import snake.game.gameplay.GameplayConfiguration;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.dto.GameplayParams;
import snake.game.gameplay.ports.GameplayEventHandler;

@AllArgsConstructor
public class LocalSnakeGame {
    private Gameplay gameplay;
    private final GameplayEventHandler gameplayEventHandler;

    public void start() {
        if (gameplay.isRunning())
            return;
        createNewGame().peek(Gameplay::start);
    }

    public void updateGameGrid() {
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
        return new GameplayConfiguration()
                .snakeGameplayCreator()
                .createGame(gamePlayParams(), gameplayEventHandler)
                .peek(this::prepareGame)
                .peekLeft(this::failedToCreateGame);
    }

    private void prepareGame(Gameplay gameplay) {
        this.gameplay = gameplay;
        var gameState = gameplay.getGameState();
        Controllers.get(GameGridController.class).localGameOptionsChanged(gameState);
        Controllers.get(ScoreboardController.class).print(gameState.getScore());
        Controllers.get(MessageController.class).printPressStartWhenReady();
    }

    private void failedToCreateGame(GameplayCreator.Error error) {
        var gameplayParams = gamePlayParams();
        Controllers.get(GameGridController.class).failedToCreateGamePlay(gameplayParams.getGridSize(), gameplayParams.getWalls());
        Controllers.get(ScoreboardController.class).clear();
        Controllers.get(MessageController.class).print(error);
    }

    private GameplayParams gamePlayParams() {
        return Controllers
                .get(GameOptionsController.class)
                .getGameplayParams();
    }
}