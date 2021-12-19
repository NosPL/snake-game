package com.noscompany.snake.game.online.server.websocket;

import com.noscompany.snake.game.online.server.message.handler.MessageHandler;
import com.noscompany.snake.game.online.server.message.handler.MessageHandlerCreator;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import javax.inject.Inject;

import static org.atmosphere.config.service.DeliverTo.DELIVER_TO.BROADCASTER;
import static org.atmosphere.config.service.DeliverTo.DELIVER_TO.RESOURCE;


@Slf4j
@ManagedService(path = UrlConstant.WEBSOCKET_URL + "{room: [0-9][0-9]}")
public class SnakeGameRoomWebSocket {
    @PathParam("room")
    private String roomName;
    @Inject
    private BroadcasterFactory broadcasterFactory;
    private MessageHandler messageHandler;

    @Ready
    @DeliverTo(RESOURCE)
    public void onConnect(AtmosphereResource r) {
        String userId = r.uuid();
        log.info("new user connected: {}", userId);
    }

    @Message
    @DeliverTo(BROADCASTER)
    public void onMessage(AtmosphereResource resource, String message) {
        log.info("resource id: {}, message: {}", resource.uuid(), message);
        getMessageHandler().handle(resource, message);
    }

    @Disconnect
    @DeliverTo(BROADCASTER)
    public void onDisconnect(AtmosphereResourceEvent re) {
        var userId = re.getResource().uuid();
        log.info("user disconnected: " + userId);
        getMessageHandler().userDisconnected(userId);
    }

    private MessageHandler getMessageHandler() {
        if (messageHandler == null)
            messageHandler = createHandler();
        return messageHandler;
    }

    private MessageHandler createHandler() {
        return MessageHandlerCreator.create(broadcaster());
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
        return UrlConstant.WEBSOCKET_URL + roomName;
    }
}