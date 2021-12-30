package snake.game.core;

import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;

public interface SnakeGame {

    void start();

    void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction);

    void kill(SnakeNumber snakeNumber);

    void pause();

    void resume();

    void cancel();

    boolean isRunning();

    GameState getGameState();
}