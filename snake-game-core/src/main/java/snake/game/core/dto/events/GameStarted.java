package snake.game.core.dto.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.GAME_STARTED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameStarted implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = GAME_STARTED;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Collection<Snake> snakes;
    Score score;

    public static GameStarted gameStarted(GameState gameState) {
        return new GameStarted(
                gameState.getGridSize(),
                gameState.getWalls(),
                gameState.getFoodPosition(),
                gameState.getSnakes(),
                gameState.getScore());
    }
}