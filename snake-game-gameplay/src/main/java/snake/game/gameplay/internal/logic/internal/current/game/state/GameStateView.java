package snake.game.gameplay.internal.logic.internal.current.game.state;

import com.noscompany.snake.game.online.contract.messages.game.dto.*;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class GameStateView {
    private volatile GameState gameState;
    private final Walls walls;
    private final GridSize gridSize;

    public void update(Collection<Snake> snakes, Option<Position> foodPosition, Score score) {
        gameState = new GameState(snakes, gridSize, walls, foodPosition, score);
    }

    public GameState get() {
        return gameState;
    }

    public static GameStateView create(Collection<Snake> snakes, Option<Position> foodPosition, Score score, Walls walls, GridSize gridSize) {
        GameState gameState = new GameState(snakes, gridSize, walls, foodPosition, score);
        return new GameStateView(gameState, walls, gridSize);
    }
}