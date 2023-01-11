package com.noscompany.snake.game.online.host.pvp.server;

import com.noscompany.snake.game.online.host.pvp.server.internal.state.IpAddressChecker;
import com.noscompany.snake.game.online.host.pvp.server.internal.state.not.started.NotStartedServerState;
import com.noscompany.snake.game.online.host.server.Server;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerConfiguration {

    @Provides
    public Server createServer() {
        return createServer(new HttpAmazonIpAddressChecker());
    }

    public Server createServer(IpAddressChecker ipAddressChecker) {
        return new JavalinServer(ipAddressChecker, new NotStartedServerState());
    }
}