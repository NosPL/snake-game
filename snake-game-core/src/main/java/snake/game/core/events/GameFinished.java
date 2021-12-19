package snake.game.core.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.GAME_FINISHED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameFinished implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = GAME_FINISHED;
    Collection<SnakeDto> snakes;
    GridSize gridSize;
    Point foodPoint;
    Score score;

    public static GameFinished gameFinished(GameState gameState) {
        return new GameFinished(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getFoodPoint(),
                gameState.getScore());
    }
}