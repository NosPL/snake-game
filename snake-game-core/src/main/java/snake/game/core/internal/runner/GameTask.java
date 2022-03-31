package snake.game.core.internal.runner;

import lombok.RequiredArgsConstructor;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.GameSpeed;
import snake.game.core.dto.GameState;
import snake.game.core.dto.events.GameFinished;
import snake.game.core.dto.events.GamePaused;
import snake.game.core.dto.events.GameResumed;
import snake.game.core.internal.logic.GameLogic;

import java.util.concurrent.atomic.AtomicBoolean;

import static snake.game.core.dto.events.GameCancelled.gameCancelled;
import static snake.game.core.dto.events.GameStarted.gameStarted;
import static snake.game.core.dto.events.TimeLeftToGameStartHasChanged.timeLeftToGameStartHasChanged;

@RequiredArgsConstructor
class GameTask implements Runnable {
    private final GameLogic gameLogic;
    private final SnakeGameEventHandler eventHandler;
    private final GameSpeed gameSpeed;
    private final CountdownTime countdownTime;
    private final AtomicBoolean pauseRequested;
    private final AtomicBoolean cancelRequested;
    private boolean shouldBeRunning = true;

    @Override
    public void run() {
        countdown();
        start();
        while (shouldBeRunning) {
            move();
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

    private void start() {
        if (shouldBeRunning)
            eventHandler.handle(gameStarted(getGameState()));
    }

    private void move() {
        gameSpeedPause();
        gameLogic
                .moveSnakes()
                .peek(eventHandler::handle)
                .peekLeft(this::handle);
        pauseIfRequested();
        cancelIfRequested();
    }

    private void handle(GameFinished gameFinished) {
        eventHandler.handle(gameFinished);
        this.shouldBeRunning = false;
    }

    private void cancelIfRequested() {
        if (cancelRequested.get()) {
            eventHandler.handle(gameCancelled(getGameState()));
            shouldBeRunning = false;
        }
    }

    private void pauseIfRequested() {
        if (pauseRequested.get()) {
            eventHandler.handle(new GamePaused());
            while (pauseRequested.get()) {
            }
            eventHandler.handle(new GameResumed());
        }
    }

    private void gameSpeedPause() {
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