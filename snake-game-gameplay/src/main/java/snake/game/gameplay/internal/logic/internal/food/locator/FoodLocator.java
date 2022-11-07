package snake.game.gameplay.internal.logic.internal.food.locator;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;
import com.noscompany.snake.game.online.contract.messages.game.dto.Snake;
import snake.game.gameplay.internal.logic.internal.SnakesMoved;

import java.util.Collection;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class FoodLocator {
    @Getter
    private Option<Position> foodPosition;
    private RandomFreePositionFinder randomFreePositionFinder;
    private int snakeMoveCount;
    private int moveLimit;

    public void updateFoodPosition(SnakesMoved snakesMoved) {
        snakeMoveCount++;
        boolean foodGotConsumed = snakesMoved.foodGotConsumed();
        if (moveLimitReached() || foodGotConsumed) {
            snakeMoveCount = 0;
            Collection<Snake> snakes = snakesMoved.getSnakes();
            foodPosition = randomFreePositionFinder.find(snakes);
        }
    }

    private boolean moveLimitReached() {
        return snakeMoveCount >= moveLimit;
    }
}