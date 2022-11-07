package com.noscompany.snake.game.online.host.server.message.handler.internal.serializer;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;

public class NotSupportedMessageTypeException extends RuntimeException {

    public NotSupportedMessageTypeException(MessageType unknownMessageType) {
        super("failed deserialize message because of unknown message type: " + unknownMessageType);
    }
}