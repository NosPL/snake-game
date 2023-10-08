package com.noscompany.snake.game.online.contract.messages.user.registry;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToEnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_ENTER_THE_ROOM;
    UserId userId;
    Reason reason;

    public enum Reason {
        USER_NAME_ALREADY_IN_USE,
        USER_ALREADY_IN_THE_ROOM,
        ROOM_IS_FULL,
        INCORRECT_USER_NAME_FORMAT
    }

    public static FailedToEnterRoom userNameAlreadyInUse(UserId userId) {
        return new FailedToEnterRoom(userId, Reason.USER_NAME_ALREADY_IN_USE);
    }

    public static FailedToEnterRoom userAlreadyInTheRoom(UserId userId) {
        return new FailedToEnterRoom(userId, Reason.USER_ALREADY_IN_THE_ROOM);
    }

    public static FailedToEnterRoom roomIsFull(UserId userId) {
        return new FailedToEnterRoom(userId, Reason.ROOM_IS_FULL);
    }

    public static FailedToEnterRoom incorrectUserNameFormat(UserId userId) {
        return new FailedToEnterRoom(userId, Reason.INCORRECT_USER_NAME_FORMAT);
    }
}