package snake.game.gameplay.internal.runner;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.State.NEW;

@AllArgsConstructor
public class GameRunner {
    private Thread gameThread;
    private final AtomicBoolean pauseRequested;
    private final AtomicBoolean cancelRequested;

    public synchronized void start() {
        if (gameThread.getState() == NEW)
            gameThread.start();
    }

    public boolean isRunning() {
        return gameThread.isAlive();
    }

    public void pause() {
        pauseRequested.set(true);
    }

    public void resume() {
        pauseRequested.set(false);
    }

    public void cancel() {
        pauseRequested.set(false);
        cancelRequested.set(true);
    }
}