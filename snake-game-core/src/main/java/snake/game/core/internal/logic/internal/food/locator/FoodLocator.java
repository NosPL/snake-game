package snake.game.core.internal.logic.internal.food.locator;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import snake.game.core.dto.Position;
import snake.game.core.dto.Snake;
import snake.game.core.internal.logic.internal.SnakesMoved;

import java.util.Collection;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class FoodLocator {
    @Getter
    private Option<Position> foodPosition;
    private GridPositionFinder gridPositionFinder;
    private int snakeMoveCount;
    private int moveLimit;

    public void updateFoodPosition(SnakesMoved snakesMoved) {
        snakeMoveCount++;
        boolean foodGotConsumed = snakesMoved.foodGotConsumed();
        if (moveLimitReached() || foodGotConsumed) {
            snakeMoveCount = 0;
            Collection<Snake> snakes = snakesMoved.getSnakes();
            foodPosition = gridPositionFinder.findRandomGridPositionExcludingPositionsOf(snakes);
        }
    }

    private boolean moveLimitReached() {
        return snakeMoveCount >= moveLimit;
    }
}