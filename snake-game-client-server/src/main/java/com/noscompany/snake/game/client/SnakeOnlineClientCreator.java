package com.noscompany.snake.game.client;

public class SnakeOnlineClientCreator {

    public static SnakeOnlineClient createClient(ClientEventHandler eventHandler) {
        return new SnakeOnlineClientImpl(new NotConnectedClient(eventHandler));
    }
}
