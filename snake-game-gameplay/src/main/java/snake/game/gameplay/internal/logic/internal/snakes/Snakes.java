package snake.game.gameplay.internal.logic.internal.snakes;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;
import snake.game.gameplay.internal.logic.internal.SnakesDidNotMoveBecauseAllAreDead;
import snake.game.gameplay.internal.logic.internal.SnakesMoved;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static java.util.stream.Collectors.toList;
import static snake.game.gameplay.internal.logic.internal.SnakesDidNotMoveBecauseAllAreDead.SNAKES_DID_NOT_MOVE_BECAUSE_ALL_ARE_DEAD;

@AllArgsConstructor
public class Snakes {
    private final Map<PlayerNumber, Snake> snakesMap;
    private final CrashedSnakesFinder crashedSnakesFinder;

    public void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection) {
        findSnakeBy(playerNumber)
                .peek(snake -> snake.changeDirection(newDirection));
    }

    public void killSnake(PlayerNumber playerNumber) {
        findSnakeBy(playerNumber)
                .peek(Snake::kill);
    }

    public List<com.noscompany.snake.game.online.contract.messages.game.dto.Snake> toDto() {
        return snakes()
                .stream()
                .map(Snake::toDto)
                .collect(toList());
    }

    public Either<SnakesDidNotMoveBecauseAllAreDead, SnakesMoved> moveAndFeed(Option<Position> foodPosition) {
        if (allSnakesAreDead())
            return left(SNAKES_DID_NOT_MOVE_BECAUSE_ALL_ARE_DEAD);
        moveSnakes();
        foodPosition.peek(this::feedSnakes);
        killCrashedSnakes();
        return snakesMoved();
    }

    private Either<SnakesDidNotMoveBecauseAllAreDead, SnakesMoved> snakesMoved() {
        return right(new SnakesMoved(this.toDto()));
    }

    private boolean allSnakesAreDead() {
        return snakes().stream()
                .noneMatch(Snake::isAlive);
    }

    private void moveSnakes() {
        snakes().forEach(Snake::move);
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