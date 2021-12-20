package com.noscompany.snake.game.commons.messages.events.room;

import com.noscompany.snake.game.commons.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToEnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_ENTER_THE_ROOM;
    String userName;
    Reason reason;

    public enum Reason {
        USER_NAME_ALREADY_IN_USE,
        USER_ALREADY_IN_THE_ROOM,
        ROOM_IS_FULL,
        USER_NAME_CANNOT_BE_BLANK,
        USER_NAME_CANNOT_BE_LONGER_THAN_15_SIGNS
    }

    public static FailedToEnterRoom userNameAlreadyInUse(String userName) {
        return new FailedToEnterRoom(userName, Reason.USER_NAME_ALREADY_IN_USE);
    }

    public static FailedToEnterRoom userAlreadyInTheRoom(String userName) {
        return new FailedToEnterRoom(userName, Reason.USER_ALREADY_IN_THE_ROOM);
    }

    public static FailedToEnterRoom roomIsFull(String userName) {
        return new FailedToEnterRoom(userName, Reason.ROOM_IS_FULL);
    }
}