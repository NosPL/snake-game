package snake.game.gameplay.internal.runner;

import snake.game.gameplay.ports.GameplayEventHandler;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.CountdownTime;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import snake.game.gameplay.internal.logic.GameLogic;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameRunnerCreator {
    public static GameRunner create(GameLogic gameLogic,
                                    GameplayEventHandler eventHandler,
                                    GameSpeed gameSpeed,
                                    CountdownTime countdownTime) {
        var pauseRequested = new AtomicBoolean(false);
        var cancelRequested = new AtomicBoolean(false);
        var snakeGameTask = new GameTask(gameLogic, eventHandler, gameSpeed, countdownTime, pauseRequested, cancelRequested);
        var gameThread = new Thread(snakeGameTask, "gameplay-runner-thread");
        return new GameRunner(gameThread, pauseRequested, cancelRequested);
    }
}