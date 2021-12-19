package snake.game.core;

import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;

public interface SnakeGame {

    void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction);

    void kill(SnakeNumber snakeNumber);

    GameState getGameState();

    void start();

    void cancel();

    void pause();

    void resume();

    boolean isRunning();
}