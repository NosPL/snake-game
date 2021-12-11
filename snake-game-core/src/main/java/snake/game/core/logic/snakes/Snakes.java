package snake.game.core.logic.snakes;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.SnakeDto;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.logic.food.Food;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class Snakes {
    private Map<SnakeNumber, Snake> snakesMap;

    public boolean isEmpty() {
        return snakesMap.isEmpty();
    }

    public void changeDirection(SnakeNumber snakeNumber, Direction newDirection) {
        var snake = snakesMap.get(snakeNumber);
        if (snake != null)
            snake.changeDirectionTo(newDirection);
    }

    public void removeBy(SnakeNumber snakeNumber) {
        snakesMap.remove(snakeNumber);
    }

    public Option<SnakeConsumedFood> moveAndConsume(Food food) {
        var result = snakesMap
                .values().stream()
                .filter(Snake::isAlive)
                .map(snake -> snake.moveAndConsume(food))
                .flatMap(Option::toJavaStream)
                .collect(toList());
        return normalize(result);
    }

    private Option<SnakeConsumedFood> normalize(List<SnakeConsumedFood> result) {
        if (result.size() == 1)
            return Option.of(result.get(0));
        else
            return Option.none();
    }

    public List<SnakeDto> toDto() {
        return snakesMap
                .values()
                .stream()
                .map(Snake::toDto)
                .collect(toList());
    }

    public void removeKilledSnakes() {
        snakesMap = snakesMap
                .values().stream()
                .filter(Snake::isAlive)
                .collect(toMap(Snake::getSnakeNumber, snake -> snake));
    }

    public boolean areAllKilled() {
        for (Snake snake : snakesMap.values())
            if (snake.isAlive())
                return false;
        return true;
    }

    public void kill(SnakeNumber snakeNumber) {
        var snake = snakesMap.get(snakeNumber);
        if (snake != null)
            snake.kill();
    }

    public void changeDirection(Direction newDirection) {
        snakesMap
                .values()
                .forEach(snake -> snake.changeDirectionTo(newDirection));
    }
}