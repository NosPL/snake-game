package com.noscompany.snake.game.online.host.server.nettosphere.internal.state;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface ServerState {
    Try<ServerState> start(ServerParams serverParams, RoomMediatorForRemoteClients handlerForRemoteClients);
    boolean isRunning();
    ServerState shutdown();
    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);
    Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);
}