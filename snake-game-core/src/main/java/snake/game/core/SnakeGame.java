package snake.game.core;

import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;

public interface SnakeGame {

    void start();

    void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction);

    void changeSnakesDirection(Direction direction);

    boolean isRunning();

    void cancel();

    void pause();

    void resume();

    GameState getGameState();
}