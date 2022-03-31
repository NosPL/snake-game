package snake.game.core.internal.logic;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.dto.events.GameContinues;
import snake.game.core.dto.events.GameFinished;

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
    public synchronized Either<GameFinished, GameContinues> moveSnakes() {
        killRequests.keySet().forEach(gameLogic::killSnake);
        directions.forEach(gameLogic::changeSnakeDirection);
        return gameLogic.moveSnakes();
    }

    @Override
    public GameState getGameState() {
        return gameLogic.getGameState();
    }
}