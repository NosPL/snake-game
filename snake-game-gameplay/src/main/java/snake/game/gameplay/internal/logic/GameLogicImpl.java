package snake.game.gameplay.internal.logic;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Position;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import snake.game.gameplay.internal.logic.internal.SnakesGotMoved;
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
    public Either<GameFinished, SnakesMoved> moveSnakes() {
        return snakes
                .moveAndFeed(getFoodPosition())
                .map(snakesGotMoved -> {
                    foodLocator.updateFoodPosition(snakesGotMoved);
                    scoring.updateScores(snakesGotMoved);
                    updateGameStateView(snakesGotMoved);
                    return snakesMoved();})
                .mapLeft(snakesDidNotMoveBecauseAllAreDead -> gameFinished());
    }

    private void updateGameStateView(SnakesGotMoved snakesGotMoved) {
        gameStateView.update(
                snakesGotMoved.getSnakes(),
                getFoodPosition(),
                scoring.getScore());
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