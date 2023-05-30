package snake.game.gameplay.internal.logic;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.*;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocator;
import snake.game.gameplay.internal.logic.internal.scoring.Scoring;
import snake.game.gameplay.internal.logic.internal.snakes.Snakes;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

@AllArgsConstructor
class GameLogicFacade implements GameLogic {
    private final Snakes snakes;
    private final Scoring scoring;
    private final FoodLocator foodLocator;
    private final GridSize gridSize;
    private final Walls walls;

    @Override
    public void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection) {
        snakes.changeSnakeDirection(playerNumber, newDirection);
    }

    @Override
    public void killSnake(PlayerNumber playerNumber) {
        snakes.killSnake(playerNumber);
    }

    @Override
    public GameState getGameState() {
        return new GameState(snakes.toDto(), gridSize, walls, getFoodPosition(), scoring.getScore());
    }

    @Override
    public Either<GameFinished, SnakesMoved> moveSnakes() {
        if (snakes.areAllDead())
            return left(gameFinished());
        snakes.move();
        snakes.killCrashed();
        snakes.feed(getFoodPosition());
        var snakesDto = snakes.toDto();
        scoring.updateScores(snakesDto);
        foodLocator.updateFoodPosition(snakesDto);
        return right(snakesMoved());
    }

    private Option<Position> getFoodPosition() {
        return foodLocator.getFoodPosition();
    }

    private SnakesMoved snakesMoved() {
        return SnakesMoved.createEvent(getGameState());
    }

    private GameFinished gameFinished() {
        return GameFinished.createEvent(getGameState());
    }
}