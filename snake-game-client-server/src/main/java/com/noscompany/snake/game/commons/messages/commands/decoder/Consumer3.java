package com.noscompany.snake.game.commons.messages.commands.decoder;

public interface Consumer3<T, Y, U> {
    void accept(T t, Y y, U u);
}
