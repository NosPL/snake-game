package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;

public class SnakeOnlineClientConfiguration {

    public SnakeOnlineClient create(ClientEventHandler eventHandler) {
        return new SnakeOnlineClientImpl(new Disconnected(eventHandler));
    }
}