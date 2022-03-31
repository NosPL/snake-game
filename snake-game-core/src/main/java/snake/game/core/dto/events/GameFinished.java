package snake.game.core.dto.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
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
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Score score;

    public static GameFinished createEvent(GameState gameState) {
        return new GameFinished(
                gameState.getSnakes(),
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition(),
                gameState.getScore());
    }
}