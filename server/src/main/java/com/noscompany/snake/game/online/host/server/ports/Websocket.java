package com.noscompany.snake.game.online.host.server.ports;

import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.dto.SendMessageError;
import io.vavr.control.Option;

public interface Websocket {
    boolean isOpen();

    void close();

    Option<SendMessageError> sendToAllClients(String message);

    Option<SendMessageError> sendToClient(RemoteClientId remoteClientId, String message);
}