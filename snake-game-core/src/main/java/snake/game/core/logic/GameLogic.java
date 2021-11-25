package snake.game.core.logic;

import io.vavr.control.Either;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.events.GameContinues;
import snake.game.core.events.GameFinished;

public interface GameLogic {

    void changeSnakeDirection(SnakeNumber snakeNumber, Direction newDirection);

    GameState getCurrentState();

    Either<GameFinished, GameContinues> move();
}