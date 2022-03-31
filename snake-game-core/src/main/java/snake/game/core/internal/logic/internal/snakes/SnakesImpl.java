package snake.game.core.internal.logic.internal.snakes;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.Position;
import snake.game.core.internal.logic.internal.SnakesDidNotMove;
import snake.game.core.internal.logic.internal.SnakesMoved;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static io.vavr.control.Option.of;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SnakesImpl implements Snakes {
    private final Map<PlayerNumber, Snake> snakesMap;
    private final CrashedSnakesFinder crashedSnakesFinder;

    @Override
    public void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection) {
        findSnakeBy(playerNumber)
                .peek(snake -> snake.changeDirection(newDirection));
    }

    @Override
    public void killSnake(PlayerNumber playerNumber) {
        findSnakeBy(playerNumber)
                .peek(Snake::kill);
    }

    @Override
    public List<snake.game.core.dto.Snake> toDto() {
        return snakes()
                .stream()
                .map(Snake::toDto)
                .collect(toList());
    }

    @Override
    public Either<SnakesDidNotMove, SnakesMoved> moveAndFeed(Option<Position> foodPosition) {
        if (allSnakesAreDead())
            return left(SnakesDidNotMove.BECAUSE_ALL_SNAKES_ARE_DEAD);
        moveSnakes();
        foodPosition.peek(this::feedSnakes);
        killCrashedSnakes();
        return snakesMoved();
    }

    private boolean allSnakesAreDead() {
        return snakes()
                .stream()
                .noneMatch(Snake::isAlive);
    }

    private void moveSnakes() {
        snakes().forEach(Snake::move);
    }

    private Either<SnakesDidNotMove, SnakesMoved> snakesMoved() {
        var snakes = this.toDto();
        return right(new SnakesMoved(snakes));
    }

    private void feedSnakes(Position foodPosition) {
        snakes().forEach(snake -> snake.tryToEatFood(foodPosition));
    }

    private void killCrashedSnakes() {
        crashedSnakesFinder
                .findCrashed(snakes())
                .forEach(this::killSnake);
    }

    private Option<Snake> findSnakeBy(PlayerNumber playerNumber) {
        return Option.of(snakesMap.get(playerNumber));
    }

    private Collection<Snake> snakes() {
        return snakesMap.values();
    }
}