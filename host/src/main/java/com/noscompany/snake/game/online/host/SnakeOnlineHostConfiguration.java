package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;

import java.util.UUID;

public class SnakeOnlineHostConfiguration {

    public SnakeOnlineHost snakeOnlineHost(Server server,
                                           HostEventHandler hostEventHandler,
                                           RoomApiForHost roomApiForHost,
                                           RoomApiForRemoteClients remoteClients) {
        var hostId = new UserId(UUID.randomUUID().toString());
        var decoratedEventHandler = new CheckIfHostIsRecipientOfFailureMessage(hostId, hostEventHandler);
        return new SnakeOnlineHostImpl(hostId, server, decoratedEventHandler, roomApiForHost, remoteClients);
    }
}