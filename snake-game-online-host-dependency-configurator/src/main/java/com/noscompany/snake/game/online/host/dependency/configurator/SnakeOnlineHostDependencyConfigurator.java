package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.snake.game.online.host.HostEventHandler;
import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.SnakeOnlineHostConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;

public class SnakeOnlineHostDependencyConfigurator {

    public SnakeOnlineHost snakeOnlineHost(HostEventHandler hostEventHandler, PlayersLimit playersLimit) {
        var server = new ServerConfiguration().createServer();
        var roomCreator = new RoomConfiguration().roomCreator();
        var roomMediator = new RoomMediatorConfiguration().roomMediator(hostEventHandler, server, roomCreator, playersLimit);
        return new SnakeOnlineHostConfiguration().snakeOnlineHost(server, hostEventHandler, roomMediator, roomMediator);
    }
}