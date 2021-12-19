package com.noscompany.snake.game.commons.messages.events.chat;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FailedToSendChatMessage implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_SEND_CHAT_MESSAGE;
    Reason reason;

    public enum Reason {
        USER_IS_NOT_IN_THE_ROOM
    }

    public static FailedToSendChatMessage userIsNotInTheRoom() {
        return new FailedToSendChatMessage(Reason.USER_IS_NOT_IN_THE_ROOM);
    }
}