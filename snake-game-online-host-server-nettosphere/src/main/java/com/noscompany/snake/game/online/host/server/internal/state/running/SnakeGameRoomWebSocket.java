package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;


@Slf4j
@ManagedService(path = "room")
public class SnakeGameRoomWebSocket {
    private final MessageDeserializer messageDeserializer = new MessageDeserializer();

    @Ready
    public void onConnect(AtmosphereResource resource) {
        log.info("new resource connected, id: {}", resource.uuid());
    }

    @Message
    public void onMessage(AtmosphereResource resource, String message) {
        log.info("resource id: {}, message: {}", resource.uuid(), message);
        RemoteClientId remoteClientId = new RemoteClientId(resource.uuid());
        messageDeserializer
                .deserialize(remoteClientId, message)
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(RoomMediatorHolder.roomMediatorForRemoteClients));
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var userId = re.getResource().uuid();
        log.info("user disconnected: " + userId);
        RoomMediatorHolder.roomMediatorForRemoteClients.removeClient(new RemoteClientId(userId));
    }
}