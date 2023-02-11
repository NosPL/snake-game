package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.snake.game.online.host.HostEventHandler;
import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.SnakeOnlineHostConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.PlayersLimit;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import snake.game.gameplay.SnakeGameplayConfiguration;
import snake.game.gameplay.SnakeGameplayCreator;

public class SnakeOnlineHostDependencyConfigurator {

    public SnakeOnlineHost snakeOnlineHost(HostEventHandler hostEventHandler, PlayersLimit playersLimit) {
        var server = new ServerConfiguration().createServer();
        var roomCreator = new RoomConfiguration().roomCreator();
        var snakeGameplayCreator = new SnakeGameplayConfiguration().snakeGameplayCreator();
        var roomMediator = new RoomMediatorConfiguration().roomMediator(hostEventHandler, server, roomCreator, playersLimit, snakeGameplayCreator);
        return new SnakeOnlineHostConfiguration().snakeOnlineHost(server, hostEventHandler, roomMediator, roomMediator);
    }
}