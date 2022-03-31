package snake.game.core.internal.logic.internal.food.locator;

import lombok.AllArgsConstructor;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Snake;
import snake.game.core.dto.Walls;

import java.util.Collection;

@AllArgsConstructor
public class FoodLocatorCreator {

    public static FoodLocator create(Collection<Snake> snakes, GridSize gridSize, Walls walls) {
        GridPositionFinder gridPositionFinder = GridPositionFinder.create(gridSize);
        var foodPosition = gridPositionFinder.findRandomGridPositionExcludingPositionsOf(snakes);
        var moveLimit = calculateMoveLimit(gridSize, walls);
        return new FoodLocator(
                foodPosition,
                gridPositionFinder,
                0,
                moveLimit);
    }

    private static int calculateMoveLimit(GridSize gridSize, Walls walls) {
        if (walls == Walls.OFF)
            return (int) (gridSize.getHeight() * 1.5);
        return gridSize.getHeight() * 2;
    }
}