package com.noscompany.snake.game.online.host.server.javalin.internal.state;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface ServerState {

    Try<ServerState> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients);

    boolean isRunning();

    ServerState shutdown();

    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);

    Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);
}