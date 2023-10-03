package snake.game.gameplay.internal.logic;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Walls;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocator;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocatorCreator;
import snake.game.gameplay.internal.logic.internal.scoring.Scoring;
import snake.game.gameplay.internal.logic.internal.scoring.ScoringCreator;
import snake.game.gameplay.internal.logic.internal.snakes.SnakesCreator;
import snake.game.gameplay.internal.logic.internal.snakes.Snakes;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class GameLogicCreator {

    public static GameLogic create(Set<PlayerNumber> playerNumbers, GridSize gridSize, Walls walls) {
        Scoring scoring = ScoringCreator.create(playerNumbers);
        Snakes snakes = SnakesCreator.create(playerNumbers, gridSize, walls);
        FoodLocator foodLocator = FoodLocatorCreator.create(snakes.toDto(), gridSize, walls);
        GameLogic gameLogic = new GameLogicFacade(snakes, scoring, foodLocator, gridSize, walls);
        AtomicReference<GameState> gameState = new AtomicReference<>(gameLogic.getGameState());
        return new GameLogicThreadSafetyDecorator(gameLogic, gameState);
    }
}