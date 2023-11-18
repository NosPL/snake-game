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
import javafx.stage.Stage;

import java.util.function.Function;

public class SnakeOnlineGuiClientConfiguration {

    public void configure(Runnable onCloseAction) {
        configure(
                onCloseAction,
                messagePublisher -> new SnakeOnlineClientConfiguration().create(messagePublisher));
    }


    public void configure(Runnable onCloseAction, Function<MessagePublisher, SnakeOnlineClient> onlineClientCreator) {
        var joinGameStage = JoinGameStage.get();
        var messagePublisher = new MessagePublisherCreator().create();
        var snakeOnlineClient = onlineClientCreator.apply(messagePublisher);
        var snakeOnlineClientStage = SnakeOnlineClientStage.get();
        setControllers(snakeOnlineClient);
        subscribeControllers(messagePublisher);
        configureSnakeOnlineClientStage(snakeOnlineClientStage, onCloseAction, messagePublisher, snakeOnlineClient);
        joinGameStage.setOnCloseRequest(e -> {
            snakeOnlineClient.disconnect();
            messagePublisher.shutdown();
            onCloseAction.run();
        });
        joinGameStage.show();
    }

    private void configureSnakeOnlineClientStage(Stage snakeOnlineClientStage, Runnable onCloseAction, MessagePublisher messagePublisher, SnakeOnlineClient snakeOnlineClient) {
        snakeOnlineClientStage.getScene().setOnKeyPressed(e -> new KeyPressedHandler(snakeOnlineClient::changeSnakeDirection));
        snakeOnlineClientStage.setOnCloseRequest(e -> {
            Controllers.get(FleetingMessageController.class).shutdown();
            snakeOnlineClient.disconnect();
            SnakeOnlineClientStage.remove();
            JoinGameStage.remove();
            messagePublisher.shutdown();
            onCloseAction.run();
        });
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

    }
}