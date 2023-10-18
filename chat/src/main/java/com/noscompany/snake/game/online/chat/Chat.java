package com.noscompany.snake.game.online.chat;

import com.noscompany.snake.game.online.contract.messages.UserId;
import com.noscompany.snake.game.online.contract.messages.chat.FailedToSendChatMessage;
import com.noscompany.snake.game.online.contract.messages.chat.UserSentChatMessage;
import com.noscompany.snake.game.online.contract.messages.user.registry.UserName;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class Chat {
    private final Map<UserId, UserName> userNamesByIds;

    public void newUserEnteredRoom(UserId userId, UserName userName) {
        log.info("New user with id {} and name {}  entered room", userId.getId(), userName.getName());
        userNamesByIds.put(userId, userName);
    }

    public void userLeftRoom(UserId userId) {
        log.info("user with id {} left the room", userId.getId());
        userNamesByIds.remove(userId);
    }

    public Either<FailedToSendChatMessage, UserSentChatMessage> sendMessage(UserId userId, String messageContent) {
        log.info("user with id {} tries to send message: {}", userId.getId(), messageContent);
        return findUserName(userId)
                .toEither(FailedToSendChatMessage.userIsNotInTheRoom(userId))
                .peekLeft(failure -> log.info("user failed to send chat message, reason: {}", failure.getReason()))
                .flatMap(userName -> sendChatMessage(userId, userName, messageContent))
                .peek(success -> log.info("user sent the message"));
    }

    private Either<FailedToSendChatMessage, UserSentChatMessage> sendChatMessage(UserId userId, UserName userName, String messageContent) {
        return Either.right(new UserSentChatMessage(userId, userName, messageContent));
    }

    private Option<UserName> findUserName(UserId userId) {
        return Option.of(userNamesByIds.get(userId));
    }
}