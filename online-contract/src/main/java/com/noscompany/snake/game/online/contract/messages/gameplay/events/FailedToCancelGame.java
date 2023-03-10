package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.noscompany.snake.game.online.contract.messages.gameplay.events.FailedToCancelGame.Reason.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToCancelGame implements OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_CANCEL_GAME;
    Reason reason;

    public static FailedToCancelGame userNotInTheRoom() {
        return new FailedToCancelGame(USER_NOT_IN_THE_ROOM);
    }

    public static FailedToCancelGame gameNotStarted() {
        return new FailedToCancelGame(GAME_NOT_STARTED);
    }

    public static FailedToCancelGame playerDidNotTakeASeat() {
        return new FailedToCancelGame(PLAYER_DID_NOT_TAKE_SEAT);
    }

    public static FailedToCancelGame playerIsNotAdmin() {
        return new FailedToCancelGame(PLAYER_IS_NOT_ADMIN);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT,
        PLAYER_IS_NOT_ADMIN
    }
}