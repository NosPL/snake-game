package com.noscompany.snake.game.server.lobby;

import com.noscompany.snake.game.server.message.sender.MessageSender;
import lombok.AllArgsConstructor;
import snake.game.core.SnakeGameEventHandler;
import snake.game.core.events.*;

@AllArgsConstructor
class SendToAllEventsDecorator implements SnakeGameEventHandler {
    private final SnakeGameEventHandler snakeGameEventHandler;
    private final MessageSender messageSender;

    @Override
    public void handle(TimeLeftToGameStartHasChanged event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GameStarted event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GameContinues event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GameFinished event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GameCancelled event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GamePaused event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }

    @Override
    public void handle(GameResumed event) {
        messageSender.sendToAll(event);
        snakeGameEventHandler.handle(event);
    }
}