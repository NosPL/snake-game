package com.noscompany.snake.game.online.host.server.websocket;

import com.noscompany.snake.game.online.host.server.message.handler.RoomMessageHandler;
import com.noscompany.snake.game.online.host.server.message.handler.RoomMessageHandlerCreator;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.*;

import javax.inject.Inject;

import static com.noscompany.snake.game.online.host.server.websocket.SnakeGameRoomWebSocket.URL_PREFIX;


@Slf4j
@ManagedService(path = URL_PREFIX + "{room: [a-zA-Z][a-zA-Z_0-9]*}")
public class SnakeGameRoomWebSocket {
    public static final String URL_PREFIX = "/room/";
    @PathParam("room")
    private String roomName;
    @Inject
    private BroadcasterFactory broadcasterFactory;
    private RoomMessageHandler roomMessageHandler;

    @Ready
    public void onConnect(AtmosphereResource resource) {
        log.info("new resource connected, id: {}", resource.uuid());
        getMessageHandler().newUserConnected(resource);
    }

    @Message
    public void onMessage(AtmosphereResource resource, String message) {
        log.info("resource id: {}, message: {}", resource.uuid(), message);
        getMessageHandler().handle(resource, message);
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var userId = re.getResource().uuid();
        log.info("user disconnected: " + userId);
        getMessageHandler().removeUserById(userId);
    }

    private RoomMessageHandler getMessageHandler() {
        if (roomMessageHandler == null)
            roomMessageHandler = RoomMessageHandlerCreator.create(broadcaster());
        return roomMessageHandler;
    }

    private Broadcaster broadcaster() {
        Broadcaster broadcaster = broadcasterFactory.lookup(broadcasterName());
        if (broadcaster == null) {
            log.error("cannot find broadcaster with name: {}", broadcasterName());
            throw new RuntimeException("cannot find broadcaster");
        }
        return broadcaster;
    }

    private String broadcasterName() {
        return URL_PREFIX + roomName;
    }
}