package com.noscompany.snake.game.online.gui.commons;

@FunctionalInterface
public interface Consumer3<T, Y, U> {
    void accept(T t, Y y, U u);
}
