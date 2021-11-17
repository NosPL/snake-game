package snake.game.core.runner;

import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.GameSpeed;
import snake.game.core.logic.GameLogic;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameRunnerCreator {
    public static GameRunner create(GameLogic gameLogic, SnakeGameEventHandler eventHandler, GameSpeed gameSpeed, CountdownTime countdownTime) {
        var pauseRequested = new AtomicBoolean(false);
        var cancelRequested = new AtomicBoolean(false);
        var snakeGameTask = new GameTask(gameLogic, eventHandler, gameSpeed, countdownTime, pauseRequested, cancelRequested);
        var gameThread = new Thread(snakeGameTask);
        return new GameRunnerImpl(gameThread, pauseRequested, cancelRequested);
    }
}