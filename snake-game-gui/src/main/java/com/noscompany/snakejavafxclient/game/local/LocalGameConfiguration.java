package com.noscompany.snakejavafxclient.game.local;

import com.noscompany.snakejavafxclient.commons.Controllers;
import com.noscompany.snakejavafxclient.commons.Stages;
import com.noscompany.snakejavafxclient.game.ButtonsController;
import com.noscompany.snakejavafxclient.game.GuiGameEventHandler;
import io.vavr.control.Either;
import javafx.stage.Stage;
import snake.game.core.SnakeGame;
import snake.game.core.SnakeGameConfiguration;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;

import static com.noscompany.snakejavafxclient.game.GuiGameEventHandler.javaFxEventHandler;

public class LocalGameConfiguration {

    public static void run() {
        Stage localGameStage = Stages.getLocalGameStage();
        var eventHandler = GuiGameEventHandler.javaFxEventHandler();
        var localSnakeGame = new LocalSnakeGame(new NullGame(), eventHandler);
        localSnakeGame.updateGameView();
        Controllers.get(GameOptionsController.class).set(localSnakeGame);
        Controllers.get(ButtonsController.class)
                .onStart(localSnakeGame::start)
                .onCancel(localSnakeGame::cancel)
                .onPause(localSnakeGame::pause)
                .onResume(localSnakeGame::resume);
        localGameStage.getScene().setOnKeyPressed(new KeyPressedHandler(localSnakeGame));
        localGameStage.setOnCloseRequest(e -> {
            Stages.getGameModeSelectionStage().show();
            localSnakeGame.cancel();
            Stages.removeLocalGameStage();
        });
        localGameStage.show();
    }

    static Either<SnakeGameConfiguration.Error, SnakeGame> createGame() {
        var gameOptions = Controllers.get(GameOptionsController.class);
        return new SnakeGameConfiguration()
                .set(gameOptions.gameSpeed())
                .set(gameOptions.gridSize())
                .set(gameOptions.playerNumbers())
                .set(gameOptions.walls())
                .set(CountdownTime.inSeconds(3))
                .set(javaFxEventHandler())
                .create();
    }

    private static class NullGame implements SnakeGame {

        @Override
        public void start() {

        }

        @Override
        public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {

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