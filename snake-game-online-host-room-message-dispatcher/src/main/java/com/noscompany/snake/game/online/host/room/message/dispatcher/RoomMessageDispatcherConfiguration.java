package com.noscompany.snake.game.online.host.room.message.dispatcher;

import com.noscompany.snake.game.online.host.room.RoomCreator;
import com.noscompany.snake.game.online.host.room.message.dispatcher.dto.HostId;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.RoomEventHandlerForHost;
import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;

import java.util.UUID;

public class RoomMessageDispatcherConfiguration {

    public RoomCommandHandlerForHost roomCommandHandlerForHost(String hostName, Server server, RoomEventHandlerForHost roomEventHandlerForHost) {
        var roomEventDispatcher = new RoomEventDispatcher(roomEventHandlerForHost, server);
        var room = RoomCreator.create(roomEventDispatcher);
        var hostId = new HostId(UUID.randomUUID().toString());
        var result = room.enter(hostId.getId(), hostName);
        assert result.isRight();
        return new RoomCommandHandler(hostId, room, server, roomEventDispatcher);
    }
}