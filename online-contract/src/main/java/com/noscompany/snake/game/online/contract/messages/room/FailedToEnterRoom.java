package com.noscompany.snake.game.online.contract.messages.room;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToEnterRoom implements OnlineMessage {
    MessageType messageType = MessageType.FAILED_TO_ENTER_THE_ROOM;
    Reason reason;

    public enum Reason {
        USER_NAME_ALREADY_IN_USE,
        USER_ALREADY_IN_THE_ROOM,
        ROOM_IS_FULL,
        INCORRECT_USER_NAME_FORMAT
    }

    public static FailedToEnterRoom userNameAlreadyInUse() {
        return new FailedToEnterRoom(Reason.USER_NAME_ALREADY_IN_USE);
    }

    public static FailedToEnterRoom userAlreadyInTheRoom() {
        return new FailedToEnterRoom(Reason.USER_ALREADY_IN_THE_ROOM);
    }

    public static FailedToEnterRoom roomIsFull() {
        return new FailedToEnterRoom(Reason.ROOM_IS_FULL);
    }

    public static FailedToEnterRoom incorrectUserNameFormat() {
        return new FailedToEnterRoom(Reason.INCORRECT_USER_NAME_FORMAT);
    }
}