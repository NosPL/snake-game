package snake.game.gameplay.console.client;

import lombok.Value;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;

@Value
class GameSettings {
    PlayerNumber playerNumber;
    GameSpeed gameSpeed;
    GridSize gridSize;
    Walls walls;
}
