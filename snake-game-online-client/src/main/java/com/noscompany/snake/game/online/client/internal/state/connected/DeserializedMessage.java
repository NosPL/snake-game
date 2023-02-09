package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.snake.game.online.client.ClientEventHandler;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
class DeserializedMessage {
    private final Consumer<ClientEventHandler> consumer;

    void applyTo(ClientEventHandler clientEventHandler) {
        consumer.accept(clientEventHandler);
    }
}
