package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.user.registry.UsersCountLimit;
import com.noscompany.snake.game.online.host.dependency.configurator.SnakeOnlineHostDependencyConfigurator;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

import java.util.UUID;

class SnakeOnlineHostGuiConfiguration {

    static MessagePublisherAdapter createConfiguredHost() {
        var usersCountLimit = new UsersCountLimit(10);
        var snakeOnlineHostStage = SnakeOnlineHostStage.get();
        var hostId = new UserId(UUID.randomUUID().toString());
        var messagePublisher = new MessagePublisherCreator().create();
        var messagePublisherAdapter = new MessagePublisherAdapter(hostId, messagePublisher);
        var hostGuiEventHandler = new GuiHostEventHandlerCreator().create(hostId);
        messagePublisher.subscribe(hostGuiEventHandler.createSubscription());
        new SnakeOnlineHostDependencyConfigurator().configureDependencies(usersCountLimit, messagePublisher);
        setStage(snakeOnlineHostStage, messagePublisherAdapter);
        setControllers(messagePublisherAdapter);
        return messagePublisherAdapter;
    }

    private static void setStage(Stage snakeOnlineHostStage, MessagePublisherAdapter snakeOnlineHost) {
        var keyEventEventHandler = new KeyPressedHandler(snakeOnlineHost::changeSnakeDirection);
        Controllers.get(HostController.class).set(keyEventEventHandler);
        snakeOnlineHostStage.getScene().setOnKeyPressed(keyEventEventHandler);
        snakeOnlineHostStage.setOnCloseRequest(e -> {
            Controllers.get(FleetingMessageController.class).shutdown();
            snakeOnlineHost.shutDownHost();
            SnakeOnlineHostStage.remove();
            GameModeSelectionStage.get().show();
            SnakeOnlineHostStage.remove();
        });
    }

    private static void setControllers(MessagePublisherAdapter snakeOnlineHost) {
        Controllers
                .get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeOnlineHost::changeGameOptions);
        Controllers
                .get(LobbySeatsController.class)
                .onTakeASeatButtonPress(snakeOnlineHost::takeASeat)
                .onFreeUpASeatButtonPress(snakeOnlineHost::freeUpASeat);
        Controllers
                .get(ChatController.class)
                .onSendChatMessageButtonPress(snakeOnlineHost::sendChatMessage);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(snakeOnlineHost::startGame)
                .onCancelButtonPress(snakeOnlineHost::cancelGame)
                .onPauseButtonPress(snakeOnlineHost::pauseGame)
                .onResumeButtonPress(snakeOnlineHost::resumeGame);
    }
}
