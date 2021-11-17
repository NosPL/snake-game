package com.noscompany.snake.game.server;

import com.noscompany.snake.game.commons.messages.events.lobby.LobbyEventHandler;
import snake.game.core.SnakeGameEventHandler;

public interface SnakeServerEventHandler extends LobbyEventHandler, SnakeGameEventHandler {

    void handle(StartingServerError error);

    void handle(ServerError error);

    void serverClosed();
}
