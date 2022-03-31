package snake.game.core.internal.runner;

public interface GameRunner {

    void start();

    void cancel();

    void pause();

    void resume();

    boolean isRunning();
}
