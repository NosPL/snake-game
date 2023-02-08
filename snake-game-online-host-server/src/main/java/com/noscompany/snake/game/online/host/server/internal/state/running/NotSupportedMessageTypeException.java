package com.noscompany.snake.game.online.host.server.internal.state.running;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage.MessageType;

class NotSupportedMessageTypeException extends RuntimeException {

    NotSupportedMessageTypeException(MessageType unknownMessageType) {
        super("failed deserialize message because of unknown message type: " + unknownMessageType);
    }
}