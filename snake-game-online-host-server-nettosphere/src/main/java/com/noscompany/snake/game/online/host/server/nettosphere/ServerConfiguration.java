package com.noscompany.snake.game.online.host.server.nettosphere;

import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.IpAddressChecker;
import com.noscompany.snake.game.online.host.server.nettosphere.internal.state.not.started.NotStartedServerState;
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
        return new AtmosphereServer(ipAddressChecker, new NotStartedServerState());
    }
}