package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForHost;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.dto.HostId;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import dagger.Module;
import dagger.Provides;

import java.util.UUID;

@Module
public class SnakeOnlineHostConfiguration {

    public SnakeOnlineHost snakeOnlineHost(HostEventHandler hostEventHandler) {
        var server = new ServerConfiguration().createServer();
        var roomMediator = new RoomMediatorConfiguration().roomMediator(hostEventHandler, server);
        return new SnakeOnlineHostConfiguration().snakeOnlineHost(server, hostEventHandler, roomMediator, roomMediator);
    }

    @Provides
    public SnakeOnlineHost snakeOnlineHost(Server server,
                                           HostEventHandler hostEventHandler,
                                           RoomMediatorForHost roomMediatorForHost,
                                           RoomMediatorForRemoteClients remoteClients) {
        HostId hostId = new HostId(UUID.randomUUID().toString());
        return new SnakeOnlineHostImpl(hostId, server, hostEventHandler, roomMediatorForHost, remoteClients);
    }
}