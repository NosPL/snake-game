package snake.game.core.logic;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.GridSize;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.events.GameContinues;
import snake.game.core.events.GameFinished;
import snake.game.core.logic.collision.detection.CollisionDetector;
import snake.game.core.logic.food.Food;
import snake.game.core.logic.food.FoodCreator;
import snake.game.core.logic.scoreboard.ScoreBoard;
import snake.game.core.logic.snakes.Snakes;

import static snake.game.core.events.GameContinues.gameContinues;
import static snake.game.core.events.GameFinished.gameFinished;

@AllArgsConstructor
class GameLogicImpl implements GameLogic {
    private final GridSize gridSize;
    private final Snakes snakes;
    private final CollisionDetector collisionDetector;
    private final ScoreBoard scoreBoard;
    private Food food;
    private GameFinished gameFinished;

    @Override
    public synchronized void changeSnakeDirection(Direction newDirection) {
        if (gameIsFinished())
            return;
        snakes.changeDirection(newDirection);
    }

    @Override
    public synchronized void changeSnakeDirection(SnakeNumber snakeNumber, Direction newDirection) {
        if (gameIsFinished())
            return;
        snakes.changeDirection(snakeNumber, newDirection);
    }

    @Override
    public synchronized GameState getCurrentState() {
        return new GameState(snakes.toDto(), gridSize, food.getPoint(), scoreBoard.getCurrentScore());
    }

    @Override
    public synchronized Either<GameFinished, GameContinues> move() {
        if (gameIsFinished())
            return Either.left(gameFinished);
        snakes.removeKilledSnakes();
        snakes.moveAndConsume(food)
                .peek(scoreBoard::update);
        updateFood();
        killCollidedSnakes();
        if (snakes.areAllKilled()) {
            var gameFinished = gameFinished(getCurrentState());
            save(gameFinished);
            return Either.left(gameFinished);
        }
        else
            return Either.right(gameContinues(getCurrentState()));
    }

    private GameFinished save(GameFinished gameFinished) {
        this.gameFinished = gameFinished;
        return gameFinished;
    }

    private void killCollidedSnakes() {
        collisionDetector
                .detectCollisions(snakes.toDto())
                .getKilledSnakesNumbers()
                .forEach(snakes::kill);
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