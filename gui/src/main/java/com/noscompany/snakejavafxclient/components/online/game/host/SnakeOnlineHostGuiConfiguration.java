package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.room.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToFreeUpSeat;
import com.noscompany.snake.game.online.contract.messages.seats.FailedToTakeASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerFreedUpASeat;
import com.noscompany.snake.game.online.contract.messages.seats.PlayerTookASeat;
import com.noscompany.snake.game.online.contract.messages.server.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.ServerStarted;
import com.noscompany.snake.game.online.host.dependency.configurator.SnakeOnlineHostDependencyConfigurator;
import com.noscompany.snakejavafxclient.components.commons.scpr.buttons.ScprButtonsController;
import com.noscompany.snakejavafxclient.components.mode.selection.GameModeSelectionStage;
import com.noscompany.snakejavafxclient.components.online.game.commons.ChatController;
import com.noscompany.snakejavafxclient.components.online.game.commons.KeyPressedHandler;
import com.noscompany.snakejavafxclient.components.online.game.commons.LobbySeatsController;
import com.noscompany.snakejavafxclient.components.online.game.commons.OnlineGameOptionsController;
import com.noscompany.snakejavafxclient.utils.Controllers;
import javafx.stage.Stage;

import java.util.UUID;

 class SnakeOnlineHostGuiConfiguration {

     static MessagePublisherAdapter createConfiguredHost() {
        var usersCountLimit = new UsersCountLimit(10);
        var snakeOnlineHostStage = SnakeOnlineHostStage.get();
        var hostId = new UserId(UUID.randomUUID().toString());
        var guiOnlineHostEventHandler = GuiOnlineHostEventHandler.instance(hostId);
        var messagePublisher = new MessagePublisherCreator().create();
        var messagePublisherAdapter = new MessagePublisherAdapter(hostId, messagePublisher);
        snakeOnlineHostStage.setOnCloseRequest(windowEvent -> messagePublisherAdapter.shutDownHost());
        var subscription = createSubscription(guiOnlineHostEventHandler);
        messagePublisher.subscribe(subscription);
        new SnakeOnlineHostDependencyConfigurator().snakeOnlineHost(usersCountLimit, messagePublisher);
        setStage(snakeOnlineHostStage, messagePublisherAdapter);
        setControllers(messagePublisherAdapter);
        return messagePublisherAdapter;
    }

    private static void setStage(Stage snakeOnlineHostStage, MessagePublisherAdapter snakeOnlineHost) {
        var keyEventEventHandler = new KeyPressedHandler(snakeOnlineHost::changeSnakeDirection);
        Controllers.get(HostController.class).set(keyEventEventHandler);
        snakeOnlineHostStage.getScene().setOnKeyPressed(keyEventEventHandler);
        snakeOnlineHostStage.setOnCloseRequest(e -> {
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

    private static Subscription createSubscription(GuiOnlineHostEventHandler hostEventHandler) {
        return new Subscription()
                .subscriberName("host")
                .toMessage(FailedToStartServer.class, hostEventHandler::failedToStartServer)
                .toMessage(ServerFailedToSendMessageToRemoteClients.class, hostEventHandler::serverFailedToSendMessageToRemoteClient)
                .toMessage(ServerStarted.class, hostEventHandler::serverStarted)
                .toMessage(FailedToEnterRoom.class, hostEventHandler::failedToEnterRoom)
                .toMessage(UserSentChatMessage.class, hostEventHandler::userSentChatMessage)
                .toMessage(GameOptionsChanged.class, hostEventHandler::gameOptionsChanged)
                .toMessage(FailedToSendChatMessage.class, hostEventHandler::failedToSendChatMessage)
                .toMessage(FailedToStartGame.class, hostEventHandler::failedToStartGame)
                .toMessage(FailedToChangeGameOptions.class, hostEventHandler::failedToChangeGameOptions)
                .toMessage(PlayerFreedUpASeat.class, hostEventHandler::playerFreedUpASeat)
                .toMessage(FailedToFreeUpSeat.class, hostEventHandler::failedToFreeUpSeat)
                .toMessage(PlayerTookASeat.class, hostEventHandler::playerTookASeat)
                .toMessage(FailedToTakeASeat.class, hostEventHandler::failedToTakeASeat)
                .toMessage(GameStartCountdown.class, hostEventHandler::gameStartCountdown)
                .toMessage(GameStarted.class, hostEventHandler::gameStarted)
                .toMessage(SnakesMoved.class, hostEventHandler::snakesMoved)
                .toMessage(GameFinished.class, hostEventHandler::gameFinished)
                .toMessage(GameCancelled.class, hostEventHandler::gameCancelled)
                .toMessage(GamePaused.class, hostEventHandler::gamePaused)
                .toMessage(GameResumed.class, hostEventHandler::gameResumed)
                .toMessage(NewUserEnteredRoom.class, hostEventHandler::newUserEnteredRoom)
                .toMessage(UserLeftRoom.class, hostEventHandler::userLeftRoom)
                .toMessage(ServerGotShutdown.class, hostEventHandler::serverGotShutdown);

    }
}