package snake.game.gameplay.internal.logic;

import io.vavr.control.Either;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;

public interface GameLogic {

    void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection);

    void killSnake(PlayerNumber playerNumber);

    Either<GameFinished, SnakesMoved> moveSnakes();

    GameState getGameState();
}