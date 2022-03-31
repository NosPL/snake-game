package snake.game.core.internal.logic;

import io.vavr.control.Either;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.events.GameContinues;
import snake.game.core.dto.events.GameFinished;

public interface GameLogic {

    void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection);

    void killSnake(PlayerNumber playerNumber);

    Either<GameFinished, GameContinues> moveSnakes();

    GameState getGameState();
}