package com.noscompany.snake.game.online.client.internal.state.connected;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.ConnectionClosed;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.client.ClientEventHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Function;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class SocketMessageHandler implements Function<String> {
    private final MessagePublisher messagePublisher;
    private final MessageDeserializer messageDeserializer;

    void connectionClosedBecauseOfError() {
       messagePublisher.publishMessage(SendClientMessageError.CONNECTION_CLOSED);
    }

    void connectionClosed() {
        messagePublisher.publishMessage(new ConnectionClosed());
    }

    @Override
    public void on(String stringMessage) {
        log.info("received message from websocket: {}", stringMessage);
        log.info("deserializing message");
        messageDeserializer
                .deserialize(stringMessage)
                .onFailure(throwable -> log.warn("Failed to deserialize message, cause: ", throwable))
                .onSuccess(deserializedMessage -> log.info("Message successfully serialized, passing it to client event handler"))
                .onSuccess(deserializedMessage -> deserializedMessage.applyTo(messagePublisher));
    }

    static SocketMessageHandler create(MessagePublisher messagePublisher, ObjectMapper objectMapper) {
        MessageDeserializer messageDeserializer = new MessageDeserializer(objectMapper);
        return new SocketMessageHandler(messagePublisher, messageDeserializer);
    }
}