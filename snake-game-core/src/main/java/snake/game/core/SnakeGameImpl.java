package snake.game.core;

import lombok.AllArgsConstructor;
import snake.game.core.dto.Direction;
import snake.game.core.dto.GameState;
import snake.game.core.dto.SnakeNumber;
import snake.game.core.logic.GameLogic;
import snake.game.core.runner.GameRunner;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
class SnakeGameImpl implements SnakeGame {
    private final GameLogic gameLogic;
    private final GameRunner gameRunner;

    @Override
    public void changeSnakesDirection(Direction direction) {
        gameLogic.changeSnakeDirection(direction);
    }

    @Override
    public void changeSnakeDirection(SnakeNumber snakeNumber, Direction direction) {
        gameLogic.changeSnakeDirection(snakeNumber, direction);
    }

    @Override
    public void start() {
        gameRunner.start();
    }

    @Override
    public boolean isRunning() {
        return gameRunner.isRunning();
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
    public GameState getGameState() {
        return gameLogic.getCurrentState();
    }
}