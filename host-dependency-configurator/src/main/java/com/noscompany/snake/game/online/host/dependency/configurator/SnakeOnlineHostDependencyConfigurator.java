package com.noscompany.snake.game.online.host.dependency.configurator;

import com.noscompany.snake.game.online.host.HostEventHandler;
import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.SnakeOnlineHostConfiguration;
import com.noscompany.snake.game.online.contract.messages.room.UsersCountLimit;
import com.noscompany.snake.game.online.host.mediator.MediatorConfiguration;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.websocket.WebsocketConfiguration;
import snake.game.gameplay.GameplayConfiguration;

public class SnakeOnlineHostDependencyConfigurator {

    public SnakeOnlineHost snakeOnlineHost(HostEventHandler hostEventHandler, UsersCountLimit usersCountLimit) {
        var websocketCreator = new WebsocketConfiguration().websocketCreator();
        var server = new ServerConfiguration().server(websocketCreator);
        var roomCreator = new RoomConfiguration().roomCreator();
        var snakeGameplayCreator = new GameplayConfiguration().snakeGameplayCreator();
        var roomMediator = new MediatorConfiguration().mediator(hostEventHandler, server, roomCreator, usersCountLimit, snakeGameplayCreator);
        return new SnakeOnlineHostConfiguration().snakeOnlineHost(server, hostEventHandler, roomMediator, roomMediator);
    }
}