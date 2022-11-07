package com.noscompany.snake.game.online.client;

import com.noscompany.snake.game.online.client.internal.state.not.connected.Disconnected;

public class SnakeOnlineClientCreator {

    public static SnakeOnlineClient createClient(ClientEventHandler eventHandler) {
        return new SnakeOnlineClientImpl(new Disconnected(eventHandler));
    }
}