package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;

import java.util.UUID;

public class SnakeOnlineHostConfiguration {

    public SnakeOnlineHost snakeOnlineHost(Server server,
                                           HostEventHandler hostEventHandler,
                                           RoomApiForHost roomApiForHost,
                                           RoomApiForRemoteClients remoteClients) {
        RoomApiForHost.HostId hostId = new RoomApiForHost.HostId(UUID.randomUUID().toString());
        return new SnakeOnlineHostImpl(hostId, server, hostEventHandler, roomApiForHost, remoteClients);
    }
}