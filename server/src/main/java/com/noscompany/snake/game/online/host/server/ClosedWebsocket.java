package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import io.vavr.control.Option;

import static com.noscompany.snake.game.online.contract.messages.server.events.ServerFailedToSendMessageToRemoteClients.Reason.SERVER_IS_NOT_STARTED;

class ClosedWebsocket implements Websocket {

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendToAllClients(String message) {
        return Option.of(new ServerFailedToSendMessageToRemoteClients(SERVER_IS_NOT_STARTED));
    }

    @Override
    public Option<ServerFailedToSendMessageToRemoteClients> sendToClient(UserId remoteClientId, String message) {
        return Option.of(new ServerFailedToSendMessageToRemoteClients(SERVER_IS_NOT_STARTED));
    }
}