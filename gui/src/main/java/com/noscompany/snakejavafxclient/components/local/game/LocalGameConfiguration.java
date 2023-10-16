package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import com.noscompany.snakejavafxclient.components.local.game.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.local.game.edit.snake.name.EditSnakeNameController;
import com.noscompany.snakejavafxclient.components.local.game.edit.snake.name.EditSnakeNameStage;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;

public class LocalGameConfiguration {

    public static void run() {
        var localGameStage = LocalGameStage.get();
        var editSnakeNameStage = EditSnakeNameStage.get();
        var messagePublisher = new MessagePublisherCreator().create();
        var localSnakeGame = new LocalSnakeGame(new NullGameplay(), messagePublisher);
        configureControllers(localSnakeGame);
        subscribeControllers(messagePublisher);
        localGameStage.getScene().setOnKeyPressed(new KeyPressedHandler(localSnakeGame));
        localGameStage.setOnCloseRequest(e -> {
            messagePublisher.shutdown();
            localSnakeGame.cancel();
            LocalGameStage.remove();
            EditSnakeNameStage.remove();
            GameModeSelectionStage.get().show();
        });
        localSnakeGame.updateGameGrid();
        localGameStage.show();
    }

    private static void configureControllers(LocalSnakeGame localSnakeGame) {
        Controllers
                .get(EditSnakeNameController.class)
                .setSnakeNameUpdatedConsumer(localSnakeGame::snakeNameUpdated);
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

    private static void subscribeControllers(MessagePublisher messagePublisher) {
        Controllers
                .getAll().stream()
                .map(AbstractController::getSubscription)
                .forEach(messagePublisher::subscribe);
    }
    
    
}