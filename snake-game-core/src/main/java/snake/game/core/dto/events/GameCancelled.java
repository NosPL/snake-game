package snake.game.core.dto.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.GAME_CANCELLED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameCancelled implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = GAME_CANCELLED;
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;

    public static GameCancelled gameCancelled(GameState gameState) {
        return new GameCancelled(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition());
    }
}