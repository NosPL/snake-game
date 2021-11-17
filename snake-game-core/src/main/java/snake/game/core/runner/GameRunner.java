package snake.game.core.runner;

public interface GameRunner {

    void start();

    void cancel();

    void pause();

    void resume();

    boolean isRunning();
}
