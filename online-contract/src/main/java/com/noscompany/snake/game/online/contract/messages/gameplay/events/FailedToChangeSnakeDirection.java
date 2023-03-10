package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToChangeSnakeDirection implements OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_CHANGE_DIRECTION;
    Reason reason;

    public static FailedToChangeSnakeDirection userNotInTheRoom() {
        return new FailedToChangeSnakeDirection(Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToChangeSnakeDirection gameNotStarted() {
        return new FailedToChangeSnakeDirection(Reason.GAME_NOT_STARTED);
    }

    public static FailedToChangeSnakeDirection playerDidNotTakeASeat() {
        return new FailedToChangeSnakeDirection(Reason.PLAYER_DID_NOT_TAKE_SEAT);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT
    }
}