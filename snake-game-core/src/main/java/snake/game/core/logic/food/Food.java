package snake.game.core.logic.food;

import snake.game.core.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class Food {
    @Getter
    private final Point point;
    private boolean gotConsumed;
    private int moveCount;
    private int moveLimit;

    public static Food create(Point point, int moveLimit) {
        return new Food(point, false, 0, moveLimit);
    }

    public void markAsConsumed() {
        this.gotConsumed = true;
    }

    public void incrementMoveCount() {
        this.moveCount++;
    }

    public boolean gotConsumed() {
        return gotConsumed;
    }

    public boolean moveCountReached() {
        return moveCount > moveLimit;
    }

}