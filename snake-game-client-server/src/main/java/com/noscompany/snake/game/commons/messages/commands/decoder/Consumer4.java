package com.noscompany.snake.game.commons.messages.commands.decoder;

public interface Consumer4<T, Y, U, I> {
    void accept(T t, Y y, U u, I i);
}
