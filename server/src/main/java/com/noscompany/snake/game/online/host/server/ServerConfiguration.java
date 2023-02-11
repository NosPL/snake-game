package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.internal.state.not.started.NotStartedServerState;

public class ServerConfiguration {

    public Server createServer() {
        return new NettosphereServer(new NotStartedServerState());
    }
}