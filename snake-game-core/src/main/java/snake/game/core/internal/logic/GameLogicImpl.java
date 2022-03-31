package snake.game.core.internal.logic;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import snake.game.core.dto.*;
import snake.game.core.dto.events.GameContinues;
import snake.game.core.dto.events.GameFinished;
import snake.game.core.internal.logic.internal.SnakesMoved;
import snake.game.core.internal.logic.internal.current.game.state.CurrentGameState;
import snake.game.core.internal.logic.internal.food.locator.FoodLocator;
import snake.game.core.internal.logic.internal.scoring.Scoring;
import snake.game.core.internal.logic.internal.snakes.Snakes;

import java.util.Collection;

@AllArgsConstructor
class GameLogicImpl implements GameLogic {
    private final Snakes snakes;
    private final Scoring scoring;
    private final FoodLocator foodLocator;
    private final CurrentGameState currentGameState;

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
        return currentGameState.get();
    }

    @Override
    public Either<GameFinished, GameContinues> moveSnakes() {
        Option<Position> foodPosition = foodLocator.getFoodPosition();
        return snakes
                .moveAndFeed(foodPosition)
                .peek(foodLocator::updateFoodPosition)
                .peek(scoring::updateScores)
                .peek(this::updateCurrentGameStata)
                .map(snakesMoved -> gameContinues())
                .mapLeft(snakesDidNotMove -> gameFinished());
    }

    private void updateCurrentGameStata(SnakesMoved snakesMoved) {
        Collection<Snake> snakes = snakesMoved.getSnakes();
        Option<Position> foodPosition = foodLocator.getFoodPosition();
        Score score = scoring.getScore();
        currentGameState.update(snakes, foodPosition, score);
    }

    private GameContinues gameContinues() {
        return GameContinues.createEvent(getGameState());
    }

    private GameFinished gameFinished() {
        return GameFinished.createEvent(getGameState());
    }
}