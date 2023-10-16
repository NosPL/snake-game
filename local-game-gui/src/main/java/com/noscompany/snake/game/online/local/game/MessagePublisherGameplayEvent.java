package com.noscompany.snake.game.online.local.game;

import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import com.noscompany.snake.game.online.local.game.grid.GameGridController;
import com.noscompany.snake.game.online.local.game.message.MessageController;
import com.noscompany.snake.game.online.local.game.scoreboard.ScoreboardController;
import lombok.AllArgsConstructor;
import snake.game.gameplay.ports.GameplayEventHandler;

@AllArgsConstructor
final class MessagePublisherGameplayEvent implements GameplayEventHandler {
    private final ScoreboardController scoreboardController;
    private final GameGridController gameGridController;
    private final MessageController messageController;

    @Override
    public void handle(GameStartCountdown event) {
        gameGridController.gameStartCountdown(event);
        messageController.printSecondsLeftToStart(event.getSecondsLeft());
        scoreboardController.print(event.getScore());
    }

    @Override
    public void handle(GameStarted event) {
        gameGridController.gameStarted(event);
        messageController.printGameStarted();
        scoreboardController.print(event.getScore());
    }

    @Override
    public void handle(SnakesMoved event) {
        gameGridController.snakesMoved(event);
        messageController.clear();
        scoreboardController.print(event.getScore());
    }

    @Override
    public void handle(GameFinished event) {
        gameGridController.gameFinished(event);
        messageController.printGameFinished(event);
        scoreboardController.print(event.getScore());
    }

    @Override
    public void handle(GameCancelled event) {
        messageController.printGameCanceled();
    }

    @Override
    public void handle(GamePaused event) {
        messageController.printGamePaused();
    }

    @Override
    public void handle(GameResumed event) {
        messageController.printGameResumed();
    }
}