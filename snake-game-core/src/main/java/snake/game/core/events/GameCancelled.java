package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.GameState;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.Point;
import snake.game.core.dto.SnakeDto;

import java.util.Collection;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_CANCELLED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameCancelled implements GameEvent, MessageDto {
    MessageDto.MessageType messageType = GAME_CANCELLED;
    Collection<SnakeDto> snakes;
    GridSize gridSize;
    Point foodPoint;

    public static GameCancelled gameCancelled(GameState gameState) {
        return new GameCancelled(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getFoodPoint());
    }
}