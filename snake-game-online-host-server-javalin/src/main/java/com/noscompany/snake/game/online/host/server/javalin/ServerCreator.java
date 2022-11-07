package com.noscompany.snake.game.online.host.server.javalin;

import com.noscompany.snake.game.online.host.room.message.dispatcher.ports.Server;
import com.noscompany.snake.game.online.host.server.javalin.internal.state.not.running.NotRunningServerState;

public class ServerCreator {

    public static Server create() {
        return new JavalinServer(new NotRunningServerState());
    }
}