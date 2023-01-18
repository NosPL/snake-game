package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameOptions;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.room.RoomState;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.RemoteClientId;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;


@Slf4j
@ManagedService(path = "room")
public class SnakeGameRoomWebSocket {
    static RoomMediatorForRemoteClients roomMediatorForRemoteClients = new NullObject();
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
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(roomMediatorForRemoteClients));
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var userId = re.getResource().uuid();
        log.info("user disconnected: " + userId);
        roomMediatorForRemoteClients.removeClient(new RemoteClientId(userId));
    }

    private static class NullObject implements RoomMediatorForRemoteClients {

        @Override
        public void sendChatMessage(RemoteClientId remoteClientId, String messageContent) {

        }

        @Override
        public void cancelGame(RemoteClientId remoteClientId) {

        }

        @Override
        public void changeDirection(RemoteClientId remoteClientId, Direction direction) {

        }

        @Override
        public void pauseGame(RemoteClientId remoteClientId) {

        }

        @Override
        public void resumeGame(RemoteClientId remoteClientId) {

        }

        @Override
        public void startGame(RemoteClientId remoteClientId) {

        }

        @Override
        public void changeGameOptions(RemoteClientId remoteClientId, GameOptions gameOptions) {

        }

        @Override
        public void freeUpSeat(RemoteClientId remoteClientId) {

        }

        @Override
        public void takeASeat(RemoteClientId remoteClientId, PlayerNumber playerNumber) {

        }

        @Override
        public void enterRoom(RemoteClientId remoteClientId, String userName) {

        }

        @Override
        public void removeClient(RemoteClientId remoteClientId) {

        }

        @Override
        public RoomState getRoomState() {
            return null;
        }
    }
}