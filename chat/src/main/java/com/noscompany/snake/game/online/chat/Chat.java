package com.noscompany.snake.game.online.chat;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.SendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.room.NewUserEnteredRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserLeftRoom;
import com.noscompany.snake.game.online.contract.messages.room.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;

import java.util.Map;

import static io.vavr.control.Either.right;

@AllArgsConstructor
public class Chat {
    private final Map<UserId, UserName> userNamesByIds;

    public void newUserEnteredRoom(UserId userId, UserName userName) {
        userNamesByIds.put(userId, userName);
    }

    public void userLeftRoom(UserId userId) {
        userNamesByIds.remove(userId);
    }

    public Either<FailedToSendChatMessage, UserSentChatMessage> sendMessage(UserId userId, String messageContent) {
        return findUserName(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom(userId))
                .flatMap(userName -> sendChatMessage(userId, userName, messageContent));
    }

    private Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, UserName userName, String messageContent) {
        return Either.right(new UserSentChatMessage(userId, userName.getName(), messageContent));
    }

    private Option<UserName> findUserName(UserId userId) {
        return Option.of(userNamesByIds.get(userId));
    }
}