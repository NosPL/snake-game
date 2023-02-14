package snake.game.gameplay;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;

public interface Gameplay {

    void start();

    void changeSnakeDirection(PlayerNumber playerNumber, Direction direction);

    void killSnake(PlayerNumber playerNumber);

    void pause();

    void resume();

    void cancel();

    boolean isRunning();

    GameState getGameState();
}