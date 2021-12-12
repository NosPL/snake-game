package com.noscompany.snakejavafxclient.game;

import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.game.grid.controller.GameGridController;
import com.noscompany.snakejavafxclient.game.local.GameOptionsController;
import javafx.application.Platform;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.GameState;
import snake.game.core.dto.GridSize;
import snake.game.core.events.*;

@AllArgsConstructor
public class GuiGameEventHandler implements SnakeGameEventHandler {
    protected final GameGridController gameGridController;
    protected final GameOptionsController gameOptionsController;
    protected final MessageController messageController;
    protected final ButtonsController buttonsController;
    protected final ScoreboardController scoreboardController;

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            gameOptionsController.disable();
            messageController.printSecondsLeftToStart(event.getSecondsLeft());
            scoreboardController.reset();
            buttonsController.disableStart();
            buttonsController.enableCancel();
            buttonsController.enablePause();
        });
    }

    @Override
    public void handle(GameStarted event) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(event.getGridSize());
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            gameOptionsController.disable();
            scoreboardController.print(event.getScore());
            buttonsController.disableStart();
            buttonsController.enableCancel();
            buttonsController.enablePause();
            messageController.clear();
        });
    }

    @Override
    public void handle(GameContinues event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            scoreboardController.print(event.getScore());
            messageController.clear();
        });
    }

    @Override
    public void handle(GameFinished event) {
        Platform.runLater(() -> {
            gameGridController.updateGrid(event.getSnakes(), event.getFoodPoint());
            gameOptionsController.enable();
            messageController.printGameFinished();
            scoreboardController.print(event.getScore());
            buttonsController.enableStart();
            buttonsController.disableCancel();
            buttonsController.disableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GameCancelled event) {
        Platform.runLater(() -> {
            gameOptionsController.enable();
            messageController.printGameCanceled();
            buttonsController.enableStart();
            buttonsController.disableCancel();
            buttonsController.disableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GamePaused event) {
        Platform.runLater(() -> {
            messageController.printGamePaused();
            buttonsController.enableResume();
            buttonsController.disablePause();
        });
    }

    @Override
    public void handle(GameResumed event) {
        Platform.runLater(() -> {
            messageController.printGameResumed();
            buttonsController.disableResume();
            buttonsController.enablePause();
        });
    }

    public void gameCreated(GameState gameState) {
        Platform.runLater(() -> {
            gameGridController.initializeGrid(gameState.getGridSize());
            gameGridController.updateGrid(gameState.getSnakes());
            scoreboardController.print(gameState.getScore());
            messageController.clear();
        });
    }

    public void handle(SnakeGameConfiguration.Error error) {
        Platform.runLater(() -> {
            GridSize gridSize = gameOptionsController.gridSize();
            gameGridController.initializeGrid(gridSize);
            scoreboardController.reset();
            messageController.print(error);
        });
    }

    public static GuiGameEventHandler javaFxEventHandler() {
        return new GuiGameEventHandler(
                Controllers.get(GameGridController.class),
                Controllers.get(GameOptionsController.class),
                Controllers.get(MessageController.class),
                Controllers.get(ButtonsController.class),
                Controllers.get(ScoreboardController.class));
    }
}