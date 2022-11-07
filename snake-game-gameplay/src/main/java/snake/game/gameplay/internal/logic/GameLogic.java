package snake.game.gameplay.internal.logic;

import io.vavr.control.Either;
import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.events.GameContinues;
import com.noscompany.snake.game.online.contract.messages.game.events.GameFinished;

public interface GameLogic {

    void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection);

    void killSnake(PlayerNumber playerNumber);

    Either<GameFinished, GameContinues> moveSnakes();

    GameState getGameState();
}