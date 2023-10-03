package snake.game.gameplay.internal.logic.internal.food.locator;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Snake;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import snake.game.gameplay.internal.logic.internal.snakes.Snakes;

import java.util.List;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
public class FoodLocator {
    @Getter
    private Option<Position> foodPosition;
    private RandomFreePositionFinder randomFreePositionFinder;
    private int snakeMoveCount;
    private int moveLimit;

    public void updateFoodPosition(List<Snake> snakes) {
        snakeMoveCount++;
        if (moveLimitReached() || foodGotConsumed(snakes)) {
            snakeMoveCount = 0;
            foodPosition = randomFreePositionFinder.find(snakes);
        }
    }

    private boolean foodGotConsumed(List<Snake> snakes) {
        return snakes.stream()
                .map(Snake::getHeadNode)
                .map(Snake.Node::getPosition)
                .anyMatch(this::headPositionEqualsFoodPosition);
    }

    private boolean headPositionEqualsFoodPosition(Position headPosition) {
        return foodPosition.exists(position -> position.equals(headPosition));
    }

    private boolean moveLimitReached() {
        return snakeMoveCount >= moveLimit;
    }
}