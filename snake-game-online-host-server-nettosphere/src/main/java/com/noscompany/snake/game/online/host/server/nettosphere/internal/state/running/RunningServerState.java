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
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.nettosphere.Nettosphere;

import static com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError.FAILED_TO_SENT_MESSAGE;

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

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            remoteClientsBroadcaster
                    .getAtmosphereResources()
                    .forEach(r -> send(serializedMessage, r));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SENT_MESSAGE);
        }
    }

    private void send(String serializedMessage, AtmosphereResource r) {
        r.write(serializedMessage);
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            remoteClientsBroadcaster
                    .getAtmosphereResources()
                    .stream()
                    .filter(a -> a.uuid().equals(remoteClientId.getId()))
                    .findAny()
                    .ifPresent(a -> send(serializedMessage, a));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SENT_MESSAGE);
        }
    }

}