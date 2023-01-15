package com.noscompany.snake.game.test.client;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import com.noscompany.snake.game.online.client.SnakeOnlineClient;
import com.noscompany.snake.game.online.client.SnakeOnlineClientConfiguration;
import com.noscompany.snake.game.online.host.server.ServerConfiguration;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediator;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.Server;
import io.vavr.control.Option;

public class SnakeOnlineTestClientConfiguration {

    public SnakeOnlineClient snakeOnlineTestClient(ClientEventHandler clientEventHandler) {
        SnakeOnlineClient snakeOnlineClient = new SnakeOnlineClientConfiguration().create(clientEventHandler);
        Server server = new ServerConfiguration().createServer();
        NullHostEventHandler nullHostEventHandler = new NullHostEventHandler();
        RoomMediator roomMediator = new RoomMediatorConfiguration().roomMediator(nullHostEventHandler, server);
        return new SnakeOnlineTestClient(snakeOnlineClient, clientEventHandler, server, roomMediator, Option.none());
    }
}