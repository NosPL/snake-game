package snake.game.gameplay;

import lombok.AllArgsConstructor;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.Direction;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.PlayerNumber;
import snake.game.gameplay.internal.logic.GameLogic;
import snake.game.gameplay.internal.runner.GameRunner;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
class SnakeGameplayImpl implements SnakeGameplay {
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