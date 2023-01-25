package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.internal.state.not.started.NotStartedServerState;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerConfiguration {

    @Provides
    public Server createServer() {
        return new NettosphereServer(new NotStartedServerState());
    }
}