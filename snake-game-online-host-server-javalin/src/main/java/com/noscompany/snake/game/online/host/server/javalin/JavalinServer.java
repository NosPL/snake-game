package com.noscompany.snake.game.online.host.server.javalin;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.ServerState;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class JavalinServer implements Server, RoomEventHandlerForRemoteClients {
    private ServerState serverState;

    @Override
    public Option<StartError> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients) {
        return serverState
                .start(port, handlerForRemoteClients)
                .onSuccess(serverState -> this.serverState = serverState)
                .toEither().swap().toOption()
                .map(StartError::new);
    }

    @Override
    public void shutdown() {
        serverState = serverState.shutdown();
    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        return serverState.sendToAllClients(onlineMessage);
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        return serverState.sendToClientWithId(remoteClientId, onlineMessage);
    }
}