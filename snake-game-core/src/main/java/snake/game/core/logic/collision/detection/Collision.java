package snake.game.core.logic.collision.detection;

import lombok.Value;
import snake.game.core.dto.SnakeNumber;

import java.util.List;

public interface Collision {
    List<SnakeNumber> getKilledSnakesNumbers();

    @Value
    class SnakeHitAnotherSnake implements Collision {
        SnakeNumber hittingSnake;
        SnakeNumber snakeThatGotHit;

        @Override
        public List<SnakeNumber> getKilledSnakesNumbers() {
            return List.of(hittingSnake);
        }
    }

    @Value
    class SnakeHitItSelf implements Collision {
        SnakeNumber snakeNumber;

        @Override
        public List<SnakeNumber> getKilledSnakesNumbers() {
            return List.of(snakeNumber);
        }
    }

    @Value
    class SnakeHitTheWall implements Collision {
        SnakeNumber snakeNumber;

        @Override
        public List<SnakeNumber> getKilledSnakesNumbers() {
            return List.of(snakeNumber);
        }
    }

    @Value
    class SnakesClashedHeads implements Collision {
        List<SnakeNumber> snakeNumbers;

        @Override
        public List<SnakeNumber> getKilledSnakesNumbers() {
            return snakeNumbers;
        }
    }
}