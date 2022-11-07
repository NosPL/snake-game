package com.noscompany.snake.game.online.host.server.message.handler;

import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.RoomCommandHandlerForRemoteClients;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.AtmosphereResource;

@AllArgsConstructor
@Slf4j
class RoomMessageHandlerImpl2 {
    private final RoomCommandHandlerForRemoteClients roomCommandHandler;
    private final MessageDeserializer messageMapper;

    public void newUserConnected(AtmosphereResource resource) {
        String userId = resource.uuid();
        log.info("new user connected: {}", userId);
    }

    public void handle(AtmosphereResource r, String serializedMessage) {
        RemoteClientId clientId = new RemoteClientId(r.uuid());
        messageMapper
                .deserialize(clientId, serializedMessage)
                .peek(deserializedMessage -> deserializedMessage.applyTo(roomCommandHandler));
    }

    public void disconnect(String userId) {
    }
}