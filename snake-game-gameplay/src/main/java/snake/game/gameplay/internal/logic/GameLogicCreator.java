package snake.game.gameplay.internal.logic;

import com.noscompany.snake.game.online.contract.messages.game.dto.GridSize;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Walls;
import snake.game.gameplay.internal.logic.internal.current.game.state.GameStateView;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocator;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocatorCreator;
import snake.game.gameplay.internal.logic.internal.scoring.Scoring;
import snake.game.gameplay.internal.logic.internal.scoring.ScoringCreator;
import snake.game.gameplay.internal.logic.internal.snakes.SnakesCreator;
import snake.game.gameplay.internal.logic.internal.snakes.Snakes;

import java.util.Set;

public class GameLogicCreator {

    public static GameLogic create(Set<PlayerNumber> playerNumbers, GridSize gridSize, Walls walls) {
        Scoring scoring = ScoringCreator.create(playerNumbers);
        Snakes snakes = SnakesCreator.create(playerNumbers, gridSize, walls);
        FoodLocator foodLocator = FoodLocatorCreator.create(snakes.toDto(), gridSize, walls);
        GameStateView gameStateView = GameStateView.create(
                snakes.toDto(), foodLocator.getFoodPosition(), scoring.getScore(), walls, gridSize);
        GameLogic gameLogic = new GameLogicImpl(snakes, scoring, foodLocator, gameStateView);
        return new GameLogicThreadSafetyDecorator(gameLogic);
    }

}