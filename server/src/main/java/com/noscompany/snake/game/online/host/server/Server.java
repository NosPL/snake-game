package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import io.vavr.control.Option;

public interface Server extends RoomEventHandlerForRemoteClients {
    Option<ServerStartError> start(ServerParams serverParams, RoomApiForRemoteClients handlerForRemoteClients);
    boolean isRunning();
    void shutdown();
}