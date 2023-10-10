package com.noscompany.snake.game.utils.monitored.executor.service;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class NamedCallable<V> implements Callable<V> {
    private final String taskName;
    private final Callable<V> callable;

    public static <T> NamedCallable<T> namedTask(String taskName, Callable<T> task) {
        return new NamedCallable<>(taskName, task);
    }

    @Override
    public V call() throws Exception {
        return callable.call();
    }

    @Override
    public String toString() {
        return taskName;
    }
}