package com.noscompany.snake.game.online.host.mediator;

import com.noscompany.snake.game.online.host.ports.RoomApiForHost;
import com.noscompany.snake.game.online.host.server.ports.RoomApiForRemoteClients;

public interface Mediator extends RoomApiForHost, RoomApiForRemoteClients {
}