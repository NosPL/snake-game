package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
class DeserializedMessage {
    private final Consumer<MessagePublisher> consumer;

    void applyTo(MessagePublisher messagePublisher) {
        consumer.accept(messagePublisher);
    }
}
