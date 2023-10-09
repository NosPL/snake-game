package snake.game.gameplay.internal.runner;

import com.noscompany.snake.game.online.contract.messages.gameplay.dto.CountdownTime;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameSpeed;
import com.noscompany.snake.game.online.contract.messages.gameplay.dto.GameState;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameCancelled;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameFinished;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GamePaused;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.GameResumed;
import lombok.RequiredArgsConstructor;
import snake.game.gameplay.internal.logic.GameLogic;
import snake.game.gameplay.ports.GameplayEventHandler;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStartCountdown.timeLeftToGameStartHasChanged;
import static com.noscompany.snake.game.online.contract.messages.gameplay.events.GameStarted.gameStarted;

@RequiredArgsConstructor
class GameTask implements Runnable {
    private final GameLogic gameLogic;
    private final GameplayEventHandler eventHandler;
    private final GameSpeed gameSpeed;
    private final CountdownTime countdownTime;
    private final AtomicBoolean pauseRequested;
    private final AtomicBoolean cancelRequested;
    private boolean shouldBeRunning = true;

    @Override
    public void run() {
        countdown();
        startGame();
        while (shouldBeRunning) {
            gameSpeedSleep();
            moveSnakes();
            pauseIfRequested();
            cancelIfRequested();
        }
    }

    private void countdown() {
        for (int i = countdownTime.inSeconds(); i > 0 && shouldBeRunning; i--) {
            var event = timeLeftToGameStartHasChanged(i, getGameState());
            eventHandler.handle(event);
            sleepInMillis(1000);
            pauseIfRequested();
            cancelIfRequested();
        }
    }

    private void startGame() {
        if (shouldBeRunning)
            eventHandler.handle(gameStarted(getGameState()));
    }

    private void moveSnakes() {
        gameLogic
                .moveSnakes()
                .peek(eventHandler::handle)
                .peekLeft(this::handle);
    }

    private void handle(GameFinished gameFinished) {
        eventHandler.handle(gameFinished);
        this.shouldBeRunning = false;
    }

    private void cancelIfRequested() {
        if (cancelRequested.get()) {
            eventHandler.handle(new GameCancelled());
            shouldBeRunning = false;
        }
    }

    private void pauseIfRequested() {
        if (pauseRequested.get()) {
            eventHandler.handle(new GamePaused());
            while (pauseRequested.get()) {
                sleepInMillis(200);
            }
            eventHandler.handle(new GameResumed());
        }
    }

    private void gameSpeedSleep() {
        int pauseTimeInMillis = gameSpeed.getPauseTimeInMillis();
        sleepInMillis(pauseTimeInMillis);
    }

    private void sleepInMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    private GameState getGameState() {
        return gameLogic.getGameState();
    }
}