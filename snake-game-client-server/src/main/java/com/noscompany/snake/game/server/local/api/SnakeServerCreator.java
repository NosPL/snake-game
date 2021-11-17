package com.noscompany.snake.game.server.local.api;

public class SnakeServerCreator {

    public static SnakeServer instance(SnakeServerEventHandler eventHandler) {
        return new SnakeServerImpl(new NotRunningServerState(eventHandler));
    }
}