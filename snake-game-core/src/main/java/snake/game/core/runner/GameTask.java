package snake.game.core.runner;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.dto.CountdownTime;
import snake.game.core.dto.GameSpeed;
import snake.game.core.events.*;
import snake.game.core.logic.GameLogic;

import java.util.concurrent.atomic.AtomicBoolean;

import static snake.game.core.events.GameStarted.gameStarted;

@RequiredArgsConstructor
class GameTask implements Runnable {
    private final GameLogic gameLogic;
    private final SnakeGameEventHandler eventHandler;
    private final GameSpeed gameSpeed;
    private final CountdownTime countdownTime;
    private final AtomicBoolean pauseRequested;
    private final AtomicBoolean cancelRequested;
    private boolean shouldStop = false;

    @Override
    public void run() {
        countdown();
        start();
        while (!shouldStop) {
            move();
        }
    }

    private void start() {
        if (!shouldStop)
            eventHandler.handle(gameStarted(gameLogic.getCurrentState()));
    }

    private void countdown() {
        for (int i = countdownTime.inSeconds(); i > 0 && !shouldStop; i--) {
            eventHandler.handle(TimeLeftToGameStartHasChanged.countdownEvent(i, gameLogic.getCurrentState()));
            sleepOneSecond();
            pauseIfRequested();
            cancelIfRequested();
        }
    }

    private void move() {
        gameSpeed.pause();
        gameLogic.move()
                .peek(eventHandler::handle)
                .peekLeft(this::handle);
        pauseIfRequested();
        cancelIfRequested();
    }

    private void handle(GameFinished gameFinished) {
        eventHandler.handle(gameFinished);
        this.shouldStop = true;
    }

    private void cancelIfRequested() {
        if (cancelRequested.get()) {
            eventHandler.handle(GameCancelled.gameCancelled(gameLogic.getCurrentState()));
            shouldStop = true;
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

    @SneakyThrows
    private void sleepOneSecond() {
        Thread.sleep(1000);
    }
}