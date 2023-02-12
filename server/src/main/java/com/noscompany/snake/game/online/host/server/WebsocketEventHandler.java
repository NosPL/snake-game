package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;

public interface WebsocketEventHandler {

    void newClientConnected(RemoteClientId remoteClientId);

    void messageReceived(RemoteClientId remoteClientId, String message);

    void clientDisconnected(RemoteClientId remoteClientId);
}