package com.noscompany.snake.game.online.local.game;

import com.noscompany.snake.game.online.gui.commons.Controllers;
import com.noscompany.snake.game.online.local.game.edit.snake.name.EditSnakeNameController;
import com.noscompany.snake.game.online.local.game.edit.snake.name.EditSnakeNameStage;
import com.noscompany.snake.game.online.local.game.grid.GameGridController;
import com.noscompany.snake.game.online.local.game.message.MessageController;
import com.noscompany.snake.game.online.local.game.scoreboard.ScoreboardController;
import com.noscompany.snake.game.online.local.game.scpr.buttons.ScprButtonsController;
import snake.game.gameplay.ports.GameplayEventHandler;

public class LocalGameConfiguration {

    public static void run(Runnable onCloseAction) {
        var localGameStage = LocalGameStage.get();
        EditSnakeNameStage.get();
        var localSnakeGame = new LocalSnakeGame(new NullGameplay(), gameplayEventHandler());
        configureControllers(localSnakeGame);
        localGameStage.getScene().setOnKeyPressed(new KeyPressedHandler(localSnakeGame));
        localGameStage.setOnCloseRequest(e -> {
            localSnakeGame.cancel();
            LocalGameStage.remove();
            EditSnakeNameStage.remove();
            onCloseAction.run();
        });
        localSnakeGame.updateGameGrid();
        localGameStage.show();
    }

    private static void configureControllers(LocalSnakeGame localSnakeGame) {
        Controllers
                .get(EditSnakeNameController.class)
                .onOnChangeSnakeNameButtonPress((playerNumber, newName) -> {
                    Controllers.get(GameOptionsController.class).snakeNameUpdated(playerNumber, newName);
                    Controllers.get(ScoreboardController.class).snakeNameUpdated(playerNumber, newName);
                });
        Controllers
                .get(GameOptionsController.class)
                .set(localSnakeGame);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(localSnakeGame::start)
                .onCancelButtonPress(localSnakeGame::cancel)
                .onPauseButtonPress(localSnakeGame::pause)
                .onResumeButtonPress(localSnakeGame::resume);
    }

    private static GameplayEventHandler gameplayEventHandler() {
        return new MessagePublisherGameplayEvent(
                Controllers.get(ScoreboardController.class),
                Controllers.get(GameGridController.class),
                Controllers.get(MessageController.class)
        );
    }
}