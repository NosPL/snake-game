package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.server.InitializeRemoteClientState;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients.RemoteClientId;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;


@Slf4j
@ManagedService(path = "room")
public class SnakeGameRoomWebSocket {
    static RoomApiForRemoteClients roomApiForRemoteClients;
    static RunningServerState runningServerState;
    private final MessageDeserializer messageDeserializer = new MessageDeserializer();

    @Ready
    public void onConnect(AtmosphereResource resource) {
        sendRoomState(resource);
    }

    private void sendRoomState(AtmosphereResource resource) {
        var remoteClientId = new RemoteClientId(resource.uuid());
        var roomState = roomApiForRemoteClients.getRoomState();
        var event = new InitializeRemoteClientState(roomState);
        runningServerState.sendToClientWithId(remoteClientId, event);
    }

    @Message
    public void onMessage(AtmosphereResource resource, String message) {
        log.info("resource id: {}, message: {}", resource.uuid(), message);
        RemoteClientId remoteClientId = new RemoteClientId(resource.uuid());
        messageDeserializer
                .deserialize(remoteClientId, message)
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(roomApiForRemoteClients));
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var userId = re.getResource().uuid();
        log.info("user disconnected: " + userId);
        roomApiForRemoteClients.leaveRoom(new RemoteClientId(userId));
    }
}