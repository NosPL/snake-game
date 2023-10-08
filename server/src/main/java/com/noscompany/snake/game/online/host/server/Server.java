package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.ServerParams;
import com.noscompany.snake.game.online.contract.messages.server.commands.StartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.FailedToStartServer;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerGotShutdown;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerStarted;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface Server {

    Either<FailedToStartServer, ServerStarted> start(ServerParams serverParams);

    Option<ServerFailedToSendMessageToRemoteClients> sendToAllClients(OnlineMessage onlineMessage);

    Option<ServerFailedToSendMessageToRemoteClients> sendToClientWithGivenId(UserId remoteClientId, OnlineMessage onlineMessage);

    ServerGotShutdown shutdown();
}