package snake.game.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor
public class GameState {
    Collection<SnakeDto> snakes;
    GridSize gridSize;
    Point foodPoint;
    Score score;
}
