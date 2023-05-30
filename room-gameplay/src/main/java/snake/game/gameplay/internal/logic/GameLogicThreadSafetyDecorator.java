package snake.game.gameplay.internal.logic;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
class GameLogicThreadSafetyDecorator implements GameLogic {
    private final GameLogic gameLogic;
    private final AtomicReference<GameState> gameState;
    private final ConcurrentHashMap<PlayerNumber, Direction> directions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<PlayerNumber, PlayerNumber> killRequests = new ConcurrentHashMap<>();

    @Override
    public void changeSnakeDirection(PlayerNumber playerNumber, Direction newDirection) {
        directions.put(playerNumber, newDirection);
    }

    @Override
    public void killSnake(PlayerNumber playerNumber) {
        killRequests.put(playerNumber, playerNumber);
    }

    @Override
    public Either<GameFinished, SnakesMoved> moveSnakes() {
        killRequests.keySet().forEach(gameLogic::killSnake);
        directions.forEach(gameLogic::changeSnakeDirection);
        var moveResult = gameLogic.moveSnakes();
        updateGameState(moveResult);
        return moveResult;
    }

    @Override
    public GameState getGameState() {
        return gameState.get();
    }

    private void updateGameState(Either<GameFinished, SnakesMoved> snakesMoveResult) {
        GameState gameState = snakesMoveResult.fold(this::toGameState, this::toGameState);
        this.gameState.set(gameState);
    }

    private GameState toGameState(SnakesMoved snakesMoved) {
        return new GameState(snakesMoved.getSnakes(), snakesMoved.getGridSize(), snakesMoved.getWalls(), snakesMoved.getFoodPosition(), snakesMoved.getScore());
    }

    private GameState toGameState(GameFinished gameFinished) {
        return new GameState(gameFinished.getSnakes(), gameFinished.getGridSize(), gameFinished.getWalls(), gameFinished.getFoodPosition(), gameFinished.getScore());
    }
}