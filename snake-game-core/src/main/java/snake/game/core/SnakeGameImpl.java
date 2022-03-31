package snake.game.core;

import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.PlayerNumber;
import snake.game.core.internal.logic.GameLogic;
import snake.game.core.internal.runner.GameRunner;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
class SnakeGameImpl implements SnakeGame {
    private final GameLogic gameLogic;
    private final GameRunner gameRunner;

    @Override
    public void changeSnakeDirection(PlayerNumber playerNumber, Direction direction) {
        gameLogic.changeSnakeDirection(playerNumber, direction);
    }

    @Override
    public void killSnake(PlayerNumber playerNumber) {
        gameLogic.killSnake(playerNumber);
    }

    @Override
    public GameState getGameState() {
        return gameLogic.getGameState();
    }

    @Override
    public void start() {
        gameRunner.start();
    }

    @Override
    public void cancel() {
        gameRunner.cancel();
    }

    @Override
    public void pause() {
        gameRunner.pause();
    }

    @Override
    public void resume() {
        gameRunner.resume();
    }

    @Override
    public boolean isRunning() {
        return gameRunner.isRunning();
    }
}