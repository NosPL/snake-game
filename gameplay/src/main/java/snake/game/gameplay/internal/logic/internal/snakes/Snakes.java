package snake.game.gameplay.internal.logic.internal.snakes;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class Snakes {
    private final Map<PlayerNumber, Snake> snakesMap;
    private final SnakeCollisionFinder snakeCollisionFinder;

    public void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection) {
        findSnakeBy(playerNumber)
                .peek(snake -> snake.changeDirection(newDirection));
    }

    public void killSnake(PlayerNumber playerNumber) {
        findSnakeBy(playerNumber)
                .peek(Snake::kill);
    }

    public List<com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake> toDto() {
        return snakes()
                .stream()
                .map(Snake::toDto)
                .toList();
    }

    public boolean areAllDead() {
        return snakes().stream()
                .noneMatch(Snake::isAlive);
    }

    public void move() {
        snakes().forEach(Snake::move);
    }

    public void killCrashed() {
        snakeCollisionFinder
                .findCrashedSnakesNumbers(snakes())
                .forEach(this::killSnake);
    }

    public void feed(Option<Position> foodPosition) {
        foodPosition.peek(this::feedSnakes);
    }

    private void feedSnakes(Position foodPosition) {
        snakes().forEach(snake -> snake.tryToEatFood(foodPosition));
    }

    private Option<Snake> findSnakeBy(PlayerNumber playerNumber) {
        return Option.of(snakesMap.get(playerNumber));
    }

    private Collection<Snake> snakes() {
        return snakesMap.values();
    }
}