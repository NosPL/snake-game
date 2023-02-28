package com.noscompany.snake.game.online.host.room.internal.chat;

import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import io.vavr.control.Either;

import static io.vavr.control.Either.right;

public class Chat {

    public Either<FailedToSendChatMessage, UserSentChatMessage> sendMessage(UserName userName, String messageContent) {
        UserSentChatMessage userSentChatMessage = new UserSentChatMessage(userName.getName(), messageContent);
        return right(userSentChatMessage);
    }
}