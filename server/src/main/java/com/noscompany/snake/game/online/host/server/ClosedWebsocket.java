package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.dto.SendMessageError;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import io.vavr.control.Option;

import static com.noscompany.snake.game.online.host.server.dto.SendMessageError.SERVER_IS_NOT_STARTED;

class ClosedWebsocket implements Websocket {

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public Option<SendMessageError> sendToAllClients(String message) {
        return Option.of(SERVER_IS_NOT_STARTED);
    }

    @Override
    public Option<SendMessageError> sendToClient(RemoteClientId remoteClientId, String message) {
        return Option.of(SERVER_IS_NOT_STARTED);
    }
}