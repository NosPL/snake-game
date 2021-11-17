package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_CANCELLED;
import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_STARTED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameStarted implements GameEvent, MessageDto {
    MessageDto.MessageType messageType = GAME_STARTED;
    GridSize gridSize;
    Point foodPoint;
    Collection<SnakeDto> snakes;
    Score score;

    public static GameStarted gameStarted(GameState gameState) {
        return new GameStarted(gameState.getGridSize(), gameState.getFoodPoint(), gameState.getSnakes(), gameState.getScore());
    }
}