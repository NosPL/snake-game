package com.noscompany.snake.game.online.host.server.nettosphere;

import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.not.started.NotStartedServerState;
import com.noscompany.snake.game.online.host.server.Server;
import dagger.Module;
import dagger.Provides;
import io.vavr.control.Option;

@Module
public class ServerConfiguration {

    @Provides
    public Server createServer() {
        return new AtmosphereServer(new NotStartedServerState(),Option.none());
    }
}