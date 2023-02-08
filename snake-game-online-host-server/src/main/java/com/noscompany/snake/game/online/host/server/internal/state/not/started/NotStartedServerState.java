package com.noscompany.snake.game.online.host.server.internal.state.not.started;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.internal.state.running.RunningServerStateCreator;
import com.noscompany.snake.game.online.host.server.internal.state.ServerState;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients.SendMessageError;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.host.server.RoomEventHandlerForRemoteClients.SendMessageError.SERVER_DIDNT_GET_STARTED;

@AllArgsConstructor
public class NotStartedServerState implements ServerState {

    @Override
    public Try<ServerState> start(ServerParams serverParams, RoomApiForRemoteClients handlerForRemoteClients) {
        return RunningServerStateCreator.createRunningServer(serverParams, handlerForRemoteClients);
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
        return Option.of(SERVER_DIDNT_GET_STARTED);
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RoomApiForRemoteClients.RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        return Option.of(SERVER_DIDNT_GET_STARTED);
    }

}