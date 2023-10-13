package com.noscompany.snakejavafxclient.components.online.game.client;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.test.client.SnakeOnlineTestClientConfiguration;
import com.noscompany.snakejavafxclient.ApplicationProfile;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.*;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import javafx.stage.Stage;

public class SnakeOnlineGuiClientConfiguration {

    public void configure() {
        var joinGameStage = JoinGameStage.get();
        var snakeOnlineClientStage = SnakeOnlineClientStage.get();
        var messagePublisher = new MessagePublisherCreator().create();
        var snakeOnlineClient = getSnakeOnlineClient(messagePublisher);
        snakeOnlineClientStage
                .getScene()
                .setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        setControllers(snakeOnlineClient);
        subscribeControllers(messagePublisher);
        joinGameStage.show();
    }

    private void subscribeControllers(MessagePublisher messagePublisher) {
        Controllers
                .getAll()
                .stream()
                .map(AbstractController::getSubscription)
                .forEach(messagePublisher::subscribe);
    }

    private SnakeOnlineClient getSnakeOnlineClient(MessagePublisher messagePublisher) {
        if (ApplicationProfile.profile == ApplicationProfile.Profile.PROD)
            return new SnakeOnlineClientConfiguration().create(messagePublisher);
        else
            return new SnakeOnlineTestClientConfiguration().snakeOnlineTestClient();
    }

    private static void setControllers(SnakeOnlineClient snakeOnlineClient) {
        Controllers
                .get(JoinGameController.class)
                .setSnakeOnlineClient(snakeOnlineClient);
        Controllers
                .get(OnlineGameOptionsController.class)
                .onGameOptionsChanged(snakeOnlineClient::changeGameOptions);
        Controllers
                .get(SeatsController.class)
                .onTakeASeatButtonPress(snakeOnlineClient::takeASeat)
                .onFreeUpASeatButtonPress(snakeOnlineClient::freeUpASeat);
        Controllers
                .get(ChatController.class)
                .onSendChatMessageButtonPress(snakeOnlineClient::sendChatMessage);
        Controllers
                .get(ScprButtonsController.class)
                .onStartButtonPress(snakeOnlineClient::startGame)
                .onCancelButtonPress(snakeOnlineClient::cancelGame)
                .onPauseButtonPress(snakeOnlineClient::pauseGame)
                .onResumeButtonPress(snakeOnlineClient::resumeGame);
        Controllers
                .get(OnlineClientController.class)
                .setSnakeOnlineClient(snakeOnlineClient);
        SnakeOnlineClientStage
                .get()
                .setOnCloseRequest(w -> Controllers.get(FleetingMessageController.class).shutdown());
    }
}