package com.noscompany.snakejavafxclient.components.local.game;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.*;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class KeyPressedLatencySimulator implements EventHandler<KeyEvent> {
    private final EventHandler<KeyEvent> eventHandler;
    private final ExecutorService executorService;

    @Override
    public void handle(KeyEvent event) {
        executorService.submit(() -> {
            sleep(0);
            eventHandler.handle(event);
        });
    }

    @SneakyThrows
    private void sleep(int millis) {
        Thread.sleep(millis);
    }

    static EventHandler<KeyEvent> simulateLatencyFor(EventHandler<KeyEvent> eventHandler) {
        return new KeyPressedLatencySimulator(eventHandler, executorService());
    }

    private static ExecutorService executorService() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }
}