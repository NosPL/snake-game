package snake.game.core.dto.events;

import com.noscompany.snake.game.commons.OnlineMessage;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import snake.game.core.dto.*;

import java.util.Collection;

import static com.noscompany.snake.game.commons.OnlineMessage.MessageType.TIME_LEFT_TO_GAME_START_CHANGED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TimeLeftToGameStartHasChanged implements GameEvent, OnlineMessage {
    OnlineMessage.MessageType messageType = TIME_LEFT_TO_GAME_START_CHANGED;
    int secondsLeft;
    Collection<Snake> snakes;
    GridSize gridSize;
    Walls walls;
    Option<Position> foodPosition;
    Score score;

    public static TimeLeftToGameStartHasChanged timeLeftToGameStartHasChanged(int secondsLeft, GameState currentState) {
        return new TimeLeftToGameStartHasChanged(
                secondsLeft,
                currentState.getSnakes(),
                currentState.getGridSize(),
                currentState.getWalls(),
                currentState.getFoodPosition(),
                currentState.getScore());
    }
}