package com.noscompany.snake.game.online.host.pvp.server.internal.state.not.started;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.pvp.server.internal.state.running.RunningServerCreator;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import com.noscompany.snake.game.online.host.pvp.server.internal.state.ServerState;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

import static com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError.SERVER_DIDNT_GET_STARTED;

@AllArgsConstructor
public class NotStartedServerState implements ServerState {

    @Override
    public Try<ServerState> start(ServerParams serverParams, RoomMediatorForRemoteClients handlerForRemoteClients) {
        return RunningServerCreator.createRunningServer(serverParams, handlerForRemoteClients);
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
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        return Option.of(SERVER_DIDNT_GET_STARTED);
    }

}