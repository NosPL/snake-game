package snake.game.core.internal.logic.internal.current.game.state;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class CurrentGameState {
    private volatile GameState gameState;
    private final Walls walls;
    private final GridSize gridSize;

    public void update(Collection<Snake> snakes, Option<Position> foodPosition, Score score) {
        gameState = new GameState(snakes, gridSize, walls, foodPosition, score);
    }

    public GameState get() {
        return gameState;
    }

    public static CurrentGameState create(Collection<Snake> snakes, Option<Position> foodPosition, Score score, Walls walls, GridSize gridSize) {
        GameState gameState = new GameState(snakes, gridSize, walls, foodPosition, score);
        return new CurrentGameState(gameState, walls, gridSize);
    }
}