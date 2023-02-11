package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import io.vavr.control.Option;

public interface RoomEventHandlerForRemoteClients {

    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);

    Option<SendMessageError> sendToClientWithId(RoomApiForRemoteClients.RemoteClientId remoteClientId, OnlineMessage onlineMessage);

    enum SendMessageError {
        SERVER_DIDNT_GET_STARTED,
        SERVER_GOT_SHUTDOWN,
        FAILED_TO_SENT_MESSAGE,
    }
}