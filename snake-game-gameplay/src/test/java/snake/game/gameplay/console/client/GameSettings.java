package snake.game.gameplay.console.client;

import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;

@Value
class GameSettings {
    PlayerNumber playerNumber;
    GameSpeed gameSpeed;
    GridSize gridSize;
    Walls walls;
}
