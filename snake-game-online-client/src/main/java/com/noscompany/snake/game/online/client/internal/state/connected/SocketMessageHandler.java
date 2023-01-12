package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Function;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class SocketMessageHandler implements Function<String> {
    private final ClientEventHandler eventHandler;
    private final MessageDeserializer messageDeserializer;

    void connectionClosedBecauseOfError() {
        eventHandler.handle(SendClientMessageError.CONNECTION_CLOSED);
    }

    void connectionClosed() {
        eventHandler.connectionClosed();
    }

    @Override
    public void on(String messageFromSocket) {
        log.info("received message from websocket: {}", messageFromSocket);
        log.info("deserializing message");
        messageDeserializer
                .deserialize(messageFromSocket)
                .onFailure(throwable -> log.warn("Failed to deserialize message, cause: ", throwable))
                .onSuccess(deserializedMessage -> log.info("Message successfully serialized, passing it to client event handler"))
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(eventHandler));
    }

    static SocketMessageHandler create(ClientEventHandler eventHandler, ObjectMapper objectMapper) {
        MessageDeserializer messageDeserializer = new MessageDeserializer(objectMapper);
        return new SocketMessageHandler(eventHandler, messageDeserializer);
    }
}