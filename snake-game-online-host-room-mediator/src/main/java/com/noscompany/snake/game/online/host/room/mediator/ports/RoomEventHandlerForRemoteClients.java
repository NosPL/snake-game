package com.noscompany.snake.game.online.host.room.mediator.ports;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import io.vavr.control.Option;

public interface RoomEventHandlerForRemoteClients {

    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);

    Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);

    enum SendMessageError {
        SERVER_DIDNT_GET_STARTED,
        SERVER_GOT_SHUTDOWN
    }
}