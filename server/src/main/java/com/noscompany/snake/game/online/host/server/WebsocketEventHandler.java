package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.UserId;

public interface WebsocketEventHandler {

    void newClientConnected(UserId remoteClientId);

    void messageReceived(UserId remoteClientId, String message);

    void clientDisconnected(UserId remoteClientId);
}