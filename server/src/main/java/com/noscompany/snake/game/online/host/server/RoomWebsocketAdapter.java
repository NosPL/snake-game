package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class RoomWebsocketAdapter implements WebsocketEventHandler {
    private final RoomApiForRemoteClients roomApiForRemoteClients;
    private final MessageDeserializer messageDeserializer;

    @Override
    public void newClientConnected(RemoteClientId remoteClientId) {
        roomApiForRemoteClients.initializeClientState(remoteClientId);
    }

    @Override
    public void messageReceived(RemoteClientId remoteClientId, String message) {
        messageDeserializer
                .deserialize(remoteClientId, message)
                .peek(deserializedMessage -> deserializedMessage.applyTo(roomApiForRemoteClients));
    }

    @Override
    public void clientDisconnected(RemoteClientId remoteClientId) {
        roomApiForRemoteClients.leaveRoom(remoteClientId);
    }
}