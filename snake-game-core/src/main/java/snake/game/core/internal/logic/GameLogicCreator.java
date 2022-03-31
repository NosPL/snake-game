package snake.game.core.internal.logic;

import io.vavr.control.Option;
import snake.game.core.dto.*;
import snake.game.core.internal.logic.internal.current.game.state.CurrentGameState;
import snake.game.core.internal.logic.internal.food.locator.FoodLocator;
import snake.game.core.internal.logic.internal.food.locator.FoodLocatorCreator;
import snake.game.core.internal.logic.internal.scoring.Scoring;
import snake.game.core.internal.logic.internal.scoring.ScoringCreator;
import snake.game.core.internal.logic.internal.snakes.Snakes;
import snake.game.core.internal.logic.internal.snakes.SnakesCreator;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GameLogicCreator {

    public static GameLogic create(Set<PlayerNumber> playerNumbers, GridSize gridSize, Walls walls) {
        Scoring scoring = ScoringCreator.create(playerNumbers);
        Snakes snakes = SnakesCreator.create(playerNumbers, gridSize, walls);
        Collection<Snake> snakesDto = snakes.toDto();
        FoodLocator foodLocator = FoodLocatorCreator.create(snakesDto, gridSize, walls);
        Option<Position> foodPosition = foodLocator.getFoodPosition();
        Score score = scoring.getScore();
        CurrentGameState currentGameState = CurrentGameState.create(snakesDto, foodPosition, score, walls, gridSize);
        GameLogic gameLogic = new GameLogicImpl(snakes, scoring, foodLocator, currentGameState);
        return threadSafe(gameLogic);
    }

    private static GameLogicThreadSafetyDecorator threadSafe(GameLogic gameLogic) {
        return new GameLogicThreadSafetyDecorator(gameLogic);
    }
}