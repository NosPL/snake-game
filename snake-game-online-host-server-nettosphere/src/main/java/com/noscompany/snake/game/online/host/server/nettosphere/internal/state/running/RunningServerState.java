package com.noscompany.snake.game.online.host.server.nettosphere.internal.state.running;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.server.ServerGotShutdown;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError;
import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.shutdown.ShutdownServerState;
import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.ServerState;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.nettosphere.Nettosphere;

@AllArgsConstructor
class RunningServerState implements ServerState {
    private final Nettosphere nettosphere;
    private final Broadcaster remoteClientsBroadcaster;
    private final ObjectMapper objectMapper;

    @Override
    public Try<ServerState> start(ServerParams serverParams, RoomMediatorForRemoteClients roomMediatorForRemoteClients) {
        if (nettosphere.isStarted())
            return Try.success(this);
        else
            return RunningServerStateCreator.createRunningServer(serverParams, roomMediatorForRemoteClients);
    }

    @Override
    public boolean isRunning() {
        return nettosphere.isStarted();
    }

    @Override
    public ServerState shutdown() {
        remoteClientsBroadcaster.broadcast(new ServerGotShutdown());
        nettosphere.stop();
        return new ShutdownServerState();
    }

    @SneakyThrows
    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
        remoteClientsBroadcaster
                .getAtmosphereResources()
                .forEach(r -> r.write(serializedMessage));
        return Option.none();
    }

    @SneakyThrows
    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
        remoteClientsBroadcaster
                .getAtmosphereResources()
                .stream()
                .filter(a -> a.uuid().equals(remoteClientId.getId()))
                .findAny()
                .ifPresent(a -> a.write(serializedMessage));
        return Option.none();
    }

}