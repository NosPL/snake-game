package com.noscompany.snake.game.online.host.server;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;

class NotSupportedMessageTypeException extends RuntimeException {

    NotSupportedMessageTypeException(MessageType unknownMessageType) {
        super("failed deserialize message because of unknown message type: " + unknownMessageType);
    }
}