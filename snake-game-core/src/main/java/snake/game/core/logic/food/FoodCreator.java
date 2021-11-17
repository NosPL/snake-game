package snake.game.core.logic.food;

import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;
import snake.game.core.logic.snakes.Snakes;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class FoodCreator {

    public static Food create(Snakes snakes, GridSize gridSize) {
        var snakeDtos = snakes.toDto();
        var freePoints = freePoints(snakeDtos, gridSize);
        var randomPoint = getRandomPointFrom(freePoints);
        var moveCount = calculateMoveCount(gridSize);
        return Food.create(randomPoint, moveCount);
    }

    private static int calculateMoveCount(GridSize gridSize) {
        return gridSize.getHeight();
    }

    private static Point getRandomPointFrom(List<Point> freePoints) {
        var random = new Random();
        var randomIndex = random.nextInt(freePoints.size());
        return freePoints.get(randomIndex);
    }

    private static List<Point> freePoints(Collection<SnakeDto> snakes, GridSize gridSize) {
        var gridPoints = gridSize.allPoints();
        gridPoints.removeAll(pointsOf(snakes));
        return gridPoints;
    }

    private static List<Point> pointsOf(Collection<SnakeDto> snakes) {
        return snakes
                .stream()
                .flatMap(s -> s.allPoints().stream())
                .collect(toList());
    }
}
