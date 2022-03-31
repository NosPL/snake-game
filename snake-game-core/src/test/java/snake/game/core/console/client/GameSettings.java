package snake.game.core.console.client;

import lombok.Value;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.Walls;

@Value
class GameSettings {
    PlayerNumber playerNumber;
    GameSpeed gameSpeed;
    GridSize gridSize;
    Walls walls;
}
