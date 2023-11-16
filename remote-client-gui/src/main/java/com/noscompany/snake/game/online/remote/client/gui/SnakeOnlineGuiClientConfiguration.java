package com.noscompany.snake.game.online.remote.client.gui;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.snake.game.online.chat.gui.ChatController;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.failure.message.gui.FleetingMessageController;
import com.noscompany.snake.game.online.game.options.gui.OnlineGameOptionsController;
import com.noscompany.snake.game.online.gameplay.gui.buttons.ScprButtonsController;
import com.noscompany.snake.game.online.gui.commons.AbstractController;
import com.noscompany.snake.game.online.gui.commons.Controllers;
import com.noscompany.snake.game.online.gui.commons.KeyPressedHandler;
import com.noscompany.snake.game.online.seats.gui.SeatsController;

import java.util.function.Function;

public class SnakeOnlineGuiClientConfiguration {

    public void configure(Runnable onCloseAction) {
        configure(
                onCloseAction,
                messagePublisher -> new SnakeOnlineClientConfiguration().create(messagePublisher));
    }


    public void configure(Runnable onCloseAction, Function<MessagePublisher, SnakeOnlineClient> onlineClientCreator) {
        var joinGameStage = JoinGameStage.get();
        joinGameStage.setOnCloseRequest(e -> {
            Controllers.get(JoinGameController.class).disconnect();
            onCloseAction.run();
        });
        var snakeOnlineClientStage = SnakeOnlineClientStage.get();
        var messagePublisher = new MessagePublisherCreator().create();
        var snakeOnlineClient = onlineClientCreator.apply(messagePublisher);
        snakeOnlineClientStage.getScene()
                .setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        snakeOnlineClientStage.setOnCloseRequest(e -> {
            Controllers.get(OnlineClientController.class).disconnectClient();
            SnakeOnlineClientStage.remove();
            JoinGameStage.remove();
            snakeOnlineClient.disconnect();
            onCloseAction.run();
        });
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