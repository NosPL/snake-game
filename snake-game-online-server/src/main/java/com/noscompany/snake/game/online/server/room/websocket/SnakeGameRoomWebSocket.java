package com.noscompany.snake.game.online.server.room.websocket;

import com.noscompany.snake.game.online.server.room.message.handler.RoomMessageHandler;
import com.noscompany.snake.game.online.server.room.message.handler.RoomMessageHandlerCreator;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.*;

import javax.inject.Inject;

import static com.noscompany.snake.game.online.server.room.websocket.SnakeGameRoomWebSocket.URL_PREFIX;


@Slf4j
@ManagedService(path = URL_PREFIX + "{room: [a-zA-Z][a-zA-Z_0-9]*}")
public class SnakeGameRoomWebSocket {
    private static final int CONNECTION_LIMIT = 20;
    public static final String URL_PREFIX = "/room/";
    @PathParam("room")
    private String roomName;
    @Inject
    private BroadcasterFactory broadcasterFactory;
    private RoomMessageHandler roomMessageHandler;

    @Ready
    public void onConnect(AtmosphereResource resource) {
        String userId = resource.uuid();
        log.info("new user connected: {}", userId);
        if (connectionLimitReached()) {
            log.info("connection limit reached for room {}", roomName);
            resource.write("room is full");
            log.info("closing resource with id {}", resource.uuid());
            Try.run(resource::close);
        }
        removeCancelledAtmosphereResources();
    }

    private boolean connectionLimitReached() {
        return broadcaster().getAtmosphereResources().stream().count() >= CONNECTION_LIMIT;
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

    private void removeCancelledAtmosphereResources() {
        Broadcaster broadcaster = broadcaster();
        broadcaster.getAtmosphereResources()
                .stream()
                .filter(AtmosphereResource::isCancelled)
                .forEach(broadcaster::removeAtmosphereResource);
    }

    private RoomMessageHandler getMessageHandler() {
        if (roomMessageHandler == null)
            roomMessageHandler = createHandler();
        return roomMessageHandler;
    }

    private RoomMessageHandler createHandler() {
        return RoomMessageHandlerCreator.create(broadcaster());
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