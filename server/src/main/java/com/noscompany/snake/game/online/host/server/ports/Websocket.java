package com.noscompany.snake.game.online.host.server.ports;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import io.vavr.control.Option;

public interface Websocket {

    boolean isOpen();

    void close();

    Option<ServerFailedToSendMessageToRemoteClients> sendToAllClients(String message);

    Option<ServerFailedToSendMessageToRemoteClients> sendToClient(UserId remoteClientId, String message);
}