package snake.game.core.runner;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.State.NEW;

@AllArgsConstructor
class GameRunnerImpl implements GameRunner {
    private Thread gameThread;
    private final AtomicBoolean pauseRequested;
    private final AtomicBoolean cancelRequested;

    @Override
    public void start() {
        if (gameThread.getState() == NEW)
            gameThread.start();
    }

    @Override
    public boolean isRunning() {
        return gameThread.isAlive();
    }

    @Override
    public void pause() {
        pauseRequested.set(true);
    }

    @Override
    public void resume() {
        pauseRequested.set(false);
    }

    @Override
    public void cancel() {
        pauseRequested.set(false);
        cancelRequested.set(true);
    }
}