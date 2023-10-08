package com.noscompany.snake.game.online.websocket;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.host.server.dto.RemoteClientId;
import com.noscompany.snake.game.online.host.server.WebsocketEventHandler;
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
    static WebsocketEventHandler websocketEventHandler;

    @Ready
    public void onConnect(AtmosphereResource resource) {
        var remoteClientId = new UserId(resource.uuid());
        websocketEventHandler.newClientConnected(remoteClientId);
    }

    @Message
    public void onMessage(AtmosphereResource resource, String message) {
        var remoteClientId = new UserId(resource.uuid());
        websocketEventHandler.messageReceived(remoteClientId, message);
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent re) {
        var remoteClientId = new UserId(re.getResource().uuid());
        websocketEventHandler.clientDisconnected(remoteClientId);
    }
}