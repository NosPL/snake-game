package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snakejavafxclient.components.commons.game.grid.GameGridController;
import com.noscompany.snakejavafxclient.components.commons.message.MessageController;
import com.noscompany.snakejavafxclient.components.commons.scoreboard.ScoreboardController;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.GameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class GuiGameplayEventHandler implements GameplayEventHandler {
    protected final GameGridController gameGridController;
    protected final GameOptionsController gameOptionsController;
    protected final MessageController messageController;
    protected final ScprButtonsController scprButtonsController;
    protected final ScoreboardController scoreboardController;

    public static GuiGameplayEventHandler javaFxEventHandler() {
        return new GuiGameplayEventHandler(
                Controllers.get(GameGridController.class),
                Controllers.get(GameOptionsController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ScprButtonsController.class),
                Controllers.get(ScoreboardController.class));
    }

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize(), event.getWalls());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            gameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.print(event.getScore());
            scprButtonsController.disableStart();
            scprButtonsController.enableCancel();
            scprButtonsController.enablePause();
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize(), event.getWalls());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            gameOptionsController.disable();
            scoreboardController.print(event.getScore());
            scprButtonsController.disableStart();
            scprButtonsController.enableCancel();
            scprButtonsController.enablePause();
            messageController.clear();
        });
    }

    @Override
    public void handle(SnakesMoved event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
            gameOptionsController.enable();
            messageController.printFinishScore(event.getScore());
            scoreboardController.print(event.getScore());
            scprButtonsController.enableStart();
            scprButtonsController.disableCancel();
            scprButtonsController.disableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            gameOptionsController.enable();
            messageController.printGameCanceled();
            scprButtonsController.enableStart();
            scprButtonsController.disableCancel();
            scprButtonsController.disableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(() -> {
            messageController.printGamePaused();
            scprButtonsController.enableResume();
            scprButtonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            messageController.printGameResumed();
            scprButtonsController.disableResume();
            scprButtonsController.enablePause();
        });
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
            gameGridController.initializeGrid(gameState.getGridSize(), gameState.getWalls());
            gameGridController.updateGrid(gameState.getSnakes());
            scoreboardController.print(gameState.getScore());
            messageController.printPressStartWhenReady();
        });
    }

    public void handle(GameplayCreator.Error error) {
        Platform.runLater(() -> {
            GridSize gridSize = gameOptionsController.gridSize();
            Walls walls = gameOptionsController.walls();
            gameGridController.initializeGrid(gridSize, walls);
            scoreboardController.clear();
            messageController.print(error.toString());
        });
    }
}