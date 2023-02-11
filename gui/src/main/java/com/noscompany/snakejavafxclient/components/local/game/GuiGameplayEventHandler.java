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
import snake.game.gameplay.SnakeGameplayCreator;
import snake.game.gameplay.SnakeGameplayEventHandler;
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
public class GuiGameplayEventHandler implements SnakeGameplayEventHandler {
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
        LagSimulator.lag(() -> {
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
        });
    }

    @Override
    public void handle(GameStarted event) {
        LagSimulator.lag(() -> {
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
        });
    }

    @Override
    public void handle(SnakesMoved event) {
        LagSimulator.lag(() -> {
            Platform.runLater(() -> {
                gameGridController.updateGrid(event.getSnakes(), event.getFoodPosition());
                scoreboardController.print(event.getScore());
                messageController.clear();
            });
        });
    }

    @Override
    public void handle(GameFinished event) {
        LagSimulator.lag(() -> {
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
        });
    }

    @Override
    public void handle(GameCancelled event) {
        LagSimulator.lag(() -> {
            Platform.runLater(() -> {
                gameOptionsController.enable();
                messageController.printGameCanceled();
                scprButtonsController.enableStart();
                scprButtonsController.disableCancel();
                scprButtonsController.disableResume();
                scprButtonsController.disablePause();
            });
        });
    }

    @Override
    public void handle(GamePaused event) {
        LagSimulator.lag(() -> {
            Platform.runLater(() -> {
                messageController.printGamePaused();
                scprButtonsController.enableResume();
                scprButtonsController.disablePause();
            });
        });
    }

    @Override
    public void handle(GameResumed event) {
        LagSimulator.lag(() -> {
            Platform.runLater(() -> {
                messageController.printGameResumed();
                scprButtonsController.disableResume();
                scprButtonsController.enablePause();
            });
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

    public void handle(SnakeGameplayCreator.Error error) {
        Platform.runLater(() -> {
            GridSize gridSize = gameOptionsController.gridSize();
            Walls walls = gameOptionsController.walls();
            gameGridController.initializeGrid(gridSize, walls);
            scoreboardController.clear();
            messageController.print(error.toString());
        });
    }

    private class LagSimulator {
        private static ExecutorService executorService = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        private static Random random = new Random();

        static void lag(Runnable runnable) {
            sleep(0);
            runnable.run();
        }

        @SneakyThrows
        static private void sleep(int millis) {
            Thread.sleep(millis);
        }
    }
}