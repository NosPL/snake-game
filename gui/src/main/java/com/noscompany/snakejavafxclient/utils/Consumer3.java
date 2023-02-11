package com.noscompany.snakejavafxclient.utils;

@FunctionalInterface
public interface Consumer3<T, Y, U> {
    void accept(T t, Y y, U u);
}
