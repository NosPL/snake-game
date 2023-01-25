package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.host.room.mediator.RoomMediatorForRemoteClients;
import com.noscompany.snake.game.online.host.room.mediator.ports.RoomEventHandlerForRemoteClients;
import com.noscompany.snake.game.online.host.server.dto.ServerParams;
import com.noscompany.snake.game.online.host.server.dto.ServerStartError;
import io.vavr.control.Option;

public interface Server extends RoomEventHandlerForRemoteClients {
    Option<ServerStartError> start(ServerParams serverParams, RoomMediatorForRemoteClients handlerForRemoteClients);
    boolean isRunning();
    void shutdown();
}