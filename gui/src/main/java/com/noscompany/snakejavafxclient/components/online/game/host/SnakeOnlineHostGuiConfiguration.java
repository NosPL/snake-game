package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.chat.gui.ChatController;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.host.dependency.configurator.SnakeOnlineHostDependencyConfigurator;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineHostGuiConfiguration {

    public void configure() {
        SetupHostStage.get();
        var snakeOnlineHostStage = SnakeOnlineHostStage.get();
        var messagePublisher = new MessagePublisherCreator().create();
        var hostId = UserId.random();
        new SnakeOnlineHostDependencyConfigurator().configureDependencies(messagePublisher);
        var msgPubAdapter = new MessagePublisherAdapter(hostId, messagePublisher);
        setStage(snakeOnlineHostStage, msgPubAdapter);
        setActionsInControllers(msgPubAdapter);
        subscribeControllers(messagePublisher);
        SetupHostStage.get().show();
    }

    private void subscribeControllers(MessagePublisher messagePublisher) {
        Controllers.getAll().stream().map(AbstractController::getSubscription).forEach(messagePublisher::subscribe);
    }

    private void setStage(Stage snakeOnlineHostStage, MessagePublisherAdapter publisherAdapter) {
        var keyEventEventHandler = new KeyPressedHandler(publisherAdapter::changeSnakeDirection);
        Controllers.get(HostController.class).set(keyEventEventHandler);
        snakeOnlineHostStage.getScene().setOnKeyPressed(keyEventEventHandler);
        snakeOnlineHostStage.setOnCloseRequest(e -> {
            Controllers.get(FleetingMessageController.class).shutdown();
            publisherAdapter.shutDownHost();
            SnakeOnlineHostStage.remove();
            GameModeSelectionStage.get().show();
            SnakeOnlineHostStage.remove();
        });
    }

    private void setActionsInControllers(MessagePublisherAdapter publisherAdapter) {
        Controllers
                .get(SetupHostController.class)
                .configure(publisherAdapter::startServer, publisherAdapter::enterRoom);
        Controllers
                .get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(publisherAdapter::changeGameOptions);
        Controllers
                .get(SeatsController.class)
                .onTakeASeatButtonPress(publisherAdapter::takeASeat)
                .onFreeUpASeatButtonPress(publisherAdapter::freeUpASeat);
        Controllers
                .get(ChatController.class)
                .onSendChatMessageButtonPress(publisherAdapter::sendChatMessage);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(publisherAdapter::startGame)
                .onCancelButtonPress(publisherAdapter::cancelGame)
                .onPauseButtonPress(publisherAdapter::pauseGame)
                .onResumeButtonPress(publisherAdapter::resumeGame);
    }
}
