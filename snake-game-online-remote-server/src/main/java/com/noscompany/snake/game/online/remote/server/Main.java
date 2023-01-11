package com.noscompany.snake.game.online.remote.server;

import com.noscompany.snake.game.online.host.room.mediator.RoomMediator;
import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorConfiguration;
import com.noscompany.snake.game.online.host.server.Server;
import com.noscompany.snake.game.online.host.server.nettosphere.ServerConfiguration;

import static com.noscompany.snake.game.online.remote.server.ServerParamsCreator.toServerParams;

class Main {

    public static void main(String[] args) {
        Server server = new ServerConfiguration().createServer();
        RoomMediator roomMediator = new RoomMediatorConfiguration().roomMediator(new NullHostEventHandler(), server);
        server.start(toServerParams(args), roomMediator);
    }

}