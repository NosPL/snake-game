package com.noscompany.snake.game.online.host;

import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;

public interface HostEventHandler extends RoomEventHandlerForHost {
    void handle(ServerStartError serverStartError);
    void failedToExecuteActionBecauseServerIsNotRunning();
    void serverStarted(ServerParams serverParams);
}