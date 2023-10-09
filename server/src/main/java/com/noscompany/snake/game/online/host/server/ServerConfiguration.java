package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.message.publisher.Subscription;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.game.options.FailedToChangeGameOptions;
import com.noscompany.snake.game.online.contract.messages.game.options.GameOptionsChanged;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.contract.messages.playground.InitializePlaygroundStateToRemoteClient;
import com.noscompany.snake.game.online.contract.messages.seats.*;
import com.noscompany.snake.game.online.contract.messages.user.registry.FailedToEnterRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.host.HostGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.commands.StartServer;
import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;

public class ServerConfiguration {

    public Server server(WebsocketCreator websocketCreator, MessagePublisher messagePublisher) {
        var server = new ServerImpl(messagePublisher, websocketCreator, new ClosedWebsocket(), ObjectMapperCreator.createInstance());
        var subscription = createSubscription(server);
        messagePublisher.subscribe(subscription);
        return server;
    }

    private Subscription createSubscription(Server server) {
        return new Subscription()
                .subscriberName("server")
//                server commands
                .toMessage(StartServer.class,  (StartServer msg) -> server.start(msg.getServerParams()))
//                host events
                .toMessage(HostGotShutdown.class, (HostGotShutdown msg) -> server.shutdown())
//                user registry events
                .toMessage(NewUserEnteredRoom.class, server::sendToAllClients)
                .toMessage(FailedToEnterRoom.class, (FailedToEnterRoom msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
                .toMessage(UserLeftRoom.class, server::sendToAllClients)
//                chat events
                .toMessage(UserSentChatMessage.class, (UserSentChatMessage msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
                .toMessage(FailedToSendChatMessage.class, (FailedToSendChatMessage msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
//                playground commands
                .toMessage(InitializePlaygroundStateToRemoteClient.class, (InitializePlaygroundStateToRemoteClient msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
//                playground events
                .toMessage(GameOptionsChanged.class, server::sendToAllClients)
                .toMessage(FailedToChangeGameOptions.class, (FailedToChangeGameOptions msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
//                seats commands
                .toMessage(InitializeSeatsToRemoteClient.class, (InitializeSeatsToRemoteClient msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
//                seats events
                .toMessage(PlayerTookASeat.class, server::sendToAllClients)
                .toMessage(FailedToTakeASeat.class, (FailedToTakeASeat msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
                .toMessage(PlayerFreedUpASeat.class, server::sendToAllClients)
                .toMessage(FailedToFreeUpSeat.class, (FailedToFreeUpSeat msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
//                gameplay events
                .toMessage(GameStartCountdown.class, server::sendToAllClients)
                .toMessage(GameStarted.class, server::sendToAllClients)
                .toMessage(FailedToStartGame.class, (FailedToStartGame msg) -> server.sendToClientWithGivenId(msg.getUserId(), msg))
                .toMessage(SnakesMoved.class, server::sendToAllClients)
                .toMessage(GameFinished.class, server::sendToAllClients)
                .toMessage(GameCancelled.class, server::sendToAllClients)
                .toMessage(GamePaused.class, server::sendToAllClients)
                .toMessage(GameResumed.class, server::sendToAllClients);
    }
}