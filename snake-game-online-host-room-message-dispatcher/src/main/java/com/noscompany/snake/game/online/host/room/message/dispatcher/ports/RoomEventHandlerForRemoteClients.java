package com.noscompany.snake.game.online.host.room.message.dispatcher.ports;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import io.vavr.control.Option;

public interface RoomEventHandlerForRemoteClients {

    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);

    Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);

    enum SendMessageError {
        SERVER_IS_NOT_RUNNING;
    }
}