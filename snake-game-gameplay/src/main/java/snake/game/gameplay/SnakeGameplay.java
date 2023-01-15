package snake.game.gameplay;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;

public interface SnakeGameplay {

    void start();

    void changeSnakeDirection(PlayerNumber playerNumber, Direction direction);

    void killSnake(PlayerNumber playerNumber);

    void pause();

    void resume();

    void cancel();

    boolean isRunning();

    GameState getGameState();
}