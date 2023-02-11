package com.noscompany.snake.game.online.host.server.internal.state;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients.SendMessageError;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface ServerState {
    Try<ServerState> start(ServerParams serverParams, RoomApiForRemoteClients handlerForRemoteClients);
    boolean isRunning();
    ServerState shutdown();
    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);
    Option<SendMessageError> sendToClientWithId(RoomApiForRemoteClients.RemoteClientId remoteClientId, OnlineMessage onlineMessage);
}