package snake.game.gameplay.dto;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import lombok.Value;

import java.util.Set;

@Value
public class GameplayParams {
    Set<PlayerNumber> playerNumbers;
    GameSpeed gameSpeed;
    GridSize gridSize;
    Walls walls;
    CountdownTime countdownTime;
}