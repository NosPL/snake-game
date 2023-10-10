package com.noscompany.snake.game.utils.monitored.executor.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NamedRunnable implements Runnable{
    private final String taskName;
    private final Runnable task;

    @Override
    public void run() {
        task.run();
    }

    @Override
    public String toString() {
        return taskName;
    }

    public static NamedRunnable namedTask(String taskName, Runnable task) {
        return new NamedRunnable(taskName, task);
    }
}