package snake.game.core.events;

import com.noscompany.snake.game.commons.MessageDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.MessageDto.MessageType.GAME_CONTINUES;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameContinues implements GameEvent {
    MessageDto.MessageType messageType = GAME_CONTINUES;
    GridSize gridSize;
    Point foodPoint;
    Collection<SnakeDto> snakes;
    Score score;

    public static GameContinues gameContinues(GameState gameState) {
        return new GameContinues(
                gameState.getGridSize(),
                gameState.getFoodPoint(),
                gameState.getSnakes(),
                gameState.getScore());
    }
}