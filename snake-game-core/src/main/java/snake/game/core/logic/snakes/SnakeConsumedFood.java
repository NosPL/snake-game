package snake.game.core.logic.snakes;

import lombok.Value;
import snake.game.core.dto.SnakeNumber;

@Value
public class SnakeConsumedFood {
    SnakeNumber snakeNumber;
}