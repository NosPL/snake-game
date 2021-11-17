package com.noscompany.snake.game.client;

import com.noscompany.snake.game.commons.messages.events.lobby.LobbyEventHandler;
import snake.game.core.SnakeGameEventHandler;

public interface ClientEventHandler extends LobbyEventHandler, SnakeGameEventHandler {

    void connectionEstablished();

    void handle(ClientError clientError);

    void handle(StartingClientError startingClientError);

    void connectionClosed();
}