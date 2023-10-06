package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToPauseGame implements OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_PAUSE_GAME;
    UserId userId;
    Reason reason;

    public static FailedToPauseGame userNotInTheRoom(UserId userId) {
        return new FailedToPauseGame(userId, Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToPauseGame gameNotStarted(UserId userId) {
        return new FailedToPauseGame(userId, Reason.GAME_NOT_STARTED);
    }

    public static FailedToPauseGame playerDidNotTakeASeat(UserId userId) {
        return new FailedToPauseGame(userId, Reason.PLAYER_DID_NOT_TAKE_SEAT);
    }

    public static FailedToPauseGame playerIsNotAdmin(UserId userId) {
        return new FailedToPauseGame(userId, Reason.PLAYER_IS_NOT_ADMIN);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT,
        PLAYER_IS_NOT_ADMIN
    }
}