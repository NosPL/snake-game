package com.noscompany.snake.game.online.host.room.internal.chat;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import io.vavr.control.Either;

public interface Chat {

    Either<FailedToSendChatMessage, UserSentChatMessage> sendMessage(String userName, String messageContent);

    enum Failure {
        MESSAGE_TO_LONG
    }

    enum MessageSent {
        MESSAGE_SENT
    }
}