package com.noscompany.snake.game.online.host.server;

import com.noscompany.message.publisher.MessagePublisher;
import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.server.events.NewRemoteClientConnected;
import com.noscompany.snake.game.online.contract.messages.server.events.RemoteClientDisconnected;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class RoomWebsocketAdapter implements WebsocketEventHandler {
    private final MessagePublisher messagePublisher;
    private final MessageDeserializer messageDeserializer;

    @Override
    public void newClientConnected(UserId remoteClientId) {
        messagePublisher.publishMessage(new NewRemoteClientConnected(remoteClientId));
    }

    @Override
    public void messageReceived(UserId remoteClientId, String message) {
        messageDeserializer
                .deserialize(remoteClientId, message)
                .peek(messagePublisher::publishMessage);
    }

    @Override
    public void clientDisconnected(UserId remoteClientId) {
        messagePublisher.publishMessage(new RemoteClientDisconnected(remoteClientId));
    }

    static RoomWebsocketAdapter create(MessagePublisher messagePublisher) {
        return new RoomWebsocketAdapter(messagePublisher, new MessageDeserializer());
    }
}

