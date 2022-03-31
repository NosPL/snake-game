package snake.game.core.internal.logic.internal.snakes;

import io.vavr.control.Either;
import io.vavr.control.Option;
import snake.game.core.dto.Direction;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.Position;
import snake.game.core.dto.Snake;
import snake.game.core.internal.logic.internal.SnakesDidNotMove;
import snake.game.core.internal.logic.internal.SnakesMoved;

import java.util.Collection;
import java.util.List;

public interface Snakes {

    void killSnake(PlayerNumber playerNumber);

    void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection);

    Either<SnakesDidNotMove, SnakesMoved> moveAndFeed(Option<Position> foodPosition);

    Collection<Snake> toDto();
}