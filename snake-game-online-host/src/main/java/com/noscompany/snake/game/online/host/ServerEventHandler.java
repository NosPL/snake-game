package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.host.server.dto.IpAddress;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import io.vavr.control.Try;

public interface ServerEventHandler {
    void update(Try<IpAddress> ipAddressResult);
    void handle(ServerStartError serverStartError);
    void failedToExecuteActionBecauseServerIsNotRunning();

    void serverStarted();
}