package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.ports.GameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

@AllArgsConstructor
public class GuiLocalGameEventHandler implements GameplayEventHandler {
    protected final GameGridController gameGridController;
    protected final GameOptionsController gameOptionsController;
    protected final MessageController messageController;
    protected final ScoreboardController scoreboardController;

    public static GuiLocalGameEventHandler javaFxEventHandler() {
        return new GuiLocalGameEventHandler(
                Controllers.get(GameGridController.class),
                Controllers.get(GameOptionsController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScoreboardController.class));
    }

    @Override
    public void handle(GameStartCountdown event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            gameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            gameOptionsController.disable();
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void handle(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.handle(event);
            gameOptionsController.enable();
            messageController.printFinishScore(event.getScore());
            scoreboardController.print(event.getScore());
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            gameOptionsController.enable();
            messageController.printGameCanceled();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(messageController::printGamePaused);
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(messageController::printGameResumed);
    }

    public void snakeNameUpdated(PlayerNumber playerNumber, String newName) {
        Platform.runLater(() -> {
            gameOptionsController.updateSnakeName(newName, playerNumber);
            scoreboardController.updateSnakeName(newName, playerNumber);
            messageController.updateSnakeName(newName, playerNumber);
        });
    }

    public void gameCreated(GameState gameState) {
        Platform.runLater(() -> {
            gameGridController.localGameOptionsChanged(gameState);
            scoreboardController.print(gameState.getScore());
            messageController.printPressStartWhenReady();
        });
    }

    public void handle(GameplayCreator.Error error) {
        Platform.runLater(() -> {
            GridSize gridSize = gameOptionsController.gridSize();
            Walls walls = gameOptionsController.walls();
            gameGridController.failedToCreateGamePlay(gridSize, walls);
            scoreboardController.clear();
            messageController.print(error.toString());
        });
    }
}