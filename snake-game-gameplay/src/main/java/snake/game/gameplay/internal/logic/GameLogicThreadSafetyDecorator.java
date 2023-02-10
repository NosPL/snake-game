package snake.game.gameplay.internal.logic;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.SnakesMoved;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
class GameLogicThreadSafetyDecorator implements GameLogic {
    private final GameLogic gameLogic;
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
        return gameLogic.moveSnakes();
    }

    @Override
    public GameState getGameState() {
        return gameLogic.getGameState();
    }
}