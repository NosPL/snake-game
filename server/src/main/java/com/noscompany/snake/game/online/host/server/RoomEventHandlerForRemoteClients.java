package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.SendMessageError;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import io.vavr.control.Option;

public interface RoomEventHandlerForRemoteClients {

    Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage);

    Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage);
}