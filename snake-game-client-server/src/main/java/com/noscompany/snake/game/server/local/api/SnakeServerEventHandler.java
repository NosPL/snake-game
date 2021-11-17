package com.noscompany.snake.game.server.local.api;

import com.noscompany.snake.game.commons.messages.events.lobby.LobbyEventHandler;
import snake.game.core.SnakeGameEventHandler;

public interface SnakeServerEventHandler extends LobbyEventHandler, SnakeGameEventHandler {

    void serverStarted();

    void handle(StartingServerError error);

    void handle(ServerError error);

    void serverClosed();
}