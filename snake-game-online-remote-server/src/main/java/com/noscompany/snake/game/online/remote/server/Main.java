package com.noscompany.snake.game.online.remote.server;

import com.noscompany.snake.game.online.host.room.mediator.RoomMediator;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.nettosphere.ServerConfiguration;

import static io.vavr.API.Try;

class Main {
    private static final String HOST = "snake-online.herokuapp.com";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        Server server = new ServerConfiguration().createServer();
        RoomMediator roomMediator = new RoomMediatorConfiguration().roomMediator(new LogHostEventHandler(), server);
        server.start(serverParams(), roomMediator);
    }

    private static ServerParams serverParams() {
        return new ServerParams(HOST, getPort());
    }

    private static Integer getPort() {
        return Try(() -> System.getenv("PORT"))
                .map(Integer::parseInt)
                .getOrElse(DEFAULT_PORT);
    }
}