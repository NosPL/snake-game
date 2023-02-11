package snake.game.gameplay.internal.logic.internal.food.locator;

import io.vavr.collection.Vector;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
class RandomFreePositionFinder {
    private final GridSize gridSize;
    private final Random random;

    public static RandomFreePositionFinder create(GridSize gridSize) {
        return new RandomFreePositionFinder(gridSize, new Random());
    }

    Option<Position> find(Collection<Snake> snakes) {
        return getAllGridPositions()
                .removeAll(positionsOf(snakes))
                .transform(this::findRandomPosition);
    }

    private Option<Position> findRandomPosition(Vector<Position> positions) {
        if (positions.isEmpty())
            return Option.none();
        var randomIndex = random.nextInt(positions.size());
        Position randomPosition = positions.get(randomIndex);
        return Option.of(randomPosition);
    }

    private Vector<Position> getAllGridPositions() {
        return Vector.ofAll(gridSize.allPositions());
    }

    private Set<Position> positionsOf(Collection<Snake> snakes) {
        return snakes.stream()
                .map(Snake::getNodes)
                .flatMap(Collection::stream)
                .map(Snake.Node::getPosition)
                .collect(toSet());
    }
}