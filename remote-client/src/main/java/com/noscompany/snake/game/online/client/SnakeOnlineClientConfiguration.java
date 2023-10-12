package com.noscompany.snake.game.online.client;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.MessagePublisherCreator;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.ResumeGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.commands.StartGame;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.network.YourIdGotInitialized;
import com.noscompany.snake.game.online.contract.messages.playground.GameReinitialized;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.online.contract.serialization.ObjectTypeMapper;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageSerializer;
import com.noscompany.snake.game.online.online.contract.serialization.object.type.mappers.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SnakeOnlineClientConfiguration {

    public SnakeOnlineClient create(ClientEventHandler eventHandler) {

        var messagePublisher = new MessagePublisherCreator().create();
        var serializer = OnlineMessageSerializer.instance();
        var deserializer = OnlineMessageDeserializer.instance();
        return create(eventHandler, messagePublisher, deserializer,serializer);
    }

    public SnakeOnlineClientImpl create(ClientEventHandler eventHandler,
                                        MessagePublisher messagePublisher, OnlineMessageDeserializer deserializer,
                                        OnlineMessageSerializer serializer) {
        messagePublisher.subscribe(createSubscription(eventHandler));
        return new SnakeOnlineClientImpl(new Disconnected(messagePublisher, deserializer, serializer));
    }

    private Subscription createSubscription(ClientEventHandler eventHandler) {
        return new Subscription()
                .toMessage(YourIdGotInitialized.class, (YourIdGotInitialized event) -> UserIdHolder.set(event.getUserId()))
//                chat events
                .toMessage(UserSentChatMessage.class, eventHandler::userSentChatMessage)
                .toMessage(FailedToSendChatMessage.class, eventHandler::failedToSendChatMessage)

//                playground
                .toMessage(InitializePlaygroundToRemoteClient.class, eventHandler::initializePlayground)
                .toMessage(GameReinitialized.class, eventHandler::gameReinitialized)
                .toMessage(GameOptionsChanged.class, eventHandler::gameOptionsChanged)
                .toMessage(FailedToChangeGameOptions.class, eventHandler::failedToChangeGameOptions)

//                gameplay
                .toMessage(GameStarted.class, eventHandler::gameStarted)
                .toMessage(FailedToStartGame.class, eventHandler::failedToStartGame)
                .toMessage(GameCancelled.class, eventHandler::gameCancelled)
                .toMessage(FailedToCancelGame.class, eventHandler::failedToCancelGame)
                .toMessage(GamePaused.class, eventHandler::gamePaused)
                .toMessage(FailedToPauseGame.class, eventHandler::failedToPauseGame)
                .toMessage(GameResumed.class, eventHandler::gameResumed)
                .toMessage(FailedToResumeGame.class, eventHandler::failedToResumeGame)
                .toMessage(GameStartCountdown.class, eventHandler::gameStartCountdown)
                .toMessage(SnakesMoved.class, eventHandler::snakesMoved)
                .toMessage(FailedToResumeGame.class, eventHandler::failedToResumeGame)

//                seats
                .toMessage(InitializeSeatsToRemoteClient.class, eventHandler::initializeSeats)
                .toMessage(PlayerTookASeat.class, eventHandler::playerTookASeat)
                .toMessage(FailedToTakeASeat.class, eventHandler::failedToTakeASeat)
                .toMessage(PlayerFreedUpASeat.class, eventHandler::playerFreedUpASeat)
                .toMessage(FailedToFreeUpSeat.class, eventHandler::failedToFreeUpASeat)

//                server
                .toMessage(ServerGotShutdown.class, eventHandler::serverGotShutdown)

//                user registry
                .toMessage(NewUserEnteredRoom.class, eventHandler::newUserEnteredRoom)
                .toMessage(FailedToEnterRoom.class, eventHandler::failedToEnterRoom)
                .subscriberName("remote-client");
    }
}