package com.noscompany.snake.game.online.host.pvp.server;

import com.noscompany.snake.game.online.host.pvp.server.internal.state.not.started.NotStartedServerState;
import com.noscompany.snake.game.online.host.server.Server;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerConfiguration {

    @Provides
    public Server createServer() {
        return new JavalinServer(new NotStartedServerState());
    }
}