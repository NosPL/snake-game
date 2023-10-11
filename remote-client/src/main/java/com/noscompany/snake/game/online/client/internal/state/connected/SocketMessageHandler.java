package com.noscompany.snake.game.online.client.internal.state.connected;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.client.ConnectionClosed;
import com.noscompany.snake.game.online.client.SendClientMessageError;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.wasync.Function;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class SocketMessageHandler implements Function<String> {
    private final MessagePublisher messagePublisher;
    private final OnlineMessageDeserializer deserializer;

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
        deserializer
                .deserialize(stringMessage)
                .peek(messagePublisher::publishMessage);
    }

    static SocketMessageHandler create(MessagePublisher messagePublisher, OnlineMessageDeserializer deserializer) {
        return new SocketMessageHandler(messagePublisher, deserializer);
    }
}