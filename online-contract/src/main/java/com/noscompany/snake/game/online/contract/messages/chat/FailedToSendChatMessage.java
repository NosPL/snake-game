package com.noscompany.snake.game.online.contract.messages.chat;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FailedToSendChatMessage implements DedicatedClientMessage {
    MessageType messageType = MessageType.FAILED_TO_SEND_CHAT_MESSAGE;
    UserId userId;
    Reason reason;

    public enum Reason {
        USER_IS_NOT_IN_THE_ROOM
    }

    public static FailedToSendChatMessage userIsNotInTheRoom(UserId userId) {
        return new FailedToSendChatMessage(userId, Reason.USER_IS_NOT_IN_THE_ROOM);
    }
}