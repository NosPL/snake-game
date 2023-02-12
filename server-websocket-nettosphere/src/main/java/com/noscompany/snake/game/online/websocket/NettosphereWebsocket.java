package com.noscompany.snake.game.online.websocket;

import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.dto.SendMessageError;
import com.noscompany.snake.game.online.host.server.ports.Websocket;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.nettosphere.Nettosphere;

import java.util.Collection;
import java.util.LinkedList;

import static com.noscompany.snake.game.online.host.server.dto.SendMessageError.CONNECTION_ISSUES;

@Slf4j
@AllArgsConstructor
class NettosphereWebsocket implements Websocket {
    private final Nettosphere nettosphere;

    @Override
    public boolean isOpen() {
        return nettosphere.isStarted();
    }

    @Override
    public void close() {
        nettosphere.stop();
    }

    @Override
    public Option<SendMessageError> sendToAllClients(String message) {
        try {
            getBroadcaster().peek(broadcaster -> broadcaster.broadcast(message));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(CONNECTION_ISSUES);
        }
    }

    @Override
    public Option<SendMessageError> sendToClient(RemoteClientId remoteClientId, String message) {
        try {
            findResourceById(remoteClientId)
                    .peek(resource -> send(message, resource));
            return Option.none();
        } catch (Throwable t) {
            return Option.of(CONNECTION_ISSUES);
        }
    }

    private Option<Broadcaster> getBroadcaster() {
        return Option.ofOptional(
                nettosphere
                        .framework()
                        .getBroadcasterFactory()
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

    private Option<AtmosphereResource> findResourceById(RemoteClientId remoteClientId) {
        return Option.ofOptional(
                getAllResources()
                        .stream()
                        .filter(a -> a.uuid().equals(remoteClientId.getId()))
                        .findAny());
    }

    private void send(String serializedMessage, AtmosphereResource r) {
        Try
                .of(() -> r.write(serializedMessage))
                .onFailure(t -> log.warn("Failed to send message to resource with id {}", r.uuid()));
    }
}