package snake.game.core;

import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.PlayerNumber;

public interface SnakeGame {

    void start();

    void changeSnakeDirection(PlayerNumber playerNumber, Direction direction);

    void killSnake(PlayerNumber playerNumber);

    void pause();

    void resume();

    void cancel();

    boolean isRunning();

    GameState getGameState();
}