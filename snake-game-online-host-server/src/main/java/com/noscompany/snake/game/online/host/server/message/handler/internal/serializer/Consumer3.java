package com.noscompany.snake.game.online.host.server.message.handler.internal.serializer;

@FunctionalInterface
public interface Consumer3<T, Y, U> {
    void accept(T t, Y y, U u);
}
