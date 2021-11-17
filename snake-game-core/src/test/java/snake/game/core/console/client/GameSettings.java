package snake.game.core.console.client;

import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.dto.Walls;

@Value
class GameSettings {
    SnakeNumber snakeNumber;
    GameSpeed gameSpeed;
    GridSize gridSize;
    Walls walls;
}
