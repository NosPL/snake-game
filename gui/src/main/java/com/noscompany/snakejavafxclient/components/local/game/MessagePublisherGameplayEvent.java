package com.noscompany.snakejavafxclient.components.local.game;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.gameplay.events.*;
import lombok.AllArgsConstructor;
import snake.game.gameplay.ports.GameplayEventHandler;

@AllArgsConstructor
final class MessagePublisherGameplayEvent implements GameplayEventHandler {
    private final MessagePublisher messagePublisher;

    @Override
    public void handle(GameStartCountdown event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameStarted event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(SnakesMoved event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameFinished event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameCancelled event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GamePaused event) {
        messagePublisher.publishMessage(event);
    }

    @Override
    public void handle(GameResumed event) {
        messagePublisher.publishMessage(event);
    }
}