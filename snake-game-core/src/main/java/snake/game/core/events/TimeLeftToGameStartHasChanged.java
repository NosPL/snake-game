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
import static com.noscompany.snake.game.commons.MessageDto.MessageType.TIME_LEFT_TO_GAME_START_CHANGED;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class TimeLeftToGameStartHasChanged implements GameEvent{
    MessageDto.MessageType messageType = TIME_LEFT_TO_GAME_START_CHANGED;
    int secondsLeft;
    Collection<SnakeDto> snakes;
    GridSize gridSize;
    Point foodPoint;

    public static TimeLeftToGameStartHasChanged countdownEvent(int secondsLeft, GameState currentState) {
        return new TimeLeftToGameStartHasChanged(
                secondsLeft,
                currentState.getSnakes(),
                currentState.getGridSize(),
                currentState.getFoodPoint());
    }
}