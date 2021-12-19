package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.commons.online.client.ServerEventHandler;
import snake.game.core.SnakeGameEventHandler;

public interface ClientEventHandler extends ServerEventHandler {

    void connectionEstablished();

    void handle(ClientError clientError);

    void handle(StartingClientError startingClientError);

    void connectionClosed();
}