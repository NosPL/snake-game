package snake.game.core.logic;

import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.dto.Walls;
import snake.game.core.logic.collision.detection.CollisionDetector;
import snake.game.core.logic.food.FoodCreator;
import snake.game.core.logic.scoring.ScoringSystemCreator;
import snake.game.core.logic.snakes.SnakesCreator;

import java.util.Set;

public class GameLogicCreator {

    public static GameLogic create(
            Set<SnakeNumber> snakeNumbers,
            Walls walls,
            GridSize gridSize) {
        var snakes = SnakesCreator.createSnakes(snakeNumbers, gridSize, walls);
        var collisionDetector = new CollisionDetector(gridSize);
        var scoringSystem = ScoringSystemCreator.create(snakeNumbers);
        var food = FoodCreator.create(snakes, gridSize);
        return new GameLogicImpl(gridSize, snakes, collisionDetector, scoringSystem, food, null);
    }
}