package com.noscompany.snake.game.online.contract.messages.gameplay.events;

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
public class FailedToChangeSnakeDirection implements DedicatedClientMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_CHANGE_DIRECTION;
    UserId userId;
    Reason reason;

    public static FailedToChangeSnakeDirection userNotInTheRoom(UserId userId) {
        return new FailedToChangeSnakeDirection(userId, Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToChangeSnakeDirection gameNotStarted(UserId userId) {
        return new FailedToChangeSnakeDirection(userId, Reason.GAME_NOT_STARTED);
    }

    public static FailedToChangeSnakeDirection playerDidNotTakeASeat(UserId userId) {
        return new FailedToChangeSnakeDirection(userId, Reason.PLAYER_DID_NOT_TAKE_SEAT);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT
    }
}