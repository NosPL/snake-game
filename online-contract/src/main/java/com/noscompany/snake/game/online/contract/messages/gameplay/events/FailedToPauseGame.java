package com.noscompany.snake.game.online.contract.messages.gameplay.events;

import com.noscompany.snake.game.online.contract.messages.OnlineMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@NoArgsConstructor(force = true, access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class FailedToPauseGame implements OnlineMessage {
    OnlineMessage.MessageType messageType = MessageType.FAILED_TO_PAUSE_GAME;
    Reason reason;

    public static FailedToPauseGame userNotInTheRoom() {
        return new FailedToPauseGame(Reason.USER_NOT_IN_THE_ROOM);
    }

    public static FailedToPauseGame gameNotStarted() {
        return new FailedToPauseGame(Reason.GAME_NOT_STARTED);
    }

    public static FailedToPauseGame playerDidNotTakeASeat() {
        return new FailedToPauseGame(Reason.PLAYER_DID_NOT_TAKE_SEAT);
    }

    public static FailedToPauseGame playerIsNotAdmin() {
        return new FailedToPauseGame(Reason.PLAYER_IS_NOT_ADMIN);
    }

    public enum Reason {
        USER_NOT_IN_THE_ROOM,
        GAME_NOT_STARTED,
        PLAYER_DID_NOT_TAKE_SEAT,
        PLAYER_IS_NOT_ADMIN
    }
}