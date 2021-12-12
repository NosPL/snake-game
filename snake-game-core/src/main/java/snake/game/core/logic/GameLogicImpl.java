package snake.game.core.logic;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.events.GameContinues;
import snake.game.core.events.GameFinished;
import snake.game.core.logic.collision.detection.CollisionDetector;
import snake.game.core.logic.collision.detection.Collisions;
import snake.game.core.logic.food.Food;
import snake.game.core.logic.food.FoodCreator;
import snake.game.core.logic.scoring.ScoringSystem;
import snake.game.core.logic.snakes.SnakeConsumedFood;
import snake.game.core.logic.snakes.Snakes;

import static snake.game.core.events.GameContinues.gameContinues;
import static snake.game.core.events.GameFinished.gameFinished;

@AllArgsConstructor
class GameLogicImpl implements GameLogic {
    private final GridSize gridSize;
    private final Snakes snakes;
    private final CollisionDetector collisionDetector;
    private final ScoringSystem scoringSystem;
    private Food food;
    private GameFinished gameFinished;

    @Override
    public synchronized void changeSnakeDirection(SnakeNumber snakeNumber, Direction newDirection) {
        snakes.changeDirection(snakeNumber, newDirection);
    }

    @Override
    public synchronized GameState getCurrentState() {
        return new GameState(snakes.toDto(), gridSize, food.getPoint(), scoringSystem.getCurrentScore());
    }

    @Override
    public synchronized Either<GameFinished, GameContinues> move() {
        if (gameIsFinished())
            return Either.left(gameFinished);
        return makeMove().peekLeft(this::save);
    }

    private Either<GameFinished, GameContinues> makeMove() {
        Option<SnakeConsumedFood> event = snakes.moveAndConsume(food);
        event.peek(scoringSystem::handle);
        Collisions collisions = collisionDetector.detectCollisions(snakes.toDto());
        snakes.handle(collisions);
        scoringSystem.handle(collisions);
        if (snakes.areAllKilled())
            return Either.left(gameFinished(getCurrentState()));
        updateFood();
        return Either.right(gameContinues(getCurrentState()));
    }

    private void save(GameFinished gameFinished) {
        this.gameFinished = gameFinished;
    }

    private void updateFood() {
        food.incrementMoveCount();
        if (food.gotConsumed() || food.moveCountReached())
            food = FoodCreator.create(snakes, gridSize);
    }

    private boolean gameIsFinished() {
        return gameFinished != null;
    }
}