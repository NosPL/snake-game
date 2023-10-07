package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.server.*;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Server {

    Either<FailedToStartServer, ServerStarted> start(ServerParams serverParams, RoomApiForRemoteClients handlerForRemoteClients);

    void shutdown();

    Option<ServerFailedToSendMessageToRemoteClients> sendToAllClients(OnlineMessage onlineMessage);

    Option<ServerFailedToSendMessageToRemoteClients> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);
}