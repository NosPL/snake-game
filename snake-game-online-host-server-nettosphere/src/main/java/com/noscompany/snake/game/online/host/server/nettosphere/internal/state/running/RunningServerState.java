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
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.nettosphere.Nettosphere;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients.SendMessageError.FAILED_TO_SENT_MESSAGE;

@Slf4j
@AllArgsConstructor
class RunningServerState implements ServerState {
    private final Nettosphere nettosphere;
    private final BroadcasterFactory broadcasterFactory;
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
        getBroadcaster()
                .peek(broadcaster -> broadcaster.broadcast(new ServerGotShutdown()));
        nettosphere.stop();
        return new ShutdownServerState();
    }

    @Override
    public Option<SendMessageError> sendToAllClients(OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            getAllResources()
                    .forEach(r -> send(serializedMessage, r));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SENT_MESSAGE);
        }
    }

    @Override
    public Option<SendMessageError> sendToClientWithId(RemoteClientId remoteClientId, OnlineMessage onlineMessage) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(onlineMessage);
            getAllResources()
                    .stream()
                    .filter(a -> a.uuid().equals(remoteClientId.getId()))
                    .findAny()
                    .ifPresent(a -> send(serializedMessage, a));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(FAILED_TO_SENT_MESSAGE);
        }
    }

    private void send(String serializedMessage, AtmosphereResource r) {
        try {
            r.write(serializedMessage);
        } catch (Throwable t) {
            log.warn("Failed to send message to resource with id {}", r.uuid());
        }
    }

    private Option<Broadcaster> getBroadcaster() {
        return Option.ofOptional(broadcasterFactory
                .lookupAll()
                .stream()
                .filter(broadcaster -> broadcaster.getID().equals("room"))
                .findAny());
    }

    private Collection<AtmosphereResource> getAllResources() {
        return getBroadcaster()
                .map(Broadcaster::getAtmosphereResources)
                .getOrElse(new LinkedList<>());
    }
}