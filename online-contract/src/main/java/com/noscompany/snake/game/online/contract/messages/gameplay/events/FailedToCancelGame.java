package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.DedicatedClientMessage;
import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import com.noscompany.snake.game.online.contract.messages.UserId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame.Reason.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToCancelGame implements DedicatedClientMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_CANCEL_GAME;
    UserId userId;
    Reason reason;

    public static FailedToCancelGame userNotInTheRoom(UserId userId) {
        return new FailedToCancelGame(userId, USER_NOT_IN_THE_ROOM);
    }

    public static FailedToCancelGame gameNotStarted(UserId userId) {
        return new FailedToCancelGame(userId, GAME_NOT_STARTED);
    }

    public static FailedToCancelGame playerDidNotTakeASeat(UserId userId) {
        return new FailedToCancelGame(userId, PLAYER_DID_NOT_TAKE_SEAT);
    }

    public static FailedToCancelGame playerIsNotAdmin(UserId userId) {
        return new FailedToCancelGame(userId, PLAYER_IS_NOT_ADMIN);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT,
        PLAYER_IS_NOT_ADMIN
    }
}