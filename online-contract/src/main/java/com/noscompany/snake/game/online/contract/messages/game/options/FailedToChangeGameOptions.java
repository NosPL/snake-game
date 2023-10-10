package com.noscompany.snake.game.online.contract.messages.game.options;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToChangeGameOptions implements DedicatedClientMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_CHANGE_GAME_OPTIONS;
    UserId userId;
    Reason reason;

    public static FailedToChangeGameOptions requesterIsNotAdmin(UserId userId) {
        return new FailedToChangeGameOptions(userId, Reason.REQUESTER_IS_NOT_ADMIN);
    }

    public static FailedToChangeGameOptions gameIsAlreadyRunning(UserId userId) {
        return new FailedToChangeGameOptions(userId, Reason.GAME_IS_ALREADY_RUNNING);
    }

    public static FailedToChangeGameOptions requesterDidNotTakeASeat(UserId userId) {
        return new FailedToChangeGameOptions(userId, Reason.REQUESTER_DID_NOT_TAKE_A_SEAT);
    }

    public static FailedToChangeGameOptions userNotInTheRoom(UserId userId) {
        return new FailedToChangeGameOptions(userId, Reason.USER_IS_NOT_IN_ROOM);
    }

    public enum Reason {
        REQUESTER_IS_NOT_ADMIN,
        GAME_IS_ALREADY_RUNNING,
        REQUESTER_DID_NOT_TAKE_A_SEAT,
        USER_IS_NOT_IN_ROOM
    }
}