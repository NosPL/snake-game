package com.noscompany.snake.game.online.host.server.javalin.internal.state.not.running;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.ServerState;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.running.RunningServerCreator;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients.*;
import static com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForRemoteClients.SendMessageError.SERVER_IS_NOT_RUNNING;

@AllArgsConstructor
public class NotRunningServerState implements ServerState {

    @Override
    public Try<ServerState> start(int port, RoomCommandHandlerForRemoteClients handlerForRemoteClients) {
        return RunningServerCreator.createRunningServer(port, handlerForRemoteClients);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public ServerState shutdown() {
        return this;
    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        return Option.of(SERVER_IS_NOT_RUNNING);
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        return Option.of(SERVER_IS_NOT_RUNNING);
    }
}