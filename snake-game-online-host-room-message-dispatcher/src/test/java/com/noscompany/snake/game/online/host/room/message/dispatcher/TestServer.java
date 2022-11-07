package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import io.vavr.control.Option;

public class TestServer implements Server {

    public static TestServer create() {
        throw new RuntimeException();
    }

    @Override
    public Option<StartError> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients) {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        throw new RuntimeException("not implemented");
    }
}