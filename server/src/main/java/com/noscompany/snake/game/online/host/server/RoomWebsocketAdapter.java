package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class RoomWebsocketAdapter implements WebsocketEventHandler {
    private final MessagePublisher messagePublisher;
    private final MessageDeserializer messageDeserializer;

    @Override
    public void newClientConnected(UserId remoteClientId) {
        log.info("New remote client got connected, id: {}", remoteClientId.getId());
    }

    @Override
    public void messageReceived(UserId remoteClientId, String message) {
        log.info("Remote client with id {} sent a message: {}",remoteClientId.getId(), message);
        messageDeserializer
                .deserialize(remoteClientId, message)
                .onSuccess(messagePublisher::publishMessage)
                .onFailure(t -> log.error("Failed to serialize incoming message, reason: ", t));
    }

    @Override
    public void clientDisconnected(UserId remoteClientId) {
        log.info("Remote client with id {} got disconnected", remoteClientId.getId());
        messagePublisher.publishMessage(new RemoteClientDisconnected(remoteClientId));
    }

    static RoomWebsocketAdapter create(MessagePublisher messagePublisher) {
        return new RoomWebsocketAdapter(messagePublisher, new MessageDeserializer());
    }
}

