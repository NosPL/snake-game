package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import com.noscompany.snake.game.online.online.contract.serialization.OnlineMessageDeserializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class RoomWebsocketAdapter implements WebsocketEventHandler {
    private final MessagePublisher messagePublisher;
    private final OnlineMessageDeserializer messageDeserializer;

    @Override
    public void newClientConnected(UserId remoteClientId) {
        log.info("New remote client got connected, id: {}", remoteClientId.getId());
    }

    @Override
    public void messageReceived(UserId remoteClientId, String message) {
        log.info("Remote client with id {} sent a message: {}",remoteClientId.getId(), message);
        messageDeserializer
                .deserialize(message)
                .peek(messagePublisher::publishMessage);
    }

    @Override
    public void clientDisconnected(UserId remoteClientId) {
        log.info("Remote client with id {} got disconnected", remoteClientId.getId());
        messagePublisher.publishMessage(new RemoteClientDisconnected(remoteClientId));
    }

    static RoomWebsocketAdapter create(MessagePublisher messagePublisher, OnlineMessageDeserializer onlineMessageDeserializer) {
        return new RoomWebsocketAdapter(messagePublisher, onlineMessageDeserializer);
    }
}