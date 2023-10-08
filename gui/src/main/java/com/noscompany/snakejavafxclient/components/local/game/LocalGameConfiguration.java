package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import io.vavr.control.Either;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import snake.game.gameplay.Gameplay;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.GameplayConfiguration;
import snake.game.gameplay.GameplayCreator;
import snake.game.gameplay.dto.GameplayParams;

import static com.noscompany.snakejavafxclient.components.local.game.GuiLocalGameEventHandler.javaFxEventHandler;

public class LocalGameConfiguration {

    public static void run() {
        Stage localGameStage = LocalGameStage.get();
        var eventHandler = GuiLocalGameEventHandler.javaFxEventHandler();
        var localSnakeGame = new LocalSnakeGame(eventHandler, new NullGameplay());
        localSnakeGame.updateGameView();
        Controllers.get(GameOptionsController.class).set(localSnakeGame);
        Controllers.get(ScprButtonsController.class)
                .onStartButtonPress(localSnakeGame::start)
                .onCancelButtonPress(localSnakeGame::cancel)
                .onPauseButtonPress(localSnakeGame::pause)
                .onResumeButtonPress(localSnakeGame::resume);
        EventHandler<KeyEvent> keyPressedHandler = new KeyPressedHandler(localSnakeGame);
//        keyPressedHandler = KeyPressedLatencySimulator.simulateLatencyFor(keyPressedHandler);
        localGameStage.getScene().setOnKeyPressed(keyPressedHandler);
        localGameStage.setOnCloseRequest(e -> {
            GameModeSelectionStage.get().show();
            localSnakeGame.cancel();
            LocalGameStage.remove();
        });
        localGameStage.show();
    }

    static Either<GameplayCreator.Error, Gameplay> createGame() {
        var gameOptionsController = Controllers.get(GameOptionsController.class);
        GameplayParams gameplayParams = gameOptionsController.getGameplayParams();
        return new GameplayConfiguration()
                .snakeGameplayCreator()
                .createGame(gameplayParams, javaFxEventHandler());
    }

    private static class NullGameplay implements Gameplay {

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
            return null;
        }
    }
}