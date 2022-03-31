package snake.game.core.dto.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.SNAKES_MOVED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameContinues implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = SNAKES_MOVED;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Collection<Snake> snakes;
    Score score;

    public static GameContinues createEvent(GameState gameState) {
        return new GameContinues(
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition(),
                gameState.getSnakes(),
                gameState.getScore());
    }
}