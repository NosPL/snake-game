package snake.game.gameplay.internal.logic.internal.food.locator;

import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.Snake;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

import java.util.Collection;

@AllArgsConstructor
public class FoodLocatorCreator {

    public static FoodLocator create(Collection<Snake> snakes, GridSize gridSize, Walls walls) {
        RandomFreePositionFinder randomFreePositionFinder = RandomFreePositionFinder.create(gridSize);
        var foodPosition = randomFreePositionFinder.find(snakes);
        var moveLimit = calculateMoveLimit(gridSize, walls);
        return new FoodLocator(
                foodPosition,
                randomFreePositionFinder,
                0,
                moveLimit);
    }

    private static int calculateMoveLimit(GridSize gridSize, Walls walls) {
        if (walls == Walls.OFF)
            return (int) (gridSize.getHeight() * 1.5);
        return gridSize.getHeight() * 2;
    }
}