package com.noscompany.snake.game.online.server.message.handler;

@FunctionalInterface
public interface Consumer3<T, Y, U> {
    void accept(T t, Y y, U u);
}
