package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.object.mapper.ObjectMapperCreator;
import com.noscompany.snake.game.online.host.server.ports.WebsocketCreator;

public class ServerConfiguration {

    public Server server(WebsocketCreator websocketCreator) {
        return new ServerImpl(websocketCreator, new ClosedWebsocket(), ObjectMapperCreator.createInstance());
    }
}