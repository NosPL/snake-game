package com.noscompany.snakejavafxclient.components.online.game.host;

import com.noscompany.snake.game.online.host.SnakeOnlineHost;
import com.noscompany.snake.game.online.host.SnakeOnlineHostConfiguration;
import com.noscompany.snake.game.online.host.room.RoomConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.nettosphere.ServerConfiguration;
import dagger.Component;

@Component(modules = {
        SnakeOnlineHostConfiguration.class,
        RoomMediatorConfiguration.class,
        ServerConfiguration.class,
        RoomConfiguration.class})
public interface HostComponent {
    SnakeOnlineHost snakeOnlineHost();
}