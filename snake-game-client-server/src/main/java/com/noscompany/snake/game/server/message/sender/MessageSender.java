package com.noscompany.snake.game.server.message.sender;

import com.noscompany.snake.game.commons.MessageDto;

public interface MessageSender {
    void sendTo(String userId, MessageDto messageDto);

    void sendToAll(MessageDto messageDto);
}
