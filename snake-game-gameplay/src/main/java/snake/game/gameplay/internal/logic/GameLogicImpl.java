package snake.game.gameplay.internal.logic;

import com.noscompany.snake.game.online.contract.messages.game.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.game.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.game.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.game.dto.Position;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.game.events.GameContinues;
import com.noscompany.snake.game.online.contract.messages.game.events.GameFinished;
import snake.game.gameplay.internal.logic.internal.SnakesDidNotMoveBecauseAllAreDead;
import snake.game.gameplay.internal.logic.internal.SnakesMoved;
import snake.game.gameplay.internal.logic.internal.current.game.state.GameStateView;
import snake.game.gameplay.internal.logic.internal.food.locator.FoodLocator;
import snake.game.gameplay.internal.logic.internal.scoring.Scoring;
import snake.game.gameplay.internal.logic.internal.snakes.Snakes;

@AllArgsConstructor
class GameLogicImpl implements GameLogic {
    private final Snakes snakes;
    private final Scoring scoring;
    private final FoodLocator foodLocator;
    private final GameStateView gameStateView;

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
        return gameStateView.get();
    }

    @Override
    public Either<GameFinished, GameContinues> moveSnakes() {
        return snakes
                .moveAndFeed(getFoodPosition())
                .map(snakesMoved -> {
                    foodLocator.updateFoodPosition(snakesMoved);
                    scoring.updateScores(snakesMoved);
                    updateGameStateView(snakesMoved);
                    return gameContinues();})
                .mapLeft(snakesDidNotMoveBecauseAllAreDead -> gameFinished());
    }

    private void updateGameStateView(SnakesMoved snakesMoved) {
        gameStateView.update(
                snakesMoved.getSnakes(),
                getFoodPosition(),
                scoring.getScore());
    }

    private Option<Position> getFoodPosition() {
        return foodLocator.getFoodPosition();
    }

    private GameContinues gameContinues() {
        return GameContinues.createEvent(getGameState());
    }

    private GameFinished gameFinished() {
        return GameFinished.createEvent(getGameState());
    }
}