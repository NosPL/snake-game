package snake.game.core.logic.snakes;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import snake.game.core.dto.Point;
import snake.game.core.logic.food.Food;

@AllArgsConstructor
class Node {
    @Getter
    private Point point;
    private boolean hasFood;

    boolean hasFood() {
        return hasFood;
    }

    Option<Node> createNewNodeFromFood() {
        if (this.hasFood) {
            this.hasFood = false;
            return Option.of(new Node(point, false));
        }
        return Option.none();
    }

    boolean consume(Food food) {
        if (point.equals(food.getPoint())) {
            hasFood = true;
            food.markAsConsumed();
            return true;
        } else
            return false;
    }
}